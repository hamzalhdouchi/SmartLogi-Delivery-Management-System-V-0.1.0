import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';
import { AuthResponse, LoginRequest, RegisterRequest } from '../models/auth-response.model';
import { User } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly apiUrl = environment.apiUrl;

    constructor(
        private http: HttpClient,
        private tokenService: TokenService,
        private router: Router
    ) { }

    login(credentials: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, credentials).pipe(
            tap(response => {
                this.tokenService.saveToken(response.token);
                if (response.refreshToken) {
                    this.tokenService.saveRefreshToken(response.refreshToken);
                }
            }),
            catchError(this.handleError)
        );
    }

    register(userData: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/auth/register`, userData).pipe(
            tap(response => {
                this.tokenService.saveToken(response.token);
                if (response.refreshToken) {
                    this.tokenService.saveRefreshToken(response.refreshToken);
                }
            }),
            catchError(this.handleError)
        );
    }

    logout(): void {
        this.tokenService.removeToken();
        this.router.navigate(['/login']);
    }

    isAuthenticated(): boolean {
        return this.tokenService.hasValidToken();
    }

    getCurrentUser(): User | null {
        return this.tokenService.decodeToken();
    }

    hasRole(role: string): boolean {
        return this.tokenService.hasRole(role);
    }

    hasAnyRole(roles: string[]): boolean {
        return roles.some(role => this.hasRole(role));
    }

    loginWithGoogle(): void {
        const googleAuthUrl = `${this.apiUrl}/oauth2/authorize/google?redirect_uri=${encodeURIComponent(environment.oAuth2.redirectUri)}&provider=google`;
        window.location.href = googleAuthUrl;
    }

    loginWithGitHub(): void {
        const githubAuthUrl = `${this.apiUrl}/oauth2/authorize/github?redirect_uri=${encodeURIComponent(environment.oAuth2.redirectUri)}&provider=github`;
        window.location.href = githubAuthUrl;
    }

    exchangeOAuthCode(code: string, provider: string): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/oauth2/callback`, { code, provider }).pipe(
            tap(response => {
                this.tokenService.saveToken(response.token);
                if (response.refreshToken) {
                    this.tokenService.saveRefreshToken(response.refreshToken);
                }
            }),
            catchError(this.handleError)
        );
    }

    private handleError(error: any): Observable<never> {
        console.error('AuthService Error:', error);
        return throwError(() => error);
    }
}
