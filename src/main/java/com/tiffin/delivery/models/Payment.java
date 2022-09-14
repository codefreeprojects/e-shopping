package com.tiffin.delivery.models;

import com.tiffin.delivery.enums.PaymentMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private PaymentMethodEnum paymentMethod;
    @OneToOne(cascade = CascadeType.DETACH)
    private TiffinOrder tiffinOrder;
    private String transactionId;
    private String otherData;
    private Date createdOn;
}
