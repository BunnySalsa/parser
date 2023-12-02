package infrastructure.config;

import java.time.Clock;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConfigurationPropertiesScan
@EnableJpaRepositories(basePackages = "adapters")
@EntityScan(basePackages = "adapters.repositories.records")
@EnableFeignClients(basePackages = "adapters")
public class AppConfig {

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
