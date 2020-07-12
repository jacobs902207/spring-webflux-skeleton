package project.spring.skeleton.endpoint.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.spring.skeleton.endpoint.user.service.component.UserFinder;
import project.spring.skeleton.endpoint.user.service.component.interfaces.UserService;
import project.spring.skeleton.endpoint.user.spec.resource.UserResource;
import reactor.core.publisher.Mono;

import static proejct.spring.skeleton.constant.common.CacheDurationTime.USER_CACHE_DURATION_MILLISECONDS;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserFinder userFinder;

    @Override
    public Mono<UserResource> findUser(long userId) {
        return userFinder.findUserByUserId(userId)
                .map(UserResource::fromDataModel)
                .cache(USER_CACHE_DURATION_MILLISECONDS);
    }
}