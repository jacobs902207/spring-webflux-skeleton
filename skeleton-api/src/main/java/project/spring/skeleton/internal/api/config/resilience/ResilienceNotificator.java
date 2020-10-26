package project.spring.skeleton.internal.api.config.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnFailureRateExceededEvent;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnSlowCallRateExceededEvent;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import io.github.resilience4j.retry.event.RetryOnErrorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import java.util.LinkedHashMap;
import java.util.Map;

import static proejct.spring.skeleton.common.util.JacksonConfig.DEFAULT_DATE_TIME_FORMATTER;
import static project.spring.skeleton.internal.api.config.resilience.ResilienceConfig.redisCircuitBreaker;
import static project.spring.skeleton.internal.api.config.resilience.ResilienceConfig.userPartnerMappingRetry;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ResilienceNotificator {
    private static final Map<CircuitBreaker.State, String> CIRCUIT_BREAKER_STATE_MAP = new LinkedHashMap();
    static {
        CIRCUIT_BREAKER_STATE_MAP.put(CircuitBreaker.State.CLOSED, "서비스의 요청이 정상화되었습니다.\n");
        CIRCUIT_BREAKER_STATE_MAP.put(CircuitBreaker.State.HALF_OPEN, "서비스의 요청 써킷이 HALF_OPEN 되었습니다.\n");
        CIRCUIT_BREAKER_STATE_MAP.put(CircuitBreaker.State.OPEN, "서비스의 요청이 지속적으로 실패되었습니다. 확인이 필요합니다.\n");
    }

    private static final Map<String, String> RESILIENCE_CONFIG_NAME_MAP = new LinkedHashMap();
    static {
        RESILIENCE_CONFIG_NAME_MAP.put(userPartnerMappingRetry, "재시도 프로세스");
        RESILIENCE_CONFIG_NAME_MAP.put(redisCircuitBreaker, "레디스 시스템");
    }

    @Async
    public void notificationWhenStateTransition(CircuitBreakerOnStateTransitionEvent circuitBreakerOnStateTransitionEvent) {
        final CircuitBreaker.State state = circuitBreakerOnStateTransitionEvent.getStateTransition().getToState();
        if (!CIRCUIT_BREAKER_STATE_MAP.containsKey(state)) {
            return;
        }

        final String circuitBreakerName = circuitBreakerOnStateTransitionEvent.getCircuitBreakerName();
        final String message = String.format("[%s] changed %s, creation time %s",
                RESILIENCE_CONFIG_NAME_MAP.get(circuitBreakerName),
                circuitBreakerOnStateTransitionEvent.getStateTransition(),
                circuitBreakerOnStateTransitionEvent.getCreationTime().format(DEFAULT_DATE_TIME_FORMATTER));

        if (CircuitBreaker.State.CLOSED.equals(state)) {
            log.error("{} {}", CIRCUIT_BREAKER_STATE_MAP.get(state), message);
            return;
        }

        log.error("{} {}", CIRCUIT_BREAKER_STATE_MAP.get(state), message);
    }

    @Async
    public void notificationWhenSlowCallRateExceeded(CircuitBreakerOnSlowCallRateExceededEvent circuitBreakerOnSlowCallRateExceededEvent) {
        // @todo.
    }

    @Async
    public void notificationWhenFailureRateExceeded(CircuitBreakerOnFailureRateExceededEvent circuitBreakerOnFailureRateExceededEvent) {
        // @todo.
    }

    @Async
    public void onRetryError(RetryOnErrorEvent retryOnErrorEvent) {
        final String retryName = retryOnErrorEvent.getName();
        final String message = String.format("[%s] retry event has %s, number_of_attempts: %s, creation time %s",
                RESILIENCE_CONFIG_NAME_MAP.get(retryName),
                retryOnErrorEvent.getLastThrowable(),
                retryOnErrorEvent.getNumberOfRetryAttempts(),
                retryOnErrorEvent.getCreationTime().format(DEFAULT_DATE_TIME_FORMATTER));

        log.error("{} {}", CIRCUIT_BREAKER_STATE_MAP.get(CircuitBreaker.State.OPEN), message);
    }
}