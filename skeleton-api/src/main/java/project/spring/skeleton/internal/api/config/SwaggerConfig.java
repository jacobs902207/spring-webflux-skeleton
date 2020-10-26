package project.spring.skeleton.internal.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proejct.spring.skeleton.common.constant.common.GatewayHeader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("project.spring.skeleton.api"))
                .build()
                .genericModelSubstitutes(Optional.class, Flux.class, Mono.class);
    }

    private ApiKey apiKey() {
        return new ApiKey(GatewayHeader.USER_AGENT_HEADER_KEY,
                GatewayHeader.USER_AGENT_HEADER_KEY, "header");
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return Collections.singletonList(new SecurityReference(GatewayHeader.USER_AGENT_HEADER_KEY, authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Documents")
                .version("0.0.1")
                .build();
    }
}