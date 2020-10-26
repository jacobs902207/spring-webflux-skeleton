package proejct.spring.skeleton.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnvironmentHelper {
    private final Environment environment;

    public String getActiveProfile() {
        return environment.getActiveProfiles()[0];
    }

    public String getCachePrefix() {
        final String active = getActiveProfile();

        switch (active) {
            case "test":
                return "test:";

            case "default":
            case "dev":
            case "sandbox":
                return "sandbox:";

            default:
                return "";
        }
    }
}