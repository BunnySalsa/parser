package domain.testing.fakes;

import static java.time.OffsetDateTime.now;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;

@Service
public class FakeClock extends Clock {
  private final Clock baseClock = Clock.systemDefaultZone();

  private Clock currentClock = Clock.systemDefaultZone();

  @Override
  public ZoneId getZone() {
    return currentClock.getZone();
  }

  @Override
  public Clock withZone(ZoneId zone) {
    return currentClock.withZone(zone);
  }

  @Override
  public Instant instant() {
    return currentClock.instant();
  }

  public void set(LocalDateTime moment) {
    var zone = ZoneId.systemDefault();
    var duration = Duration.between(now(baseClock), ZonedDateTime.of(moment, zone));

    currentClock = Clock.offset(baseClock, duration);
  }

  public void set(LocalDateTime moment, ZoneId zone) {
    var duration = Duration.between(now(baseClock), ZonedDateTime.of(moment, zone));

    currentClock = Clock.offset(baseClock, duration);
  }

  public void set(ZonedDateTime moment) {
    var duration = Duration.between(now(baseClock), moment);

    currentClock = Clock.offset(baseClock, duration);
  }

  public void set(LocalDate moment) {
    var duration = Duration.between(now(baseClock), moment.atTime(0, 0).atZone(ZoneId.systemDefault()));

    currentClock = Clock.offset(baseClock, duration);
  }

  public void set(int year, int month, int day) {
    var duration = Duration.between(now(baseClock), LocalDate.of(year, month, day).atTime(0, 0).atZone(ZoneId.systemDefault()));

    currentClock = Clock.offset(baseClock, duration);
  }

  public void setDefault() {
    currentClock = baseClock;
  }

  public void setFixed(ZonedDateTime moment) {
    currentClock = Clock.fixed(moment.toInstant(), moment.getZone());
  }
}
