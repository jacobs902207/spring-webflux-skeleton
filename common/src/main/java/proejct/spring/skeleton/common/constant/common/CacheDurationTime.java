package proejct.spring.skeleton.common.constant.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheDurationTime {
    public static final Duration DEFAULT_CACHE_DURATION_MILLISECONDS = Duration.ofMillis(20000);
}