package application.testing;

import java.sql.Connection;
import java.sql.DriverManager;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.ThreadLocalScopeManager;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@Slf4j
public class Postgres {

  private static volatile Postgres instance;

  public static Postgres getInstance() {
    Postgres localInstance = instance;
    if (localInstance == null) {
      synchronized (Postgres.class) {
        localInstance = instance;
        if (localInstance == null) {
          instance = localInstance = new Postgres();
        }
      }
    }
    return localInstance;
  }

  @Getter
  private final PostgreSQLContainer<?> container;

  @SneakyThrows
  private Postgres() {
    container = new PostgreSQLContainer<>("postgres:15.2");
    Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);

    container.withReuse(true);
    container.withDatabaseName("postgres");
    container.withLogConsumer(logConsumer);
    container.start();

    Scope.setScopeManager(new ThreadLocalScopeManager());
    try (var connection = getConnection()) {
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase(
          "changelog/changelog.xml",
          new ClassLoaderResourceAccessor(),
          database);
      liquibase.update("default");
      liquibase.close();
    }
  }

  @SneakyThrows
  public Connection getConnection() {
    var jdbcUrl  = container.getJdbcUrl();
    var username = container.getUsername();
    var password = container.getPassword();

    DriverManager.registerDriver(new org.postgresql.Driver());
    return DriverManager.getConnection(jdbcUrl, username, password);
  }

}
