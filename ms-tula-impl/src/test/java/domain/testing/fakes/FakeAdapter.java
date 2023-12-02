package domain.testing.fakes;

import static java.util.Objects.isNull;

import domain.model.primitives.Id;
import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class FakeAdapter<K extends Id<?>, T> {
  @Autowired
  protected Clock clock;

  @Autowired
  protected DatabaseRelations relations;

  protected Map<K, T> savedData = new LinkedHashMap<>();

  public T get(@NonNull K id) {
    return savedData.get(id);
  }

  public List<T> selectAll() {
    return savedData.values().stream().toList();
  }

  public Stream<T> streamAll() {
    return savedData.values().stream();
  }

  public void insert(@NonNull T obj) {
    var id = getId(obj);
    if (isNull(id)) {
      id = createId();
    }
    savedData.put(id, obj);
    setId(obj, id);
  }

  public void update(@NonNull T obj) {
    var id = getId(obj);
    if (isNull(id)) {
      throw new NullPointerException();
    }
    savedData.put(id, obj);
  }

  public void insertAll(@NonNull List<T> list) {
    list.forEach(this::insert);
  }

  protected abstract K getId(@NonNull T object);

  protected abstract void setId(@NonNull T object, @NonNull K id);

  protected abstract K createId();

  public void init(Consumer<FakeAdapter<K, T>> consumer) {
    consumer.accept(this);
  }
}
