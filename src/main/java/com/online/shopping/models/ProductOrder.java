package com.online.shopping.models;

import com.online.shopping.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @OneToOne
    private Product product;
    @OneToOne
    private User user;
    @OneToOne
    private Payment payment;
    private String address;
    private String pinCode;
    private OrderStatus orderStatus;
    private Long quantity;
    private Date createdOn;


}
