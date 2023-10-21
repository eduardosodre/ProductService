package br.com.school.product.api.product;

import br.com.school.product.domain.product.ProductEntity;
import java.math.BigDecimal;

public record ProductResponse(String id,
                              String sku,
                              String name,
                              BigDecimal stock,
                              BigDecimal cost,
                              BigDecimal price) {

    public static ProductResponse fromEntity(ProductEntity entity) {
        return new ProductResponse(entity.getId(),
            entity.getSku(),
            entity.getName(),
            entity.getStock(),
            entity.getCost(),
            entity.getPrice());
    }

}
