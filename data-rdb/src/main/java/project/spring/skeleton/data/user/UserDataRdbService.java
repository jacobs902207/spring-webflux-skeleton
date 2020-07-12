package project.spring.skeleton.data.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import project.spring.skeleton.data.user.interfaces.UserDataService;
import reactor.core.publisher.Mono;

@Primary
@Component
@RequiredArgsConstructor
public class UserDataRdbService implements UserDataService {
    @Override
    public Mono<UserDataModel> findOne(long userId) {
        return null;
    }
}