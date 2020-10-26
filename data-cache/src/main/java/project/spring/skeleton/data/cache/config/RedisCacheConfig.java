package project.spring.skeleton.data.cache.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;

import static proejct.spring.skeleton.common.util.JacksonConfig.OBJECT_MAPPER_NAME;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Value("${spring.redis.host}")
    public String REDIS_HOST;

    @Value("${spring.redis.port}")
    public Integer REDIS_PORT;

    @Value("${spring.redis.password}")
    public String REDIS_PASSWORD;

    @Value("${spring.redis.timeout}")
    public Integer REDIS_TIMEOUT;

    @Value("${spring.redis.lettuce.pool.max-active}")
    public Integer REDIS_MAX_TOTAL;

    @Value("${spring.redis.lettuce.pool.max-idle}")
    public Integer REDIS_MAX_IDLE;

    @Value("${spring.redis.lettuce.pool.max-wait}")
    public Integer REDIS_MAX_WAIT_MILLIS;

    @Value("${spring.redis.lettuce.pool.min-idle}")
    public Integer REDIS_MIN_IDLE;

    private final ObjectMapper objectMapper;

    public RedisCacheConfig(@Qualifier(OBJECT_MAPPER_NAME) ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Primary
    @Bean("ReactiveRedisConnectionFactory")
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory(redisStandaloneConfiguration(), poolConfig());
    }

    @Bean
    @Primary
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(@Qualifier("ReactiveRedisConnectionFactory") ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
                .<String, String>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(new Jackson2JsonRedisSerializer<>(String.class))
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, HashMap<Long, String>> reactiveRedisHashMapTemplate(@Qualifier("ReactiveRedisConnectionFactory") ReactiveRedisConnectionFactory connectionFactory) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(HashMap.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        RedisSerializationContext<String, HashMap<Long, String>> serializationContext = RedisSerializationContext
                .<String, HashMap<Long, String>>newSerializationContext(new GenericJackson2JsonRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(jackson2JsonRedisSerializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    private LettuceClientConfiguration poolConfig() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        poolConfig.setMaxIdle(REDIS_MAX_IDLE);
        poolConfig.setMinIdle(REDIS_MIN_IDLE);
        poolConfig.setMaxTotal(REDIS_MAX_TOTAL);

        return LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .commandTimeout(Duration.ofMillis(REDIS_TIMEOUT))
                .build();
    }

    private RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(REDIS_PASSWORD));

        return redisStandaloneConfiguration;
    }
}