import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule, ReactiveFormsModule],
    templateUrl: './register.component.html',
    // styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
    registerForm!: FormGroup;
    isLoading = false;
    showPassword = false;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private notificationService: NotificationService
    ) { }

    ngOnInit(): void {
        if (this.authService.isAuthenticated()) {
            this.router.navigate(['/dashboard']);
            return;
        }

        this.initForm();
    }

    private initForm(): void {
        this.registerForm = this.fb.group({
            username: ['', [Validators.required, Validators.minLength(3)]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(8)]],
            confirmPassword: ['', [Validators.required]],
            firstName: ['', [Validators.required]],
            lastName: ['', [Validators.required]],
            phone: [''],
            address: ['']
        }, {
            validators: this.passwordMatchValidator
        });
    }

    private passwordMatchValidator(form: FormGroup) {
        const password = form.get('password');
        const confirmPassword = form.get('confirmPassword');

        if (password?.value !== confirmPassword?.value) {
            confirmPassword?.setErrors({ mismatch: true });
        }
        return null;
    }

    onSubmit(): void {
        if (this.registerForm.invalid) {
            this.registerForm.markAllAsTouched();
            return;
        }

        this.isLoading = true;
        const { confirmPassword, ...userData } = this.registerForm.value;

        this.authService.register(userData).subscribe({
            next: () => {
                this.notificationService.success('Inscription réussie ! Bienvenue sur SmartLogi.');
                this.router.navigate(['/dashboard']);
            },
            error: (error) => {
                this.isLoading = false;
                if (error.status === 409) {
                    this.notificationService.error('Cet email ou nom d\'utilisateur existe déjà');
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

    getError(field: string): string {
        const control = this.registerForm.get(field);
        if (control?.hasError('required')) return 'Ce champ est requis';
        if (control?.hasError('email')) return 'Email invalide';
        if (control?.hasError('minlength')) {
            return `Minimum ${control.errors?.['minlength'].requiredLength} caractères`;
        }
        if (control?.hasError('mismatch')) return 'Les mots de passe ne correspondent pas';
        return '';
    }

    isFieldInvalid(field: string): boolean {
        const control = this.registerForm.get(field);
        return !!(control?.invalid && control?.touched);
    }
}
