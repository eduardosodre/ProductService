package br.com.school.product.domain.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.school.product.domain.exception.NotFoundException;
import br.com.school.product.domain.kafka.product.EventType;
import br.com.school.product.domain.kafka.product.ProductProducer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;
    @Mock
    private ProductProducer producer;

    @Test
    void shouldCreateNewProduct() {
        final var expectedSku = "1";
        final var expectedName = "product name";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create(expectedSku, expectedName, expectedStock,
            expectedCost, expectedPrice);

        final var expectedId = product.getId();
        final var expectedEventType = EventType.CREATE;

        when(repository.save(any())).thenReturn(product);
        doNothing().when(producer).sendProduct(any());

        service.createProduct(product);

        verify(repository, times(1))
            .save(argThat(arg -> Objects.equals(expectedSku, arg.getSku())
                && Objects.equals(expectedName, arg.getName())
                && Objects.equals(expectedStock, arg.getStock())
                && Objects.equals(expectedCost, arg.getCost())
                && Objects.equals(expectedPrice, arg.getPrice())));
        verify(producer, times(1))
            .sendProduct(argThat(arg -> Objects.equals(expectedSku, arg.sku())
                && Objects.equals(expectedName, arg.name())
                && Objects.equals(expectedStock, arg.stock())
                && Objects.equals(expectedId, arg.id())
                && Objects.equals(expectedEventType, arg.eventType())));
    }

    @Test
    void shouldUpdateProduct() {
        final var expectedSku = "1";
        final var expectedName = "product name";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var productFromBd = ProductEntity.create("123", "expectedName",
            BigDecimal.valueOf(100),
            BigDecimal.valueOf(200), BigDecimal.valueOf(300));

        final var expectedId = productFromBd.getId();
        final var expectedEventType = EventType.UPDATE;

        final var product = ProductEntity.with(productFromBd.getId(),
            expectedSku, expectedName, expectedStock, expectedCost, expectedPrice);

        when(repository.save(any())).thenReturn(product);
        doNothing().when(producer).sendProduct(any());

        service.updateProduct(productFromBd, product);

        verify(repository, times(1))
            .save(argThat(arg -> Objects.equals(expectedSku, arg.getSku())
                && Objects.equals(expectedName, arg.getName())
                && Objects.equals(expectedStock, arg.getStock())
                && Objects.equals(expectedCost, arg.getCost())
                && Objects.equals(expectedPrice, arg.getPrice())));

        verify(producer, times(1))
            .sendProduct(argThat(arg -> Objects.equals(expectedSku, arg.sku())
                && Objects.equals(expectedName, arg.name())
                && Objects.equals(expectedStock, arg.stock())
                && Objects.equals(expectedId, arg.id())
                && Objects.equals(expectedEventType, arg.eventType())));
    }

    @Test
    void shouldReturnProductGetById() {
        final var expectedSku = "1";
        final var expectedName = "product name";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create(expectedSku, expectedName, expectedStock,
            expectedCost, expectedPrice);

        when(repository.findById(any()))
            .thenReturn(Optional.of(product));

        final var actualProduct = service.getProductById("1");

        Assertions.assertTrue(
            Objects.equals(expectedSku, actualProduct.getSku())
                && Objects.equals(expectedName, actualProduct.getName())
                && Objects.equals(expectedStock, actualProduct.getStock())
                && Objects.equals(expectedCost, actualProduct.getCost())
                && Objects.equals(expectedPrice, actualProduct.getPrice()));

        verify(repository, times(1))
            .findById("1");
    }

    @Test
    void shouldNotReturnProductGetByIdWhenProductNotFound() {
        final var expectedMessageError = "product not found with id 1 not found";
        when(repository.findById(any()))
            .thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class,
            () -> service.getProductById("1"));

        Assertions.assertEquals(expectedMessageError, exception.getMessage());

        verify(repository, times(1))
            .findById("1");
    }


    @Test
    void shouldDeleteProduct() {
        final var expectedSku = "1";
        final var expectedName = "product name";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create(expectedSku, expectedName, expectedStock,
            expectedCost, expectedPrice);

        final var expectedId = product.getId();
        final var expectedEventType = EventType.DELETE;

        when(repository.findById(any()))
            .thenReturn(Optional.of(product));

        doNothing().when(repository).delete(any());
        doNothing().when(producer).sendProduct(any());

        service.deleteProduct("1");

        verify(repository, times(1)).findById("1");
        verify(repository, times(1)).delete(any());
        verify(producer, times(1))
            .sendProduct(argThat(arg -> Objects.equals(expectedSku, arg.sku())
                && Objects.equals(expectedName, arg.name())
                && Objects.equals(expectedStock, arg.stock())
                && Objects.equals(expectedId, arg.id())
                && Objects.equals(expectedEventType, arg.eventType())));
    }

    @Test
    void shouldNotDeleteProductWhenProductNotFound() {
        final var expectedMessageError = "product not found with id 1 not found";
        when(repository.findById(any()))
            .thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class,
            () -> service.deleteProduct("1"));

        Assertions.assertEquals(expectedMessageError, exception.getMessage());

        verify(repository, times(1)).findById("1");
        verify(repository, never()).delete(any());
    }

    @Test
    void shouldFindAll() {
        final var expectedSku = "1";
        final var expectedName = "product name";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create(expectedSku, expectedName, expectedStock,
            expectedCost, expectedPrice);

        final var page = new PageImpl<ProductEntity>(List.of(product));

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        final var pageReturn = service.findAllProducts(Pageable.ofSize(1));
        Assertions.assertNotNull(pageReturn);
        Assertions.assertEquals(product.getId(), pageReturn.get().toList().get(0).getId());

        verify(repository, times(1)).findAll(any(Pageable.class));
    }
}