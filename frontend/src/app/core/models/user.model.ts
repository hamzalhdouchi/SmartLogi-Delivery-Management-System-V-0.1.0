export interface User {
    id: number;
    username: string;
    email: string;
    firstName: string;
    lastName: string;
    roles: string[];
    phone?: string;
    address?: string;
    createdAt: Date;
}
