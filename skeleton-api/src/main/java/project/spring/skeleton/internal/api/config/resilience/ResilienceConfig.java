package project.spring.skeleton.internal.api.config.resilience;

import com.google.common.collect.Lists;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proejct.spring.skeleton.common.exception.ApiClientException;
import proejct.spring.skeleton.common.exception.NotFoundException;

import java.time.Duration;
import java.util.List;

import static proejct.spring.skeleton.common.constant.common.RetryAttemptConfiguration.DEFAULT_RETRY_COUNT;
import static proejct.spring.skeleton.common.constant.common.RetryAttemptConfiguration.DEFAULT_RETRY_DELAY_TIME;

@Configuration
@RequiredArgsConstructor
public class ResilienceConfig {
    public static final String userPartnerMappingRetry = "userPartnerMappingRetry";
    public static final String redisCircuitBreaker = "redisCircuitBreaker";

    private final ResilienceNotificator resilienceNotificator;

    private final List<String> circuitBreakers = Lists.newArrayList(redisCircuitBreaker);
    private final List<String> retries = Lists.newArrayList(userPartnerMappingRetry);

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(MeterRegistry meterRegistry) {
        CircuitBreakerConfig defaultConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(20)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .permittedNumberOfCallsInHalfOpenState(3)
                .minimumNumberOfCalls(10)
                .failureRateThreshold(50)
                .recordExceptions(ApiClientException.class)
                .ignoreExceptions(NotFoundException.class)
                .waitDurationInOpenState(Duration.ofSeconds(50))
                .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(defaultConfig);
        for (String c : circuitBreakers) {
            registry.circuitBreaker(c)
                    .getEventPublisher()
                    .onSlowCallRateExceeded(resilienceNotificator::notificationWhenSlowCallRateExceeded)
                    .onFailureRateExceeded(resilienceNotificator::notificationWhenFailureRateExceeded)
                    .onStateTransition(resilienceNotificator::notificationWhenStateTransition);
        }

        final TaggedCircuitBreakerMetrics taggedCircuitBreakerMetrics = TaggedCircuitBreakerMetrics
                .ofCircuitBreakerRegistry(registry);

        taggedCircuitBreakerMetrics.bindTo(meterRegistry);

        return registry;
    }

    @Bean
    public RetryRegistry retryRegistry(MeterRegistry meterRegistry) {
        RetryConfig defaultConfig = RetryConfig.custom()
                .maxAttempts(DEFAULT_RETRY_COUNT)
                .waitDuration(Duration.ofMillis(DEFAULT_RETRY_DELAY_TIME))
                .build();

        RetryRegistry registry = RetryRegistry.of(defaultConfig);
        for (String r : retries) {
            registry.retry(r)
                    .getEventPublisher()
                    .onError(resilienceNotificator::onRetryError);
        }

        final TaggedRetryMetrics taggedRetryMetrics = TaggedRetryMetrics
                .ofRetryRegistry(registry);

        taggedRetryMetrics.bindTo(meterRegistry);

        return registry;
    }

    @Bean
    public CircuitBreaker redisCircuitBreaker(CircuitBreakerRegistry registry) {
        CircuitBreakerConfig defaultConfig = CircuitBreakerConfig.custom()
                .slowCallDurationThreshold(Duration.ofMillis(1000))
                .build();

        return registry.circuitBreaker(redisCircuitBreaker, defaultConfig);
    }

    @Bean(userPartnerMappingRetry)
    public Retry userPartnerMappingRetry(RetryRegistry registry) {
        RetryConfig defaultConfig = RetryConfig.custom()
                .maxAttempts(DEFAULT_RETRY_COUNT)
                .waitDuration(Duration.ofMillis(DEFAULT_RETRY_DELAY_TIME))
                .retryExceptions(Exception.class)
                .build();

        return registry.retry(userPartnerMappingRetry, defaultConfig);
    }
}