package com.tiffin.delivery.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tiffin.delivery.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity @NoArgsConstructor @AllArgsConstructor @Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private String aadharNumber;
    private UserRoleEnum role;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Address address;
    private Date createdOn;
    private Boolean active;
}
