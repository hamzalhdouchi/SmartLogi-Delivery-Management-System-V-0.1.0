import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { Parcel, ParcelStatus, ParcelStatistics } from '../models/parcel.model';
import { PagedResponse } from '../../../core/models/paged-response.model';

@Injectable({
    providedIn: 'root'
})
export class ParcelService {
    private readonly apiUrl = `${environment.apiUrl}/colis`;

    constructor(private http: HttpClient) { }

    getAllParcels(page: number = 0, size: number = 10, filters?: any): Observable<PagedResponse<Parcel>> {
        let params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString());

        if (filters) {
            Object.keys(filters).forEach(key => {
                if (filters[key]) {
                    params = params.set(key, filters[key]);
                }
            });
        }

        return this.http.get<PagedResponse<Parcel>>(this.apiUrl, { params })
            .pipe(catchError(this.handleError));
    }

    getParcelById(id: number): Observable<Parcel> {
        return this.http.get<Parcel>(`${this.apiUrl}/${id}`)
            .pipe(catchError(this.handleError));
    }

    getParcelByTrackingNumber(trackingNumber: string): Observable<Parcel> {
        return this.http.get<Parcel>(`${this.apiUrl}/track/${trackingNumber}`)
            .pipe(catchError(this.handleError));
    }

    createParcel(parcel: Parcel): Observable<Parcel> {
        return this.http.post<Parcel>(this.apiUrl, parcel)
            .pipe(catchError(this.handleError));
    }

    updateParcel(id: number, parcel: Parcel): Observable<Parcel> {
        return this.http.put<Parcel>(`${this.apiUrl}/${id}`, parcel)
            .pipe(catchError(this.handleError));
    }

    updateStatus(id: number, status: ParcelStatus, comment?: string): Observable<Parcel> {
        return this.http.put<Parcel>(`${this.apiUrl}/${id}/status`, { status, comment })
            .pipe(catchError(this.handleError));
    }

    assignToDriver(id: number, driverId: number): Observable<Parcel> {
        return this.http.post<Parcel>(`${this.apiUrl}/${id}/assign`, { driverId })
            .pipe(catchError(this.handleError));
    }

    getMyParcels(): Observable<Parcel[]> {
        return this.http.get<Parcel[]>(`${this.apiUrl}/my-packages`)
            .pipe(catchError(this.handleError));
    }

    deleteParcel(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`)
            .pipe(catchError(this.handleError));
    }

    getStatistics(): Observable<ParcelStatistics> {
        return this.http.get<ParcelStatistics>(`${environment.apiUrl}/statistics/packages`)
            .pipe(catchError(this.handleError));
    }

    private handleError(error: any): Observable<never> {
        console.error('ParcelService Error:', error);
        return throwError(() => error);
    }
}
