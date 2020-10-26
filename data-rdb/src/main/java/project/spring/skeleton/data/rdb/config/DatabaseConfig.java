package project.spring.skeleton.data.rdb.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import project.spring.skeleton.data.rdb.config.datasource.MasterDataSourceConfig;
import project.spring.skeleton.data.rdb.config.datasource.ReplicationRoutingDataSource;
import project.spring.skeleton.data.rdb.config.datasource.SlaveDataSourceConfig;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "project.spring.skeleton.data.rdb")
@EnableConfigurationProperties({MasterDataSourceConfig.class, SlaveDataSourceConfig.class})
public class DatabaseConfig {
    private static final String PERSISTENCE_UNIT_NAME = "skeleton";

    @Bean("mysqlJdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean
    public DataSource dataSource(MasterDataSourceConfig masterDataSourceConfig, SlaveDataSourceConfig slaveDataSourceConfig) {
        DataSource masterDataSource = new HikariDataSource(masterDataSourceConfig);
        DataSource slaveDataSource = new HikariDataSource(slaveDataSourceConfig);

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("slave", slaveDataSource);
        dataSourceMap.put("master", masterDataSource);

        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        routingDataSource.afterPropertiesSet();

        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaProperties jpaProperties) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("project.spring.skeleton.data");
        factory.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);

        Properties properties = new Properties();
        HibernateSettings hibernateSettings = new HibernateSettings();
        properties.putAll(new HibernateProperties().determineHibernateProperties(jpaProperties.getProperties(), hibernateSettings));
        factory.setJpaProperties(properties);

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}