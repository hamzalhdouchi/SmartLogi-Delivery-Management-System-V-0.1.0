import { User } from './user.model';

export interface AuthResponse {
    token: string;
    type: string;
    refreshToken?: string;
    user: User;
}

export interface LoginRequest {
    username: string;
    password: string;
}

export interface RegisterRequest {
    username: string;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    phone?: string;
    address?: string;
}
