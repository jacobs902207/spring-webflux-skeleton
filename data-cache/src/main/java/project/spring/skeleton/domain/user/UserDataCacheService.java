package project.spring.skeleton.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.spring.skeleton.data.user.UserDataModel;
import project.spring.skeleton.data.user.interfaces.UserDataService;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDataCacheService implements UserDataService {
//    private final ReactiveRedisExpireCache<UserDataModel> reactiveRedisExpireCache;

    @Override
    public Mono<UserDataModel> findOne(long userId) {
        // return reactiveRedisExpireCache.getObject(userId);
        return Mono.empty();
    }
}