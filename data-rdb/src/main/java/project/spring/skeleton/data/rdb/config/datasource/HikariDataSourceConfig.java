package project.spring.skeleton.data.rdb.config.datasource;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Getter
//@Setter
//@Configuration
//@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class HikariDataSourceConfig extends HikariConfig { }