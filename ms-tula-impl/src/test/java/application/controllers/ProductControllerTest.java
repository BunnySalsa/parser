package application.controllers;

import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import adapters.ProductAdapter;
import application.testing.IntegrationTest;
import team.microchad.api.dto.ProductBlankDto;
import team.microchad.api.dto.ProductDto;
import domain.model.Product;
import domain.model.ProductBlank;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class ProductControllerTest extends IntegrationTest {

  @Autowired
  private ProductController controller;

  @Autowired
  private ProductAdapter productAdapter;

  @Test
  void worksFineWhenNewProductCreated() {
    var dto = new ProductBlankDto();
    dto.setTitle("Some title");

    var response = controller.createProduct(dto);
    assertThat(response.getStatusCode()).isEqualTo(OK);

    var list = productAdapter.getAll();
    assertThat(list).hasSize(1);

    var product = list.get(0);
    assertThat(product.getTitle()).isEqualTo("Some title");
  }

  @Test
  void workFineWhenGettingProduct() {
    AtomicReference<Product> createdProduct = new AtomicReference<>();
    handler.run(() -> {
      createdProduct.set(productAdapter.create(ProductBlank.builder()
          .title("Some title")
          .build()));
    });
    var response = controller.getProductById(createdProduct.get().getId().getValue());
    assertThat(response.getStatusCode()).isEqualTo(OK);
    var expected = createdProduct.get();
    var presented = response.getBody().getData();
    assertAll(
        () -> assertThat(expected.getId().getValue()).isEqualTo(presented.getId()),
        () -> assertThat(expected.getTitle()).isEqualTo(presented.getTitle()),
        () -> assertThat(expected.getCreated()).isCloseTo(presented.getCreated(), within(100,
            ChronoUnit.MILLIS))
    );
  }

  @Test
  void workFineWhenUpdatingProduct() {
    AtomicReference<Product> createdProduct = new AtomicReference<>();
    handler.run(() -> {
      createdProduct.set(productAdapter.create(ProductBlank.builder()
          .title("Some title")
          .build()));
    });
    var newTitle = "Some new title";
    var productDto = new ProductDto();
    productDto.setCreated(now(clock));
    productDto.setTitle(newTitle);
    productDto.setId(UUID.randomUUID());

    var response = controller.updateProductById(createdProduct.get().getId().getValue(), productDto);
    assertThat(response.getStatusCode()).isEqualTo(OK);

    var updatedProduct = productAdapter.get(createdProduct.get().getId());
    var expected = createdProduct.get();

    assertAll(
        () -> assertThat(expected.getId()).isEqualTo(updatedProduct.getId()),
        () -> assertThat(newTitle).isEqualTo(updatedProduct.getTitle()),
        () -> assertThat(expected.getCreated()).isCloseTo(updatedProduct.getCreated(), within(100,
            ChronoUnit.MILLIS)));
  }


}
