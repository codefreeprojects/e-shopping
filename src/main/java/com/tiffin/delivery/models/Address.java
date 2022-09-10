package com.tiffin.delivery.models;

import com.tiffin.delivery.enums.AddressAreaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private AddressAreaEnum area;
    private String pin;
}
