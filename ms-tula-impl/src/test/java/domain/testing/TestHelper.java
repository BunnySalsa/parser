package domain.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import am.ik.yavi.core.ConstraintViolation;
import am.ik.yavi.core.ViolationMessage;
import domain.exceptions.ValidationException;
import java.util.List;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.function.Executable;

public class TestHelper {


  public static Pair<String, String> violation(ConstraintViolation violation) {
    return Pair.of(violation.name(), violation.messageKey());
  }

  public static Pair<String, String> violation(String name, ViolationMessage violationMessage) {
    return Pair.of(name, violationMessage.messageKey());
  }

  @SafeVarargs
  public static void assertViolations(Executable executable, @NonNull Pair<String, String>... expected) {
    var ex = assertThrows(ValidationException.class, executable);
    var violations = ex.getViolations();
    assertThat(violations).isNotNull();

    var actual = violations.stream().map(TestHelper::violation).toList();

    assertThat(actual).containsExactlyInAnyOrderElementsOf(List.of(expected));
  }
}
