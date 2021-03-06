package project.spring.skeleton.internal.api.common.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import project.spring.skeleton.internal.api.common.logger.JsonLogger;
import project.spring.skeleton.internal.api.common.logger.JsonTemplate;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebLoggingConfig implements WebFilter {
    private String hostName;

    private final Integer port;
    private final String phase;

    public WebLoggingConfig(@Value("${server.port:8080}") Integer port, @Value("${spring.profiles.active}") String phase) {
        this.phase = phase;
        this.port = port;
    }

    @PostConstruct
    public void init() {
        try {
            hostName = InetAddress.getLocalHost()
                    .getHostName();

        } catch (Exception ex) {
            hostName = "unknown";
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Long startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();

        JsonTemplate template = new JsonTemplate();
        String path = request.getURI().getPath();

        template.setRequestTime(LocalDateTime.now().toString());
        template.setHostname(hostName);
        template.setPort(port);
        template.setMethod(request.getMethodValue());
        template.setUrl(path);
        template.setPhase(phase);
        template.setRequestParams(request.getQueryParams().toString());
        template.setRequestHeader(request.getHeaders().toString());

        if (path == null || isExceptRequestPath(path)) {
            return chain.filter(exchange);
        }

        return chain.filter(new ServerWebExchangeDecorator(exchange) {
            @Override
            public ServerHttpRequest getRequest() {
                return new WrappedServerHttpRequest(exchange.getRequest(), template);
            }

            @Override
            public ServerHttpResponse getResponse() {
                return new WrappedServerHttpResponse(exchange.getResponse(), template);
            }
        }).doFinally(signalType -> {
            Long endTime = System.currentTimeMillis();
            template.setElapsedTime(endTime - startTime);

            new JsonLogger().log(template);
        });
    }

    private boolean isExceptRequestPath(String path) {
        return isHealthCheck(path) || isSwaggerOrFavicon(path);
    }

    private boolean isHealthCheck(String path) {
        return Stream.of("/api/ping", "/actuator/health")
                .anyMatch(path::startsWith);
    }

    private boolean isSwaggerOrFavicon(String path) {
        return Stream.of("/swagger", "/swagger-ui.html", "/null/swagger-resources/configuration/ui", "/v2/api-docs", "/webjars/", "/favicon.ico")
                .anyMatch(path::startsWith);
    }
}