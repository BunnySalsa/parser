package domain.operations;

import domain.exceptions.ProductNotFoundException;
import domain.model.Product;
import domain.model.Product.ProductId;
import domain.ports.ProductPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateProductOperation extends Operation {
  private final ProductPort productPort;

  public void execute(@NonNull ProductId id, @NonNull Product newProduct) {
    transactional(() -> {
      var product = productPort.get(id);
      if (product == null) {
        throw new ProductNotFoundException(id);
      }
      productPort.update(id, newProduct);
    });

    log.debug("Product updated: {}", newProduct.getId());
  }
}
