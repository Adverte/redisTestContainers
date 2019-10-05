package com;

import java.util.Optional;

/**
 * @author Oleg Ushakov. 2019
 */
public interface Cache {
  void put(String key, Object value);

  <T> Optional<T> get(String key, Class<T> expectedClass);
}
