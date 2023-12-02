package application.testing;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

import domain.ports.TransactionHandler;
import java.time.Clock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Execution(CONCURRENT)
@Configurable
public abstract class IntegrationTest {

  protected ApplicationContext context;

  private static final Object object = new Object();

  @Autowired
  protected Clock clock;

  @Autowired
  protected TransactionHandler handler;

  @SneakyThrows
  @BeforeEach
  public void up() {
    synchronized (object) {
      try (var connection = Postgres.getInstance().getConnection()) {
        var database = ContextId.getCurrent();
        try (var statement = connection.prepareStatement(
            "CREATE DATABASE " + database + " WITH TEMPLATE postgres")) {
          statement.executeUpdate();
        }
      }
    }
    context = new AnnotationConfigApplicationContext(IntegrationConfig.class);
    var factory = context.getAutowireCapableBeanFactory();
    factory.autowireBean(this);
  }

  @SneakyThrows
  @AfterEach
  public void down() {
    if (nonNull(context)) {
      SpringApplication.exit(context);
    }

    try (var connection = Postgres.getInstance().getConnection()) {
      var database = ContextId.getCurrent();
      try (var statement = connection.prepareStatement("DROP DATABASE " + database)) {
        statement.executeUpdate();
      }
    }

    ContextId.reset();
  }

}
