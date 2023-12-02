package domain.testing;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan({"domain.testing.fakes", "domain.operations"})
public class FakingConfig {

}
