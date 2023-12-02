package domain.model;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.constraint.CharSequenceConstraint;
import am.ik.yavi.constraint.ObjectConstraint;
import am.ik.yavi.core.Validator;
import domain.exceptions.ValidationException;
import domain.model.primitives.Id;
import domain.testing.ExcludeFromJacocoGeneratedReport;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@ToString
public class Product {

  @Setter
  private ProductId id;

  private String title;

  private OffsetDateTime created;

  @ExcludeFromJacocoGeneratedReport
  public static class ProductId extends Id<UUID>  {
    public ProductId(UUID id) {
      super(id);
    }

    public static ProductId of(@NonNull String id) {
      return new ProductId(UUID.fromString(id));
    }

    public static ProductId of(@NonNull UUID id) {
      return new ProductId(id);
    }
  }

  public static class ProductBuilder {
    private ProductBuilder() {}
  }

  public static ProductBuilder builder() {
    return new ProductBuilder() {
      @ExcludeFromJacocoGeneratedReport
      @Override
      public Product build() {
        var object = super.build();
        var violations = VALIDATOR.validate(object);
        if (violations.isValid()) {
          return object;
        }
        throw new ValidationException(object, violations);
      }
    };
  }

  private static final Validator<Product> VALIDATOR = ValidatorBuilder.<Product>of()
      ._object(Product::getId, "id", ObjectConstraint::notNull)
      ._string(Product::getTitle, "title", CharSequenceConstraint::notBlank)
      ._object(Product::getCreated, "created", ObjectConstraint::notNull)
      .build();
}
