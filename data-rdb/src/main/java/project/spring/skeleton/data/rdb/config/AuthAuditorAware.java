package project.spring.skeleton.data.rdb.config;

import org.springframework.data.domain.AuditorAware;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class AuthAuditorAware implements AuditorAware<String> {
    @Override @NotNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of("API");
    }
}