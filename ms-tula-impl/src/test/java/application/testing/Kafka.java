package application.testing;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class Kafka {

  private static volatile Kafka instance;

  public static Kafka getInstance() {
    Kafka localInstance = instance;
    if (localInstance == null) {
      synchronized (Kafka.class) {
        localInstance = instance;
        if (localInstance == null) {
          instance = localInstance = new Kafka();
        }
      }
    }
    return localInstance;
  }

  @Getter
  private final KafkaContainer container;

  public Kafka() {
    Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
    container = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.1"));
    container.withReuse(true);
    container.withLogConsumer(logConsumer);
    container.start();
  }
}
