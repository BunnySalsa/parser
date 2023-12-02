package domain.testing.fakes;

import static java.time.OffsetDateTime.now;

import domain.model.Product;
import domain.model.Product.ProductId;
import domain.model.ProductBlank;
import domain.ports.ProductPort;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FakeProductAdapter extends FakeAdapter<ProductId, Product> implements ProductPort {

  @Override
  public Product create(@NonNull ProductBlank blank) {
    var product = Product.builder()
      .id(ProductId.of(UUID.randomUUID()))
      .title(blank.getTitle())
      .created(now(clock))
      .build();

    savedData.put(product.getId(), product);

    return product;
  }

  @Override
  public void update(ProductId id, Product product) {
    savedData.remove(id);
    savedData.put(id, product);
  }

  @Override
  protected ProductId getId(@NonNull Product product) {
    return product.getId();
  }

  @Override
  protected void setId(@NonNull Product product, @NonNull ProductId id) {
    product.setId(id);
  }

  @Override
  protected ProductId createId() {
    return ProductId.of(UUID.randomUUID());
  }
}
