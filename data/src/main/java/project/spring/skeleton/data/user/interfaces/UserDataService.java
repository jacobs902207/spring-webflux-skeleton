package project.spring.skeleton.data.user.interfaces;

import project.spring.skeleton.data.user.UserDataModel;
import reactor.core.publisher.Mono;

public interface UserDataService {
    Mono<UserDataModel> findOne(long userId);
}