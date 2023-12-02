package adapters.repositories;

import adapters.repositories.records.ProductRecord;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<ProductRecord, UUID> {
  @Modifying
  @Query(nativeQuery = true, value = """
    INSERT INTO product (
      id,
      title,
      created
    ) VALUES (
      :#{#record.id},
      :#{#record.title},
      :#{#record.created}
    )
    ON CONFLICT DO NOTHING;
      """)
  void insert(@Param("record") ProductRecord record);
}
