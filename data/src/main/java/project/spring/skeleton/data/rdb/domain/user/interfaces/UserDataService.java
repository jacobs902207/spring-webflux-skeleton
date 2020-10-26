package project.spring.skeleton.data.rdb.domain.user.interfaces;

import project.spring.skeleton.data.rdb.domain.user.UserDataModel;
import reactor.core.publisher.Mono;

public interface UserDataService {
    Mono<UserDataModel> findOne(long userId);
}