package project.spring.skeleton.client.skeleton.spec;

import reactor.core.publisher.Mono;

public interface SkeletonTestApiSpec {
    Mono<Void> testApi(long id);
}