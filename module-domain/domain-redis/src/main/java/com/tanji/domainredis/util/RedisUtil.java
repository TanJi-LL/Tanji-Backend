package com.tanji.domainredis.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {
    // RedisTemplate은 Redis와 상호작용하는 다양한 작업을 제공하는 클래스
    // 키는 String으로, 값은 Object 타입으로 저장될 수 있는 키-값 저장소로서 Redis와 작업
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 지정된 만료 시간과 함께 값을 Redis에 저장하는 메서드.
     * 'key'에 해당하는 값 'val'을 지정된 시간 'time' 동안 Redis에 저장.
     * 시간 단위는 'timeUnit'으로 설정.
     * @param key      Redis에 저장할 키.
     * @param val      Redis에 저장할 값.
     * @param time     해당 값이 Redis에 유지될 시간.
     * @param timeUnit 시간의 단위 (초, 분 등).
     */
    public void saveAsValue(String key, Object val, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, val, time, timeUnit);
    }

    /**
     * 만료 시간 없이 값을 Redis에 영구적으로 저장하는 메서드.
     * @param key Redis에 저장할 키.
     * @param val Redis에 저장할 값.
     */
    public void saveAsPermanentValue(String key, Object val) {
        redisTemplate.opsForValue().set(key, val);
    }

    /**
     * 자정을 만료 시간으로 값을 Redis에 저장하는 메서드.
     * @param key Redis에 저장할 키.
     * @param value 저장할 저장할 값.
     */
    public void saveAsMidnightTTL(String key, Object value) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.plusDays(1).toLocalDate().atStartOfDay();
        long seconds = Duration.between(now, midnight).getSeconds();
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * Redis에 해당 키가 존재하는지 확인하는 메서드.
     *
     * @param key Redis에서 존재 여부를 확인할 키.
     * @return 키가 존재하면 true, 없으면 false를 반환.
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }


    /**
     * Redis에서 값을 조회하는 메서드.
     *
     * @param key 조회할 값과 연결된 키.
     * @return 주어진 키에 연결된 값, 또는 키가 존재하지 않을 경우 null.
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public Object getValue(String key) {
        return get(key);
    }


    /**
     * Redis에서 해당 키를 삭제하는 메서드.
     *
     * @param key 삭제할 키.
     * @return 키가 성공적으로 삭제되면 true, 그렇지 않으면 false.
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }


    /**
     * Sorted Set에 저장하는 메서드.
     * @param key  Sorted Set 이름.
     * @param value 저장할 값.
     * @param score 저장할 값의 점수.
     */
    public void saveToZSet(String key, String value, int score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }


    /**
     * Sorted Set에서 해당 값의 순위를 조회하는 메서드. (내림차순)
     * @param key   Sorted Set 이름.
     * @param value 조회할 값.
     * @return      조회된 값의 순위.
     */
    public Long getZSetReverseRank(String key, String value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }


    /**
     * Sorted Set에서 해당 값의 점수를 조회하는 메서드.
     * @param key   Sorted Set 이름.
     * @param value 조회할 값.
     * @return      조회된 값의 점수.
     */
    public Double getZSetScore(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }
}