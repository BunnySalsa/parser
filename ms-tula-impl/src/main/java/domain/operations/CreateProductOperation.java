package domain.operations;

import domain.model.Product;
import domain.model.ProductBlank;
import domain.ports.ProductPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateProductOperation extends Operation {
  private final ProductPort productPort;

  public Product execute(@NonNull ProductBlank blank) {
    var product = transactional(() ->
        productPort.create(blank)
    );

    log.info("Product created: {}", product);
    return product;
  }

}
