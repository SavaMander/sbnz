export interface GetOrdersRequest {
    orderId: string; 
    restaurant: string;
    orderList: string; 
    totalPrice: number;
    creationDate: string; 
}