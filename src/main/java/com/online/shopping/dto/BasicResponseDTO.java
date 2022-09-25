package com.online.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class BasicResponseDTO<T> {
    private boolean isSuccess;
    private String message;
    private T data;
}
