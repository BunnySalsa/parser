package domain.operations;

import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import adapters.repositories.records.ProductRecord;
import domain.exceptions.ProductNotFoundException;
import domain.model.Product;
import domain.model.Product.ProductBuilder;
import domain.model.Product.ProductId;
import domain.model.ProductBlank;
import domain.ports.ProductPort;
import domain.testing.FakedPortsTest;
import domain.testing.LogLock;
import domain.testing.fakes.FakeClock;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateProductOperationTest extends FakedPortsTest {

  @Autowired
  private UpdateProductOperation operation;

  @Autowired
  private FakeClock clock;

  @Autowired
  private ProductPort productPort;

  @Override
  protected void setupLoggers() {
    watchLoggerForClass(UpdateProductOperation.class);
  }

  @Test
  @LogLock
  void worksFineWhenProductUpdated() {
    var createdProduct = productPort.create(productBlank());
    var id = createdProduct.getId();
    var newTitle = "new title";

    var product = getUpdatedProduct(newTitle, createdProduct);

    operation.execute(id, product);
    var updatedProduct = productPort.get(id);

    assertThat(updatedProduct.getTitle()).isEqualTo(newTitle);

  }

  @Test
  @LogLock
  void shouldThrowIfRecordWithIdIsNotPresented() {
    var id = UUID.randomUUID();
    var product = getProduct(id);

    assertThrows(ProductNotFoundException.class, () -> operation.execute(
        ProductId.of(id),
        product));
  }

  @Test
  @LogLock
  void shouldThrowNpeIfIdIsNotPresented() {
    assertThrows(NullPointerException.class, () -> operation.execute(null,
        getProduct(UUID.randomUUID())));
  }

  @Test
  @LogLock
  void shouldThrowNpeIfProductIsNotPresented() {
    assertThrows(NullPointerException.class, () -> operation.execute(
        ProductId.of(UUID.randomUUID()), null));
  }
  private Product getProduct(UUID id) {
    return Product.builder().
        title("Not existing product")
        .id(ProductId.of(id))
        .created(now(clock))
        .build();
  }

  private Product getUpdatedProduct(String newTitle, Product createdProduct) {
    var product = Product.builder().
        title(newTitle)
        .id(createdProduct.getId())
        .created(createdProduct.getCreated())
        .build();
    return product;
  }

  private ProductBlank productBlank() {
    return ProductBlank.builder()
        .title("Product title")
        .build();
  }
}
