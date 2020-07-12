package project.spring.skeleton.endpoint.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import proejct.spring.skeleton.constant.common.GatewayHeader;
import project.spring.skeleton.ApiVersion;
import project.spring.skeleton.endpoint.user.service.component.interfaces.UserService;
import project.spring.skeleton.endpoint.user.spec.UserControllerSpec;
import project.spring.skeleton.endpoint.user.spec.resource.UserResource;
import reactor.core.publisher.Mono;

@RestController
@ApiVersion("v1")
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController implements UserControllerSpec {
    private final UserService userService;

    @GetMapping
    public Mono<UserResource> findUser(@RequestHeader(GatewayHeader.GATEWAY_HEADER_KEY) long userId) {
        return userService.findUser(userId);
    }
}