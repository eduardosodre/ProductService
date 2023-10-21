package br.com.school.product.api.product;

import br.com.school.product.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
        @RequestBody ProductRequest request) {
        final var productEntity = request.toEntity();
        final var savedProduct = service.createProduct(productEntity);
        final var productResponse = ProductResponse.fromEntity(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
        final var product = service.getProductById(id);
        final var response = ProductResponse.fromEntity(product);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable String id,
        @RequestBody ProductRequest request) {
        final var productFromBd = service.getProductById(id);
        final var productToUpdate = request.toEntity(id);
        final var savedProduct = service.updateProduct(productFromBd, productToUpdate);
        final var productResponse = ProductResponse.fromEntity(savedProduct);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteById(@PathVariable String id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        final var products = service.findAllProducts(pageable);
        final var responsePage = products.map(ProductResponse::fromEntity);
        return ResponseEntity.ok(responsePage);
    }
}
