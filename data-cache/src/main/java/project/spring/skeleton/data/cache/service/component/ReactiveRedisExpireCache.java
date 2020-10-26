package project.spring.skeleton.data.cache.service.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import proejct.spring.skeleton.common.exception.NotFoundException;
import proejct.spring.skeleton.common.util.EnvironmentHelper;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReactiveRedisExpireCache<V> {
    private final ReactiveRedisTemplate<String, V> redisTemplate;
    private final ObjectMapper objectMapper;
    private final EnvironmentHelper env;

    private static final long EXPIRED_DAYS = 90;

    public Mono<Boolean> hasKey(final String key) {
        return redisTemplate.hasKey(String.format("%s%s", env.getCachePrefix(), key));
    }

    public Mono<Boolean> set(final String key, final V value) {
        log.info("[ReactiveRedisExpireCache#set] key: {}, value: {}", key, value);
        final String redisKey = String.format("%s%s", env.getCachePrefix(), key);

        updateExpireTimeFor(redisKey);
        redisTemplate.opsForValue().set(redisKey, value)
                .subscribe();

        return Mono.just(true);
    }

    public Mono<Boolean> setBeforeExpiredSeconds(final String key, final V value, final int SECONDS) {
        log.info("[ReactiveRedisExpireCache#setBeforeSeconds] key: {}, value: {}", key, value);
        final String redisKey = String.format("%s%s", env.getCachePrefix(), key);

        redisTemplate.opsForValue()
                .set(redisKey, value, Duration.ofSeconds(SECONDS))
                .subscribe();

        return Mono.just(true);
    }

    public Mono<V> getObjectAndNotUpdateExpireTime(final String key, final Class<V> typeClass) {
        final String redisKey = String.format("%s%s", env.getCachePrefix(), key);

        return redisTemplate.hasKey(redisKey)
                .filter(Boolean::booleanValue)
                .flatMap(hasKey -> redisTemplate.opsForValue()
                        .get(redisKey)
                        .map(result -> {
                            try {
                                return objectMapper.readValue(result.toString(), typeClass);
                            } catch (IOException ex) {
                                log.error("[ReactiveRedisExpireCache#getObjectAndNotUpdateExpireTime {}] key:{}, {}", ex.getClass().getSimpleName(), key, ex.getMessage());
                                throw new IllegalStateException("데이터를 요청하는 도중에 문제가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                            }
                        }))
                .switchIfEmpty(Mono.error(new NotFoundException("존재하지 않는 데이터입니다.")));
    }

    public Mono<V> getObject(final String key, final Class<V> typeClass) {
        final String redisKey = String.format("%s%s", env.getCachePrefix(), key);

        return redisTemplate.hasKey(redisKey)
                .filter(Boolean::booleanValue)
                .flatMap(hasKey -> {
                    updateExpireTimeFor(redisKey);
                    return redisTemplate.opsForValue()
                            .get(redisKey)
                            .map(result -> {
                                try {
                                    return objectMapper.readValue(result.toString(), typeClass);
                                } catch (IOException ex) {
                                    log.error("[ReactiveRedisExpireCache#getObjectAndNotUpdateExpireTime {}] key:{}, {}", ex.getClass().getSimpleName(), key, ex.getMessage());
                                    throw new IllegalStateException("데이터를 요청하는 도중에 문제가 발생하였습니다. 잠시 후 다시 시도해주세요.");
                                }
                            });
                })
                .switchIfEmpty(Mono.error(new NotFoundException("존재하지 않는 데이터입니다.")));
    }

    public Mono<Long> increment(final String key, final long value) {
        final String redisKey = String.format("%s%s", env.getCachePrefix(), key);

        log.info("[ReactiveRedisExpireCache#increment] key: {}", redisKey);

        updateExpireTimeFor(redisKey);
        return redisTemplate.opsForValue().increment(redisKey, value);
    }

    public Mono<Double> increment(final String key, final double value) {
        final String redisKey = String.format("%s%s", env.getCachePrefix(), key);

        log.info("[ReactiveRedisExpireCache#increment] key: {}", redisKey);

        updateExpireTimeFor(redisKey);
        return redisTemplate.opsForValue().increment(redisKey, value);
    }

    private void updateExpireTimeFor(final String key) {
        redisTemplate.expire(key, Duration.ofDays(EXPIRED_DAYS))
                .subscribe();
    }
}