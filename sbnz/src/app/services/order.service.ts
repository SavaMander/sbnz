import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OrderRequest } from '../dto/OrderRequest';
import { env } from '../env/env';
import { MessageResponse } from '../dto/MessageResponse';
import { Observable } from 'rxjs';
import { GetOrdersRequest } from '../dto/GetOrdersRequest';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private http: HttpClient) { }

    order(order: OrderRequest): Observable<MessageResponse> {
      return this.http.post<MessageResponse>(env.apiHost+'orders',order,{
      });
    }

    getMyOrders(email: string): Observable<GetOrdersRequest[]> {
        let params = new HttpParams().set('email', email);
        return this.http.get<GetOrdersRequest[]>(`${env.apiHost}orders`, { params: params });
    }

    cancelOrder(orderId: string, email: string): Observable<MessageResponse> {
        let params = new HttpParams().set('email', email);
        return this.http.put<MessageResponse>(
            `${env.apiHost}orders/${orderId}/cancel`, 
            null,
            { params: params }
        );
    }
}
