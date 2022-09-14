package com.tiffin.delivery.models;

import com.tiffin.delivery.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity @NoArgsConstructor @AllArgsConstructor @Data
public class TiffinOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TiffinPlan tiffinPlan;
    @OneToOne(cascade = CascadeType.DETACH)
    private User bookedBy;
    private Date startFrom;
    private Date endTo;
    @Temporal(TemporalType.DATE)
    private Date statusDate;
    private int totalDays;
    private Long totalPrice;
    private OrderStatusEnum orderStatus;
    @OneToOne(cascade = CascadeType.DETACH)
    private User assignedTo;
    @OneToOne(cascade = CascadeType.DETACH)
    private Address address;
    private Date createdOn;
}
