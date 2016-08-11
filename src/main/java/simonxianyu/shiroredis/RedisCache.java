package simonxianyu.shiroredis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collection;
import java.util.Set;

/**
 * Implement shiro's cache interface
 * Created by simon on 16/8/1.
 */
public class RedisCache<K,V> implements Cache<K, V> {
  private JedisPool jedisPool;
  private BeanSerializer<String> beanSerializer;

  public RedisCache(JedisPool jedisPool, BeanSerializer<String> beanSerializer) {
    this.jedisPool = jedisPool;
    this.beanSerializer = beanSerializer;
  }


  @Override
  public V get(K key) throws CacheException {
    if (key == null) {
      return null;
    }
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      String v = jedis.get(key.toString());
      if (null != v) {
        return beanSerializer.deserialize(v);
      }
    } finally {
      if (jedis!=null) {
        jedis.close();
      }
    }
    return null;
  }

  @Override
  public V put(K key, V value) throws CacheException {
    if (key == null) {
      throw new IllegalArgumentException("key should not be null");
    }
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      String s = beanSerializer.serialize(value);
      jedis.set(key.toString(), s);
    } finally {
      if (jedis!=null) {
        jedis.close();
      }
    }
    return value;
  }

  @Override
  public V remove(K key) throws CacheException {
    if (key == null) {
      throw new IllegalArgumentException("key should not be null");
    }
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      String sv = jedis.get(key.toString());
      jedis.del(key.toString());
      if (null != sv) {
        return beanSerializer.deserialize(sv);
      }
    } finally {
      if (jedis!=null) {
        jedis.close();
      }
    }
    return null;
  }

  @Override
  public void clear() throws CacheException {

  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public Set<K> keys() {
    return null;
  }

  @Override
  public Collection<V> values() {
    return null;
  }
}
