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
@EnableJpaRepositories(basePackages = "pl.ftims.ias.your_climbing_gym.mos",
        entityManagerFactoryRef = "mosEntityManagerFactory",
        transactionManagerRef = "mosTransactionManager"
)
public class MosDbConfig {

    @Primary
    @Bean
    @ConfigurationProperties("app.datasource.mos")
    public DataSourceProperties mosDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary

    @Bean
    @ConfigurationProperties("app.datasource.mos.configuration")
    public DataSource mosDataSource() {
        return mosDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }


    @Primary

    @Bean(name = "mosEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mosEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mosDataSource())
                .packages(UserEntity.class, PersonalDataEntity.class, AccessLevelEntity.class, AbstractEntity.class)
                .build();
    }


    @Primary
    @Bean
    public PlatformTransactionManager mosTransactionManager(
            final @Qualifier("mosEntityManagerFactory") LocalContainerEntityManagerFactoryBean mosEntityManagerFactory) {
        return new JpaTransactionManager(mosEntityManagerFactory.getObject());
    }
}
