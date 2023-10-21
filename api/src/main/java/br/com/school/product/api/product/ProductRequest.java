package br.com.school.product.api.product;

import br.com.school.product.domain.product.ProductEntity;
import java.math.BigDecimal;

public record ProductRequest(String sku,
                             String name,
                             BigDecimal stock,
                             BigDecimal cost,
                             BigDecimal price) {

    public ProductEntity toEntity() {
        return ProductEntity.create(sku, name, stock, cost, price);
    }

    public ProductEntity toEntity(String id) {
        return ProductEntity.with(id, sku, name, stock, cost, price);
    }

}
