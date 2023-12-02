package application.testing;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ContextId {
  private static final ThreadLocal<String> contextId = new ThreadLocal<>();

  public static String getCurrent() {
    if (isBlank(contextId.get())) {
      contextId.set(randomAlphabetic(10).toLowerCase());
    }
    return contextId.get();
  }

  public static void reset() {
    contextId.remove();
  }
}
