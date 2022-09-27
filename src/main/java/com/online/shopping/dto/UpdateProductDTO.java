package com.online.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateProductDTO {
    private Long productId;
    private String name;
    private String type;
    private Integer price;
    private String details;
    private String company;
    private String quantity;
    private MultipartFile bannerImage;
}
