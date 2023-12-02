package domain.exceptions;

import domain.model.Product.ProductId;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class ProductNotFoundException extends DomainException {
  private final ProductId id;

  public ProductNotFoundException(@NonNull ProductId id) {
    this.id = id;
  }
}
