package proejct.spring.skeleton.common.environment;

import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.*;

public class YmlFileLoaderProcessor implements EnvironmentPostProcessor {
    private static final Class UNMODIFIABLE_MAP_CLASS = Collections.unmodifiableMap(new HashMap()).getClass();

    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {
        List<String> activeProfiles = Lists.newArrayList(environment.getActiveProfiles());

        ResourceLoader resourceLoader = application.getResourceLoader();
        if (null == resourceLoader) {
            resourceLoader = new DefaultResourceLoader();
        }

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);

        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath*:/application-*");
            YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
            for (Resource resource : resources) {
                List<PropertySource> propertySources = new ArrayList<>(yamlPropertySourceLoader.load(resource.getFilename(), resource));
                for (PropertySource propertySource : propertySources) {
                    if (propertySource.getSource() instanceof LinkedHashMap) {
                        Object profile = ((LinkedHashMap) propertySource.getSource()).get("spring.profiles");
                        if (null != profile) {
                            if (activeProfiles.contains(profile.toString())) {
                                environment.getPropertySources().addLast(propertySource);
                            }
                        } else {
                            environment.getPropertySources().addLast(propertySource);
                        }

                    } else if (propertySource.getSource().getClass().equals(UNMODIFIABLE_MAP_CLASS)) {
                        Object profile = propertySource.getProperty("spring.profiles");
                        if (null != profile) {
                            if (activeProfiles.contains(profile.toString())) {
                                environment.getPropertySources().addLast(propertySource);
                            }
                        }

                    } else {
                        environment.getPropertySources().addLast(propertySource);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}