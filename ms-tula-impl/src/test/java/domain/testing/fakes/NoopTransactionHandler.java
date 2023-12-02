package domain.testing.fakes;

import domain.ports.TransactionHandler;
import java.util.function.Supplier;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class NoopTransactionHandler implements TransactionHandler {

  @Override
  public <T> T run(@NonNull Supplier<T> supplier) {
    return supplier.get();
  }

  @Override
  public void run(@NonNull Runnable runnable) {
    runnable.run();
  }
}
