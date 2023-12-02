package adapters.repositories;

import static java.util.Objects.isNull;

import adapters.repositories.records.ProductRecord;
import domain.model.Product;
import domain.model.Product.ProductId;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepositoriesMapper {

  default ProductRecord map(Product product) {
    if (isNull(product)) {
      return null;
    }

    var record = new ProductRecord();
    record.setId(product.getId().getValue());
    record.setTitle(product.getTitle());
    record.setCreated(product.getCreated());
    return record;
  }

  default Product map(ProductRecord record) {
    if (isNull(record)) {
      return null;
    }

    return Product.builder()
        .id(new ProductId(record.getId()))
        .title(record.getTitle())
        .created(record.getCreated())
        .build();
  }

  List<Product> map(Iterable<ProductRecord> records);
}
