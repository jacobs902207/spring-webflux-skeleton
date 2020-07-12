package project.spring.skeleton.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FallbackNotificator<T> {
    public Mono<T> send(Mono<T> object) {
        return object.doOnNext(obj -> {
            if (obj == null) {
                return;
            }

            // *important 레디스 데이터 동기화 알림
            log.error(String.format("레디스에 저장된 데이터가 올바르지 않습니다. 레디스 데이터를 동기화합니다.\n\n%s", obj.toString()));
        });
    }
}