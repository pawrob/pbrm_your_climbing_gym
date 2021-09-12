package pl.ftims.ias.your_climbing_gym.utils.dbconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import pl.ftims.ias.your_climbing_gym.entities.AbstractEntity;
import pl.ftims.ias.your_climbing_gym.entities.AccessLevelEntity;
import pl.ftims.ias.your_climbing_gym.entities.PersonalDataEntity;
import pl.ftims.ias.your_climbing_gym.entities.UserEntity;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "pl.ftims.ias.your_climbing_gym.mok",
        entityManagerFactoryRef = "mokEntityManagerFactory",
        transactionManagerRef = "mokTransactionManager"
)
public class MokDbConfig {

    @Primary
    @Bean
    @ConfigurationProperties("app.datasource.mok")
    public DataSourceProperties mokDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary

    @Bean
    @ConfigurationProperties("app.datasource.mok.configuration")
    public DataSource mokDataSource() {
        return mokDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }


    @Primary

    @Bean(name = "mokEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mokEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mokDataSource())
                .packages(UserEntity.class, PersonalDataEntity.class, AccessLevelEntity.class, AbstractEntity.class)
                .build();
    }


    @Primary
    @Bean
    public PlatformTransactionManager mokTransactionManager(
            final @Qualifier("mokEntityManagerFactory") LocalContainerEntityManagerFactoryBean mokEntityManagerFactory) {
        return new JpaTransactionManager(mokEntityManagerFactory.getObject());
    }
}
