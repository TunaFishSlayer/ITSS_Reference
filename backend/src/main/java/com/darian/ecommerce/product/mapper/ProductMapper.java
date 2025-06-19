package com.darian.ecommerce.product.mapper;

import com.darian.ecommerce.product.entity.Product;
import com.darian.ecommerce.product.entity.ProductImage;
import com.darian.ecommerce.order.enums.AvailabilityStatus;
import com.darian.ecommerce.product.dto.*;
import com.darian.ecommerce.product.service.RelatedProductService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    private final RelatedProductService relatedProductService;
    private final ProductEditHistoryMapper productEditHistoryMapper;

    public ProductMapper(RelatedProductService relatedProductService, ProductEditHistoryMapper productEditHistoryMapper) {
        this.relatedProductService = relatedProductService;
        this.productEditHistoryMapper = productEditHistoryMapper;
    }

    // Private method to map Product entity to CustomerProductDTO
    public CustomerProductDTO toCustomerDTO(Product product) {
        //get images url
        List<String> images =  product.getImages().stream()
                .map(ProductImage::getUrl)
                .collect(Collectors.toList());

        //get availability status
        AvailabilityStatus availabilityStatus = switch (product.getStockQuantity()){
            case 0 -> AvailabilityStatus.OUT_OF_STOCK;
            default -> product.getStockQuantity() < 50
                    ? AvailabilityStatus.LOW_STOCK
                    : AvailabilityStatus.IN_STOCK;
        };

        //get related products from this product
        List<RelatedProductDTO> relatedProductDTOS = relatedProductService.suggestRelatedProducts(product.getProductId());

        return  CustomerProductDTO.builder()
                .productId(product.getProductId())
                .category(product.getCategory().getName())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .specifications(product.getSpecifications())
                .images(images)
                .availabilityStatus(availabilityStatus)
                .relatedProducts(relatedProductDTOS)
                .build();

    }

    // Private method to map Product entity to ManagerProductDTO
    public ManagerProductDTO toManagerDTO(Product product) {
        //get images url
        List<String> images =  product.getImages().stream()
                .map(ProductImage::getUrl)
                .collect(Collectors.toList());

        //get edit history
        List<ProductEditHistoryDTO> editHistoryDTOS = product.getEditHistory().stream()
                .map(productEditHistoryMapper::toDTO)
                .toList();

        return ManagerProductDTO.builder()
                .productId(product.getProductId())
                .category(product.getCategory().getName())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .specifications(product.getSpecifications())
                .images(images)
                .value(product.getValue())
                .barcode(product.getBarcode())
                .stockQuantity(product.getStockQuantity())
                .warehouseEntryDate(product.getWarehouseEntryDate())
                .editHistory(editHistoryDTOS)
                .build();
    }

    // Private method to map Product entity to ProductDTO
    private ProductDTO toProductDTO(Product product) {
        //get images url
        List<String> images =  product.getImages().stream()
                .map(ProductImage::getUrl)
                .collect(Collectors.toList());

        return ManagerProductDTO.builder()
                .productId(product.getProductId())
                .category(product.getCategory().getName())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .specifications(product.getSpecifications())
                .images(images)
                .build();
    }

}
