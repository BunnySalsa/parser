package domain.testing;

import static java.lang.Thread.currentThread;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import domain.testing.fakes.FakeClock;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Execution;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Execution(CONCURRENT)
@Configurable
public abstract class FakedPortsTest {

  @Autowired
  protected FakeClock clock;

  protected AnnotationConfigApplicationContext context;

  protected final ListAppender<ILoggingEvent> watcher = new ListAppender<>();

  public FakedPortsTest() {
    context = new AnnotationConfigApplicationContext(FakingConfig.class);
    var factory = context.getAutowireCapableBeanFactory();
    factory.autowireBean(this);
  }

  protected String lastLogMessage() {
    var thread = currentThread();

    var list = watcher.list.stream()
      .filter(event -> event.getThreadName().equals(thread.getName()))
      .map(ILoggingEvent::getFormattedMessage)
    .toList();

    if (list.isEmpty()) {
      return null;
    }

    var items = new ArrayList<>(list);
    Collections.reverse(items);

    return items.get(0);
  }

  protected String getLogMessage(int index) {
    var thread = currentThread();

    var list = watcher.list.stream()
    .filter(event -> event.getThreadName().equals(thread.getName()))
    .map(ILoggingEvent::getFormattedMessage)
    .toList();

    if (list.isEmpty()) {
      return null;
    }

    var items = new ArrayList<>(list);
    Collections.reverse(items);

    return items.get(index);
  }

  protected void setupLoggers() {
    // Do nothing
  }

  protected void watchLoggerForClass(Class<?> clazz) {
    var logger = (Logger) LoggerFactory.getLogger(clazz);
    logger.addAppender(watcher);
  }

  @BeforeEach
  public void setupLogWatcher() {
    setupLoggers();
    watcher.start();
  }

}
