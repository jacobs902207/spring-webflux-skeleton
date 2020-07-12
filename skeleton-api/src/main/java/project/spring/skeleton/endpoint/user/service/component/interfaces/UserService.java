package project.spring.skeleton.endpoint.user.service.component.interfaces;

import project.spring.skeleton.endpoint.user.spec.resource.UserResource;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResource> findUser(long userId);
}