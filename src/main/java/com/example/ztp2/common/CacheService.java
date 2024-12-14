package com.example.ztp2.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CacheService {

    private static volatile CacheService instance;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final long expirationTimeMillis;
    private final ObjectMapper objectMapper;

    private CacheService(long expirationTime, TimeUnit timeUnit) {
        this.expirationTimeMillis = timeUnit.toMillis(expirationTime);
        this.objectMapper = new ObjectMapper();
    }

    public static CacheService getInstance(long expirationTime, TimeUnit timeUnit) {
        if (instance == null) {
            synchronized (CacheService.class) {
                if (instance == null) {
                    instance = new CacheService(expirationTime, timeUnit);
                }
            }
        }
        return instance;
    }

    public <T> T get(String key, TypeReference<T> typeReference) {
        CacheEntry cacheEntry = cache.get(key);
        if (cacheEntry != null && System.currentTimeMillis() - cacheEntry.timestamp < expirationTimeMillis) {
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(cacheEntry.data), typeReference);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> void put(String key, T data) {
        cache.put(key, new CacheEntry(data, System.currentTimeMillis()));
    }

    public void clear(String key) {
        cache.remove(key);
    }

    private static class CacheEntry {
        private final Object data;
        private final long timestamp;

        public CacheEntry(Object data, long timestamp) {
            this.data = data;
            this.timestamp = timestamp;
        }
    }
}
