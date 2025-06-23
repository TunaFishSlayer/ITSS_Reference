package com.darian.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String category;
    private String name;
    private String description;
    private Float weight; // Thêm trường weight để lưu trọng lượng sản phẩm
    private boolean rushEligible; // Thêm trường này để xác định sản phẩm có đủ điều kiện giao hàng nhanh hay không
    private String barcode;
    private Float price;
    private String specifications;
    private List<String> images;  // Lưu danh sách URL thay vì kiểu String đơn lẻ
}
