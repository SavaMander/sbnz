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
      return this.http.post<MessageResponse>(env.apiHost+'auth/order',order,{
      });
    }

    getMyOrders(email: string): Observable<GetOrdersRequest[]> {
        let params = new HttpParams().set('email', email);
        return this.http.get<GetOrdersRequest[]>(`${env.apiHost}auth/orders`, { params: params });
    }
}
