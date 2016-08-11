package simonxianyu.shiroredis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implement Shiro's @see CacheManager
 * Created by simon on 16/8/1.
 */
public class RedisCacheManager implements CacheManager {
  private JedisPool jedisPool;
  private ConcurrentMap<String, Cache> cacheMap;
  private BeanSerializer<String> beanSerializer;

  public RedisCacheManager() {
    cacheMap = new ConcurrentHashMap<>();
  }

  public <K, V> Cache<K, V> getCache(String name) throws CacheException {
    if (cacheMap.containsKey(name)) {
      return cacheMap.get(name);
    } else {
      RedisCache newOne = new RedisCache(jedisPool, beanSerializer);
      Cache<K,V> cache = cacheMap.putIfAbsent(name, newOne);
      return cache == null ? newOne:cache;
    }
  }

  public JedisPool getJedisPool() {
    return jedisPool;
  }

  public void setJedisPool(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  public BeanSerializer<String> getBeanSerializer() {
    return beanSerializer;
  }

  public void setBeanSerializer(BeanSerializer<String> beanSerializer) {
    this.beanSerializer = beanSerializer;
  }
}
