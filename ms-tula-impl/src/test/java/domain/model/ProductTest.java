package domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import domain.exceptions.ValidationException;
import domain.model.Product.ProductBuilder;
import domain.model.Product.ProductId;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductTest {

  @Test
  void worksFineWhenProductIdIsNonNull() {
    var doc = product().build();
    assertThat(doc.getId()).isNotNull();
  }

  @Test
  void throwsWhenProductIdIsNull() {
    var builder = product().id(null);
    var thrown = assertThrows(ValidationException.class, builder::build);
    assertThat(thrown.getViolations()).hasSize(1);
    assertThat(thrown.getViolations().get(0).name()).isEqualTo("id");
  }

  @Test
  void worksFineWhenProductTitleIsNonBlank() {
    var doc = product().build();
    assertThat(doc.getTitle()).isNotBlank();
  }

  private static ProductBuilder product() {
    return Product.builder()
        .id(new ProductId(UUID.randomUUID()))
        .title("Product title")
        .created(OffsetDateTime.parse("2023-01-01T01:02:03Z"));
  }
}
