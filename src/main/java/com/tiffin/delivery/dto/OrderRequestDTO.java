package com.tiffin.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @NotBlank
    private Long tiffinPlanId;
    @NotBlank
    private Long bookedBy;
    @NotBlank
    private Date startFrom;
    @NotBlank
    private Date endTo;
    @NotBlank
    private  int numberOfDays;
}