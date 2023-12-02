package domain.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.exceptions.ValidationException;
import domain.model.ProductBlank.ProductBlankBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductBlankTest {
  @Test
  void worksFineWhenTitleIsNotNull() {
    var productBlank = productBlank().build();
    assertThat(productBlank.getTitle()).isNotNull();
  }

  @Test
  void throwsWhenTitleIsNull() {
    var builder = productBlank().title(null);
    var thrown = assertThrows(ValidationException.class, builder::build);
    Assertions.assertThat(thrown.getViolations()).hasSize(1);
    Assertions.assertThat(thrown.getViolations().get(0).name()).isEqualTo("title");
  }

  private ProductBlankBuilder productBlank() {
    return ProductBlank.builder()
        .title("Product title");
  }
}
