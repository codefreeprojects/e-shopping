package com.tiffin.delivery.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class TiffinPlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String planName;
    private Long pricePerDay;
    private String bannerUrl;
    private String description;
    private Date createdOn;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Address address;
}
