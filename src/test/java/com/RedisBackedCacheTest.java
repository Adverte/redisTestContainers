package com;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import redis.clients.jedis.Jedis;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.rnorth.visibleassertions.VisibleAssertions.assertEquals;

/**
 * @author Oleg Ushakov. 2019
 */
public class RedisBackedCacheTest {
  private Cache cache;
  @Rule
  public GenericContainer redis = new GenericContainer("redis:3.0.6").withExposedPorts(6379);

  @Before
  public void setUp() {
    // jedis will be a Redis client we inject, but we don't have one to provide yet...
    Jedis jedis = new Jedis(redis.getContainerIpAddress(), redis.getMappedPort(6379));
    System.out.println("jedis" + jedis);
    cache = new RedisBackedCache(jedis, "test");
  }

  @Test
  public void testFindingAnInsertedValue() {
    cache.put("foo", "FOO");
    Optional<String> foundObject = cache.get("foo", String.class);

    assertTrue("When an object in the cache is retrieved, it can be found", foundObject.isPresent());
    assertEquals("When we put a String in to the cache and retrieve it, the value is the same", "FOO", foundObject.get());
  }

  @Test
  public void testNotFindingAValueThatWasNotInserted() {
    Optional<String> foundObject = cache.get("bar", String.class);

    assertFalse("When an object that's not in the cache is retrieved, nothing is found", foundObject.isPresent());
  }
}