package com.moyada.reservoir.manager;

import com.moyada.reservoir.utils.JsonUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Component
public class CacheService {

    private static final long CACHE_TIME = TimeUnit.HOURS.toMillis(12L);

    @Autowired
    protected RedissonClient redissonClient;

    public boolean exist(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.isExists();
    }

    public long ttl(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.remainTimeToLive();
    }

    public <T> boolean set(String key, T data) {
        String json = JsonUtil.toJson(data);
        if (json == null) {
            return false;
        }
        RBucket<String> bucket = redissonClient.getBucket(key, ByteArrayCodec.INSTANCE);
        bucket.setAsync(json, CACHE_TIME, TimeUnit.MILLISECONDS);
        return true;
    }

    public <T> boolean set(String key, T data, long expireTime) {
        String json = JsonUtil.toJson(data);
        if (json == null) {
            return false;
        }
        RBucket<String> bucket = redissonClient.getBucket(key, ByteArrayCodec.INSTANCE);
        bucket.setAsync(json, expireTime, TimeUnit.MILLISECONDS);
        return true;
    }

    public <T> boolean setList(String key, List<T> data) {
        String json = JsonUtil.toJson(data);
        if (json == null) {
            return false;
        }
        RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        bucket.setAsync(json, CACHE_TIME, TimeUnit.MILLISECONDS);
        return true;
    }

    public <T> boolean setList(String key, List<T> data, long expireTime) {
        String json = JsonUtil.toJson(data);
        if (json == null) {
            return false;
        }
        RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        bucket.setAsync(json, expireTime, TimeUnit.MILLISECONDS);
        return true;
    }

    public <T> T get(String key, Class<T> type) {
        RBucket<String> bucket = redissonClient.getBucket(key, ByteArrayCodec.INSTANCE);
        String data = bucket.get();
        if (null == data) {
            return null;
        }

        return JsonUtil.toObject(data, type);
    }

    public <T> List<T> getList(String key, Class<T> type) {
        RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        String data = bucket.get();
        if (null == data) {
            return null;
        }

        return JsonUtil.toList(data, type);
    }

    public boolean del(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (!bucket.isExists()) {
            return false;
        }
        bucket.setAsync(null);
        return true;
    }

    public int size(String key) {
        Collection<String> bucket = redissonClient.getList(key, StringCodec.INSTANCE);
        return bucket.size();
    }

    public <T> boolean add(String key, T data, long expireTime) {
        String value = JsonUtil.toJson(data);
        if (value == null) {
            return false;
        }
        RList<String> bucket = redissonClient.getList(key, StringCodec.INSTANCE);
        long time = expireTime - bucket.remainTimeToLive();
        bucket.expire(time, TimeUnit.MILLISECONDS);
        return bucket.add(value);
    }

    public <T> T getFirst(String key, Class<T> type) {
        RList<String> bucket = redissonClient.getList(key, StringCodec.INSTANCE);
        if (bucket.isEmpty()) {
            return null;
        }
        String data = bucket.remove(0);
        if (data == null) {
            return null;
        }
        return JsonUtil.toObject(data, type);
    }
}
