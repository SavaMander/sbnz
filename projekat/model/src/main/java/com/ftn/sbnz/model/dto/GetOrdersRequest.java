package com.ftn.sbnz.model.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersRequest {
    private UUID orderId;
    String restaurant;
    String orderList;
    double totalPrice;

}
