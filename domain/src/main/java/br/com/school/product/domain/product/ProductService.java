package br.com.school.product.domain.product;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    @PostConstruct
    public void create() {
        final var prod = ProductEntity.create("123",
            "nome do produto",
            BigDecimal.valueOf(1),
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(20));
        repository.save(prod);
    }
}
