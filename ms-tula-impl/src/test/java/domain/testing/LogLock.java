package domain.testing;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.parallel.ResourceLock;

@Documented
@Retention(RUNTIME)
@Target({METHOD})
@ResourceLock(value = "resources")
public @interface LogLock {
}
