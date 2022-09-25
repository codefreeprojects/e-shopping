package com.online.shopping.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.shopping.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity @NoArgsConstructor @AllArgsConstructor @Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private UserRoleEnum role;
    private Date createdOn;
}
