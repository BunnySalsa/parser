package domain.model.primitives;

import domain.testing.ExcludeFromJacocoGeneratedReport;
import lombok.Getter;
import lombok.NonNull;

@ExcludeFromJacocoGeneratedReport
public abstract class Id<T> {
  @Getter
  @NonNull
  protected T value;

  protected Id(@NonNull T id) {
    this.value = id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    return this.value.equals(((Id<?>) obj).value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
