import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../../environments/environment';
import { User } from '../models/user.model';

interface JwtPayload {
    sub: string;
    roles: string[];
    userId: number;
    email: string;
    firstName: string;
    lastName: string;
    exp: number;
    iat: number;
}

@Injectable({
    providedIn: 'root'
})
export class TokenService {
    private readonly tokenKey = environment.tokenKey;
    private readonly refreshTokenKey = environment.refreshTokenKey;

    saveToken(token: string): void {
        localStorage.setItem(this.tokenKey, token);
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    removeToken(): void {
        localStorage.removeItem(this.tokenKey);
        localStorage.removeItem(this.refreshTokenKey);
    }

    saveRefreshToken(refreshToken: string): void {
        localStorage.setItem(this.refreshTokenKey, refreshToken);
    }

    getRefreshToken(): string | null {
        return localStorage.getItem(this.refreshTokenKey);
    }

    hasValidToken(): boolean {
        const token = this.getToken();
        if (!token) {
            return false;
        }

        try {
            const decoded = jwtDecode<JwtPayload>(token);
            const now = Date.now() / 1000;
            return decoded.exp > now;
        } catch {
            return false;
        }
    }

    decodeToken(): User | null {
        const token = this.getToken();
        if (!token) {
            return null;
        }

        try {
            const decoded = jwtDecode<JwtPayload>(token);
            return {
                id: decoded.userId,
                username: decoded.sub,
                email: decoded.email,
                firstName: decoded.firstName,
                lastName: decoded.lastName,
                roles: decoded.roles || [],
                createdAt: new Date()
            };
        } catch {
            return null;
        }
    }

    getRoles(): string[] {
        const user = this.decodeToken();
        return user?.roles || [];
    }

    hasRole(role: string): boolean {
        return this.getRoles().includes(role);
    }
}
