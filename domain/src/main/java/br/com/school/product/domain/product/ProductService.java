package br.com.school.product.domain.product;

import br.com.school.product.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public ProductEntity createProduct(ProductEntity entity) {
        return repository.save(entity);
    }

    public ProductEntity updateProduct(ProductEntity productFromBd, ProductEntity product) {
        productFromBd.update(product.getSku(), product.getName(), product.getStock(),
            product.getCost(), product.getPrice());
        return repository.save(productFromBd);
    }

    public ProductEntity getProductById(String id) {
        return repository.findById(id)
            .orElseThrow(
                () -> NotFoundException.create(
                    "product not found with id %s not found".formatted(id)));
    }

    public void deleteProduct(String id) {
        final var productFromBd = getProductById(id);
        repository.delete(productFromBd);
    }

    public Page<ProductEntity> findAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
