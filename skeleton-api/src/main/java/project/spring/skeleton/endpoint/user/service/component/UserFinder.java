package project.spring.skeleton.endpoint.user.service.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.stereotype.Component;
import proejct.spring.skeleton.exception.InvalidArgumentsException;
import project.spring.skeleton.common.FallbackNotificator;
import project.spring.skeleton.config.hystrix.HystrixConfig;
import project.spring.skeleton.data.user.UserDataModel;
import project.spring.skeleton.data.user.interfaces.UserDataService;
import project.spring.skeleton.domain.user.UserDataCacheService;
import project.spring.skeleton.endpoint.user.service.component.interfaces.FindUserService;
import project.spring.skeleton.endpoint.user.service.component.validate.PortalAccountValidator;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFinder implements FindUserService {
    private final FallbackNotificator<UserDataModel> fallbackNotificator;
    private final UserDataCacheService userDataCacheService;
    private final UserDataService userDataService;

    @Override
    public Mono<UserDataModel> findUserByUserId(long userId) {
        return HystrixCommands
                .from(userDataCacheService.findOne(userId))
                .fallback(fallbackFindUserByUserId(userId))
                .groupName("find user by userId (@@redis)")
                .commandName("findUserByUserId")
                .commandProperties(HystrixConfig.getConfiguration())
                .toMono()
                .filter(PortalAccountValidator::valid);
    }

    /**
     * use this fallback method when circuit open
     */
    public Mono<UserDataModel> fallbackFindUserByUserId(long userId) {
        final String event = "UserFinder#fallbackFindUserByUserId";
        log.warn("[{}] user_id: {}", event, userId);

        return userDataService.findOne(userId)
                .switchIfEmpty(Mono.error(new InvalidArgumentsException("잘못된 요청입니다. 존재하지 않는 유저입니다.")))
                .filter(PortalAccountValidator::valid)
                .as(fallbackNotificator::send);

                // *important 유저 데이터 레디스 동기화 (look aside)
                // .doOnNext(r -> userCacheEventListener.initialize(new UserDataCacheEvent(event, r)));
    }
}