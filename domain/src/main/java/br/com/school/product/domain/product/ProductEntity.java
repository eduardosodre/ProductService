package br.com.school.product.domain.product;

import br.com.school.product.domain.exception.NotificationException;
import br.com.school.product.domain.validation.Error;
import br.com.school.product.domain.validation.NotificationValidation;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ProductEntity {

    private String id;
    private String sku;
    private String name;
    private BigDecimal stock;
    private BigDecimal cost;
    private BigDecimal price;

    private ProductEntity(final String id,
                          final String sku,
                          final String name,
                          final BigDecimal stock,
                          final BigDecimal cost,
                          final BigDecimal price) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.stock = stock;
        this.cost = cost;
        this.price = price;
        selfValidate();
    }

    public static ProductEntity create(final String sku,
                                       final String name,
                                       final BigDecimal stock,
                                       final BigDecimal cost,
                                       final BigDecimal price) {
        final var id = UUID.randomUUID().toString();
        return new ProductEntity(id, sku, name, stock, cost, price);
    }

    public void update(final String sku,
                       final String name,
                       final BigDecimal stock,
                       final BigDecimal cost,
                       final BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.stock = stock;
        this.cost = cost;
        this.price = price;
        selfValidate();
    }

    private void selfValidate() {
        final var notification = NotificationValidation.create();

        if (sku.isEmpty()) {
            notification.append(new Error("sku cannot be null"));
        }

        final var nameLength = name.trim().length();
        if (nameLength < 5 || nameLength > 200) {
            notification.append(new Error("name must be between 5 and 200 characters"));
        }

        if (stock.compareTo(BigDecimal.ZERO) < 0) {
            notification.append(new Error("stock must be positive"));
        }

        if (cost.compareTo(BigDecimal.ZERO) <= 0) {
            notification.append(new Error("cost must be greater than 0"));
        }

        if (price.compareTo(cost) <= 0) {
            notification.append(new Error("price must be greater than cost"));
        }

        if (notification.hasErrors()) {
            throw new NotificationException("failed to instance new product", notification);
        }
    }
}