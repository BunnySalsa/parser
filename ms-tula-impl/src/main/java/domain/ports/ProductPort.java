package domain.ports;

import domain.model.Product;
import domain.model.Product.ProductId;
import domain.model.ProductBlank;
import lombok.NonNull;

public interface ProductPort {
  Product create(@NonNull ProductBlank blank);

  Product get(@NonNull ProductId id);

  void update(@NonNull ProductId id, Product product);
}
