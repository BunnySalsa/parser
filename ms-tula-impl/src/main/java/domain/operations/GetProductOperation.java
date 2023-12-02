package domain.operations;

import domain.model.Product;
import domain.model.Product.ProductId;
import domain.ports.ProductPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductOperation extends Operation {
  private final ProductPort productPort;

  public Product execute(@NonNull ProductId id) {
    return transactional(() -> productPort.get(id));
  }

}
