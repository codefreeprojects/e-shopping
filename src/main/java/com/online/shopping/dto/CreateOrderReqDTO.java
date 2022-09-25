package com.online.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateOrderReqDTO {
    private Long productId;
    private Long userId;
    private PaymentDTO paymentDetails;
    private String address;
    private String pinCode;
}
