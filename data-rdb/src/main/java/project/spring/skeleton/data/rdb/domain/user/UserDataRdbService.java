package project.spring.skeleton.data.rdb.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import project.spring.skeleton.data.rdb.domain.user.interfaces.UserDataService;
import project.spring.skeleton.data.rdb.domain.user.repository.UserRepository;
import reactor.core.publisher.Mono;

@Primary
@Component
@RequiredArgsConstructor
public class UserDataRdbService implements UserDataService {
    private final UserRepository userRepository;

    @Override
    public Mono<UserDataModel> findOne(long userId) {
        return null;
    }
}