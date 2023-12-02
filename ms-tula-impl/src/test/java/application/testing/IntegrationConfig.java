package application.testing;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import com.zaxxer.hikari.HikariDataSource;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@TestConfiguration
@ComponentScan(basePackages = {"application", "domain.model", "domain.operations", "adapters"})
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
@EnableJpaRepositories(basePackages = "adapters.repositories")
@EntityScan(basePackages = "adapters.repositories.records")
@EnableFeignClients(basePackages = "adapters")
public class IntegrationConfig {

  @Bean
  public static PropertySourcesPlaceholderConfigurer properties() {
    var properties = new Properties();

    properties.setProperty("app.kafka.producer.topic", "local.sample.events." + ContextId.getCurrent());

    var configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setProperties(properties);
    return configurer;
  }

  @Bean
  @Primary
  public DataSource dataSource() {
    var postgres = Postgres.getInstance().getContainer();

    var host = postgres.getHost();
    var port = postgres.getFirstMappedPort();
    var database = ContextId.getCurrent();
    var username = postgres.getUsername();
    var password = postgres.getPassword();

    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + database);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setMaximumPoolSize(1);
    return dataSource;
  }

  @Bean
  public ProducerFactory<String, String> producerFactory() {
    var kafka = Kafka.getInstance().getContainer();
    var properties = new HashMap<String, Object>();
    properties.put(BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
    properties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(CLIENT_ID_CONFIG, "client-id-" + ContextId.getCurrent());
    return new DefaultKafkaProducerFactory<>(properties);
  }

  private static final Object object = new Object();

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> factory) {
    synchronized (object) {
      createTopics();
    }
    return new KafkaTemplate<>(factory);
  }

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    var kafka = Kafka.getInstance().getContainer();
    Map<String, Object> props = new HashMap<>();
    props.put(BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(GROUP_ID_CONFIG, "group-id-" + ContextId.getCurrent());
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
      ConsumerFactory<String, String> consumerFactory) {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
    factory.setConsumerFactory(consumerFactory);
    return factory;
  }

  private void createTopics() {
    var kafka = Kafka.getInstance().getContainer();
    var kafkaProperties = new HashMap<String, Object>();
    kafkaProperties.put(BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
    try (var client = AdminClient.create(kafkaProperties)) {
      var topics = new ArrayList<NewTopic>();
      topics.add(new NewTopic("local.sample.events." + ContextId.getCurrent(), 1, (short) 1));
      client.createTopics(topics);
    }
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
