package adapters.repositories.records;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductRecord {

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;

  @Column(name = "title")
  private String title;

  @Column(name = "created")
  private OffsetDateTime created;
}
