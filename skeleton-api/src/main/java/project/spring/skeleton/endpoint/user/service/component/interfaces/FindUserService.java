package project.spring.skeleton.endpoint.user.service.component.interfaces;

import project.spring.skeleton.data.user.UserDataModel;
import reactor.core.publisher.Mono;

public interface FindUserService {
    Mono<UserDataModel> findUserByUserId(long userId);
}