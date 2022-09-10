package com.tiffin.delivery.dto;

import com.tiffin.delivery.enums.PaymentMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PaymentRequestDTO {
    @NotBlank
    private Long orderId;
    private PaymentMethodEnum paymentMethod;
    private String otherData;
}
