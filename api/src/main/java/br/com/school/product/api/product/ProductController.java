package br.com.school.product.api.product;

import br.com.school.product.domain.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "create a new product")
    @ApiResponse(responseCode = "201", description = "product created successfully")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
        @RequestBody ProductRequest request) {
        final var productEntity = request.toEntity();
        final var savedProduct = service.createProduct(productEntity);
        final var productResponse = ProductResponse.fromEntity(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @Operation(summary = "Get a product by ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
        final var product = service.getProductById(id);
        final var response = ProductResponse.fromEntity(product);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update an existing product")
    @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "404", description = "Product not found")
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

    @Operation(summary = "Delete a product by ID")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteById(@PathVariable String id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "List of products")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        final var products = service.findAllProducts(pageable);
        final var responsePage = products.map(ProductResponse::fromEntity);
        return ResponseEntity.ok(responsePage);
    }
}
