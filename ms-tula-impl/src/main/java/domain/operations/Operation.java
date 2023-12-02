package domain.operations;

import domain.ports.TransactionHandler;
import java.time.Clock;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

@Slf4j
public abstract class Operation {

  @Autowired
  private TransactionHandler handler;

  @Autowired
  protected Clock clock;

  protected <T> T transactional(@NonNull Supplier<T> supplier) {
    StopWatch watch = new StopWatch();
    log.trace("Transaction begin");
    watch.start();
    var result = handler.run(supplier);
    watch.stop();
    log.trace("Transaction finished. Elapsed time: {}ms", watch.getTotalTimeMillis());
    return result;
  }

  protected void transactional(@NonNull Runnable runnable) {
    StopWatch watch = new StopWatch();
    log.trace("Transaction begin");
    watch.start();
    handler.run(runnable);
    watch.stop();
    log.trace("Transaction finished. Elapsed time: {}ms", watch.getTotalTimeMillis());
  }

}
