package domain.operations;


import static java.time.ZonedDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;

import domain.model.ProductBlank;
import domain.testing.FakedPortsTest;
import domain.testing.LogLock;
import domain.testing.fakes.FakeClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateProductOperationTest extends FakedPortsTest {
  @Autowired
  private CreateProductOperation operation;

  @Autowired
  private FakeClock clock;

  @Override
  protected void setupLoggers() {
    watchLoggerForClass(CreateProductOperation.class);
  }

  @Test
  @LogLock
  void worksFineWhenNewProductCreated() {
    var moment = parse("2023-01-01T00:00:00Z");
    clock.setFixed(moment);

    var blank = productBlank();

    var product = operation.execute(blank);
    assertThat(product.getTitle()).isEqualTo(blank.getTitle());
    assertThat(product.getCreated()).isEqualTo(moment.toOffsetDateTime());

    var message = lastLogMessage();
    assertThat(message).contains("Product created");
  }

  @Test
  void shouldThrowNpeWhenBlankIsNull() {
    Assertions.assertThrows(NullPointerException.class, () -> operation.execute(null));
  }

  private ProductBlank productBlank() {
    return ProductBlank.builder()
        .title("Product title")
        .build();
  }
}