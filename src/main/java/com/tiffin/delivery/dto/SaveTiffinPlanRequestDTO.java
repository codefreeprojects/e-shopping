package com.tiffin.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveTiffinPlanRequestDTO {
    @NotBlank
    private String planName;
    @NotBlank
    private Long pricePerDay;
    @NotBlank
    private MultipartFile bannerImg;
    @NotBlank
    private String description;
    @NotBlank
    private Long addressId;
}


