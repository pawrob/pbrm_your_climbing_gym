package pl.ftims.ias.your_climbing_gym.utils.dbconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.ftims.ias.your_climbing_gym.entities.*;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"pl.ftims.ias.your_climbing_gym.auth"},
        entityManagerFactoryRef = "authEntityManagerFactory",
        transactionManagerRef = "authTransactionManager"
)
public class AuthDbConfig {


    @Bean
    @ConfigurationProperties("app.datasource.auth")
    public DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @ConfigurationProperties("app.datasource.auth.configuration")
    public DataSource authDataSource() {
        return authDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }


    @Bean(name = "authEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean authEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(authDataSource())
                .packages(SessionLogEntity.class, AuthenticationViewEntity.class, UserEntity.class)
                .build();
    }
    @Bean
    public InstrumentationLoadTimeWeaver loadTimeWeaver()  throws Throwable {
        InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        return loadTimeWeaver;
    }

    @Bean
    public PlatformTransactionManager authTransactionManager(
            final @Qualifier("authEntityManagerFactory") LocalContainerEntityManagerFactoryBean authEntityManagerFactory) {
        return new JpaTransactionManager(authEntityManagerFactory.getObject());
    }
}
