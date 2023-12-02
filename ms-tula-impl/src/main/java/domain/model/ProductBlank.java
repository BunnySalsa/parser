package domain.model;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.constraint.CharSequenceConstraint;
import am.ik.yavi.core.Validator;
import domain.exceptions.ValidationException;
import domain.testing.ExcludeFromJacocoGeneratedReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@ToString
public class ProductBlank {

  private String title;

  public static class ProductBlankBuilder {
    private ProductBlankBuilder() {}
  }

  public static ProductBlankBuilder builder() {
    return new ProductBlankBuilder() {
      @ExcludeFromJacocoGeneratedReport
      @Override
      public ProductBlank build() {
        var object = super.build();
        var violations = VALIDATOR.validate(object);
        if (violations.isValid()) {
          return object;
        }
        throw new ValidationException(object, violations);
      }
    };
  }

  private static final Validator<ProductBlank> VALIDATOR = ValidatorBuilder.<ProductBlank>of()
      ._string(ProductBlank::getTitle, "title", CharSequenceConstraint::notBlank)
      .build();
}
