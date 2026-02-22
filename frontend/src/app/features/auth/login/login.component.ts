import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule, ReactiveFormsModule],
    templateUrl: './login.component.html',
    // styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
    loginForm!: FormGroup;
    isLoading = false;
    showPassword = false;
    returnUrl = '/dashboard';

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private route: ActivatedRoute,
        private notificationService: NotificationService
    ) { }

    ngOnInit(): void {
        if (this.authService.isAuthenticated()) {
            this.router.navigate(['/dashboard']);
            return;
        }

        this.initForm();
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
    }

    private initForm(): void {
        this.loginForm = this.fb.group({
            username: ['', [Validators.required]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    onSubmit(): void {
        if (this.loginForm.invalid) {
            this.loginForm.markAllAsTouched();
            return;
        }

        this.isLoading = true;
        this.authService.login(this.loginForm.value).subscribe({
            next: () => {
                this.notificationService.success('Connexion réussie !');
                this.router.navigate([this.returnUrl]);
            },
            error: (error) => {
                this.isLoading = false;
                if (error.status === 401) {
                    this.notificationService.error('Identifiants incorrects');
                }
            },
            complete: () => {
                this.isLoading = false;
            }
        });
    }

    togglePassword(): void {
        this.showPassword = !this.showPassword;
    }

    loginWithGoogle(): void {
        this.authService.loginWithGoogle();
    }

    loginWithGitHub(): void {
        this.authService.loginWithGitHub();
    }

    getError(field: string): string {
        const control = this.loginForm.get(field);
        if (control?.hasError('required')) {
            return 'Ce champ est requis';
        }
        if (control?.hasError('minlength')) {
            return `Minimum ${control.errors?.['minlength'].requiredLength} caractères`;
        }
        return '';
    }

    isFieldInvalid(field: string): boolean {
        const control = this.loginForm.get(field);
        return !!(control?.invalid && control?.touched);
    }
}
