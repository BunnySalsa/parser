package adapters;

import static domain.model.Product.ProductId;
import static java.time.OffsetDateTime.now;
import static java.util.Objects.isNull;

import adapters.repositories.ProductRepository;
import adapters.repositories.RepositoriesMapper;
import adapters.repositories.records.ProductRecord;
import domain.model.Product;
import domain.model.ProductBlank;
import domain.ports.ProductPort;
import java.time.Clock;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAdapter implements ProductPort {
  private final ProductRepository repository;
  private final RepositoriesMapper mapper;
  private final Clock clock;

  @Override
  public Product create(@NonNull ProductBlank blank) {
    var record = new ProductRecord();
    record.setId(UUID.randomUUID());
    record.setTitle(blank.getTitle());
    record.setCreated(now(clock));

    repository.insert(record);

    return mapper.map(record);
  }

  @Override
  public Product get(@NonNull ProductId id) {
    var record = repository.findById(id.getValue()).orElse(null);
    if (isNull(record)) {
      return null;
    }

    return mapper.map(record);
  }

  @Override
  public void update(ProductId id, Product product) {
    var record = repository.findById(id.getValue()).orElse(null);
    if (isNull(record)) {
      return;
    }
    var newProductRecord = mapper.map(product);
    newProductRecord.setId(record.getId());
    newProductRecord.setCreated(record.getCreated());

    repository.save(newProductRecord);
  }

  public List<Product> getAll() {
    var records = repository.findAll();
    return mapper.map(records);
  }
}
