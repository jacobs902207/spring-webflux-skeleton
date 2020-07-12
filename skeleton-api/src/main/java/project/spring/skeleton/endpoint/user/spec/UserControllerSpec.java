package project.spring.skeleton.endpoint.user.spec;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import project.spring.skeleton.endpoint.user.spec.resource.UserResource;
import reactor.core.publisher.Mono;

@Api(tags = "유저 API")
public interface UserControllerSpec {
    @ApiOperation(value = "유저 조회")
    Mono<UserResource> findUser(long userId);
}