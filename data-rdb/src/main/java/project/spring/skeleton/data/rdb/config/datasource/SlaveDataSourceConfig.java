package project.spring.skeleton.data.rdb.config.datasource;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "datasource.slave")
@PropertySource("classpath:db/db-${spring.profiles.active}.properties")
public class SlaveDataSourceConfig extends HikariConfig {
}