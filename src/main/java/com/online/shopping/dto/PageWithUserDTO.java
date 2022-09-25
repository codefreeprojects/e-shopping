package com.online.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PageWithUserDTO {
    @NotBlank
    private Long userId;
    @NotBlank
    private int page;
    @NotBlank
    private int size;
}
