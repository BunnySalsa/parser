package domain.testing;

import static java.util.Objects.isNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TripletMap<Key1, Key2, Value> {
  private final Map<Key1, Map<Key2, Value>> map = new ConcurrentHashMap<>();

  public void put(Key1 key1, Key2 key2, Value value) {
    var innerMap = map.get(key1);
    if (isNull(innerMap)) {
      map.put(key1, new ConcurrentHashMap<>());
      innerMap = map.get(key1);
    }

    innerMap.put(key2, value);
  }

  public Value get(Key1 key1, Key2 key2) {
    var innerMap = map.get(key1);
    if (isNull(innerMap)) {
      return null;
    }

    return innerMap.get(key2);
  }
}
