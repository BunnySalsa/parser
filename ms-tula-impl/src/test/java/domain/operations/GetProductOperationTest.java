package domain.operations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import domain.model.Product.ProductId;
import domain.model.ProductBlank;
import domain.ports.ProductPort;
import domain.testing.FakedPortsTest;
import domain.testing.fakes.FakeClock;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GetProductOperationTest extends FakedPortsTest {
  @Autowired
  private GetProductOperation operation;

  @Autowired
  private FakeClock clock;

  @Autowired
  private ProductPort productPort;

  @Override
  protected void setupLoggers() {
    watchLoggerForClass(GetProductOperationTest.class);
  }

  @Test
  void worksFineWhenGetsProduct() {
    var blank = productPort.create(productBlank());
    var result = operation.execute(blank.getId());
    assertThat(result).isEqualTo(blank);
  }

  @Test
  void throwsNpeWhenIdIsNull() {
    Assertions.assertThrows(NullPointerException.class,
        () -> operation.execute(null));
  }

  private ProductBlank productBlank() {
    return ProductBlank.builder()
        .title("Product title")
        .build();
  }
}
