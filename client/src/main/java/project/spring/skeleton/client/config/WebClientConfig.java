package project.spring.skeleton.client.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import project.spring.skeleton.client.config.property.HostProperty;
import project.spring.skeleton.client.config.property.SkeletonApiProperty;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Slf4j
@Configuration
public class WebClientConfig {
    public static final String SKELETON_TEST_API_CLIENT = "SKELETON_TEST_API_CLIENT";

    private static final int DEFAULT_CONNECTION_TIMEOUT = 3000;
    private static final int DEFAULT_WRITE_TIMEOUT = 25000;
    private static final int DEFAULT_READ_TIMEOUT = 10000;

    @Bean(SKELETON_TEST_API_CLIENT)
    public WebClient kakaoWorkClient(SkeletonApiProperty property) {
        return getWebClientConfiguration(property.getTestApi());
    }

    private WebClient getWebClientConfiguration(HostProperty hostProperty) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024*1024*50))
                .build();

        exchangeStrategies
                .messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));

        return WebClient.builder()
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient
                                        .create()
                                        .tcpConfiguration(
                                                client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, defaultIfNull(hostProperty.getConnectionTimeout(), DEFAULT_CONNECTION_TIMEOUT))
                                                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(defaultIfNull(hostProperty.getConnectionTimeout(), DEFAULT_READ_TIMEOUT), TimeUnit.MILLISECONDS))
                                                                .addHandlerLast(new WriteTimeoutHandler(defaultIfNull(hostProperty.getConnectionTimeout(), DEFAULT_WRITE_TIMEOUT), TimeUnit.MILLISECONDS))))))
                .exchangeStrategies(exchangeStrategies)
                .filter(ExchangeFilterFunction.ofRequestProcessor(
                        clientRequest -> {
                            log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                            return Mono.just(clientRequest);
                        }
                ))
                .filter(ExchangeFilterFunction.ofResponseProcessor(
                        clientResponse -> {
                            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                            return Mono.just(clientResponse);
                        }
                ))
                .baseUrl(hostProperty.rootUri())
                .build();
    }
}