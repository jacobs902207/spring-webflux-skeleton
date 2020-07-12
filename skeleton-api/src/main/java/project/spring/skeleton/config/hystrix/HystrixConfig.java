package project.spring.skeleton.config.hystrix;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.netflix.hystrix.HystrixEventType.SHORT_CIRCUITED;
import static com.netflix.hystrix.HystrixEventType.SUCCESS;

@Slf4j
@Configuration
@EnableHystrix
@EnableCircuitBreaker
public class HystrixConfig {
    private final AtomicBoolean isOpen = new AtomicBoolean(false);

    @Bean
    public HystrixPlugins hystrixEventNotifier() {
        HystrixPlugins hystrixPlugins = HystrixPlugins.getInstance();
        hystrixPlugins.registerEventNotifier(new HystrixEventNotifier() {

            @Override
            public void markEvent(HystrixEventType eventType, HystrixCommandKey key) {
                super.markEvent(eventType, key);

                if (isShortCircuitOpen(eventType)) {
                    log.error("CIRCUIT BREAKER OPEN", String.format("%s: %s", key.toString(), eventType.toString()));
                }

                if (isCircuitClose(eventType)) {
                    log.info("CIRCUIT BREAKER BACK TO NORMAL", String.format("%s: %s", key.toString(), eventType.toString()));
                }
            }
        });

        return hystrixPlugins;
    }

    private boolean isShortCircuitOpen(HystrixEventType eventType) {
        return SHORT_CIRCUITED == eventType && isOpen.compareAndSet(false, true);
    }

    private boolean isCircuitClose(HystrixEventType eventType) {
        return SUCCESS == eventType && isOpen.compareAndSet(true, false);
    }

    public static HystrixCommandProperties.Setter getConfiguration() {
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();

        // @todo. core size 늘리기
        commandProperties.withFallbackEnabled(true);
        commandProperties.withRequestLogEnabled(false);
        commandProperties.withCircuitBreakerEnabled(true);
        commandProperties.withCircuitBreakerRequestVolumeThreshold(25);
        commandProperties.withCircuitBreakerSleepWindowInMilliseconds(20000);
        commandProperties.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
        commandProperties.withExecutionTimeoutInMilliseconds(10000);
        commandProperties.withMetricsRollingStatisticalWindowInMilliseconds(180000);

        return commandProperties;
    }
}