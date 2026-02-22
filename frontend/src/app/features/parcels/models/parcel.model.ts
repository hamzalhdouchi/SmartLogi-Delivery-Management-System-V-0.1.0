export interface Parcel {
    id?: number;
    trackingNumber?: string;
    senderId: number;
    senderName?: string;
    recipientId: number;
    recipientName?: string;
    deliveryAddress: string;
    status: ParcelStatus;
    priority: Priority;
    zoneId: number;
    zoneName?: string;
    driverId?: number;
    driverName?: string;
    weight: number;
    description?: string;
    estimatedDeliveryDate: Date;
    actualDeliveryDate?: Date;
    createdAt?: Date;
    updatedAt?: Date;
}

export enum ParcelStatus {
    PENDING = 'PENDING',
    ASSIGNED = 'ASSIGNED',
    COLLECTED = 'COLLECTED',
    IN_TRANSIT = 'IN_TRANSIT',
    OUT_FOR_DELIVERY = 'OUT_FOR_DELIVERY',
    DELIVERED = 'DELIVERED',
    FAILED = 'FAILED',
    CANCELLED = 'CANCELLED',
    RETURNED = 'RETURNED'
}

export enum Priority {
    LOW = 'LOW',
    NORMAL = 'NORMAL',
    HIGH = 'HIGH',
    URGENT = 'URGENT'
}

export interface Client {
    id: number;
    name: string;
    email: string;
    phone: string;
    address: string;
    company?: string;
}

export interface Driver {
    id: number;
    name: string;
    email: string;
    phone: string;
    licenseNumber: string;
    vehicleType: string;
    zoneId: number;
    available: boolean;
}

export interface Zone {
    id: number;
    name: string;
    code: string;
    description?: string;
    city: string;
    region: string;
}

export interface ParcelStatistics {
    total: number;
    pending: number;
    inProgress: number;
    delivered: number;
    delayed: number;
    cancelled: number;
}
