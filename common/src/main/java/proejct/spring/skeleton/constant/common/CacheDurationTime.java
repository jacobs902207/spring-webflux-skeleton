package proejct.spring.skeleton.constant.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheDurationTime {
    public static final Duration DEFAULT_CACHE_DURATION_MILLISECONDS = Duration.ofMillis(20000);
    public static final Duration USER_CACHE_DURATION_MILLISECONDS = Duration.ofMillis(20000);
}