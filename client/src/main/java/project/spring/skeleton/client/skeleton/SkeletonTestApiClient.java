package project.spring.skeleton.client.skeleton;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import proejct.spring.skeleton.common.constant.common.GatewayHeader;
import proejct.spring.skeleton.common.exception.ApiClientException;
import project.spring.skeleton.internal.api.common.BaseErrorResponse;
import project.spring.skeleton.client.skeleton.spec.SkeletonTestApiSpec;
import reactor.core.publisher.Mono;

import static proejct.spring.skeleton.common.constant.common.CacheDurationTime.DEFAULT_CACHE_DURATION_MILLISECONDS;
import static project.spring.skeleton.client.config.WebClientConfig.SKELETON_TEST_API_CLIENT;

@Slf4j
@Component
public class SkeletonTestApiClient implements SkeletonTestApiSpec {
    private static final String TEST_REQUEST_PATH = "/test/{id}";

    private final WebClient webClient;

    public SkeletonTestApiClient(@Qualifier(SKELETON_TEST_API_CLIENT) WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Void> testApi(long id) {
        final String event = "SkeletonTestApiClient#testApi";
        log.info("{}, request_path: {}, parameters: {}", event, TEST_REQUEST_PATH, id);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(TEST_REQUEST_PATH)
                        .build(id))
                .headers(httpHeaders -> httpHeaders.set(GatewayHeader.USER_AGENT_HEADER_KEY, "test"))
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                        .bodyToMono(BaseErrorResponse.class)
                        .map(BaseErrorResponse::getErrorCode)
                        .map(ApiClientException::new))
                .bodyToMono(Object.class)
                .cache(DEFAULT_CACHE_DURATION_MILLISECONDS)
                .onErrorResume(ex -> {
                    log.warn("[{} {}] request_path: {}, parameters: {}, {}",
                            event, ex.getClass().getSimpleName(), TEST_REQUEST_PATH, id, ex.getMessage(), ex);

                    return Mono.error(ex);
                })
                .then();
    }
}