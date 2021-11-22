package pl.ftims.ias.perfectbeta.utils.dbconfig;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import pl.ftims.ias.perfectbeta.entities.AbstractEntity;
import pl.ftims.ias.perfectbeta.entities.AccessLevelEntity;
import pl.ftims.ias.perfectbeta.entities.PersonalDataEntity;
import pl.ftims.ias.perfectbeta.entities.UserEntity;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "pl.ftims.ias.perfectbeta.mos",
        entityManagerFactoryRef = "mosEntityManagerFactory",
        transactionManagerRef = "mosTransactionManager"
)
public class MosDbConfig {


    @Bean
    @ConfigurationProperties("app.datasource.mos")
    public DataSourceProperties mosDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @ConfigurationProperties("app.datasource.mos.configuration")
    public DataSource mosDataSource() {
        return mosDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }


    @Bean(name = "mosEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mosEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mosDataSource())
                .packages(UserEntity.class, PersonalDataEntity.class, AccessLevelEntity.class, AbstractEntity.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager mosTransactionManager(
            final @Qualifier("mosEntityManagerFactory") LocalContainerEntityManagerFactoryBean mosEntityManagerFactory) {
        return new JpaTransactionManager(mosEntityManagerFactory.getObject());
    }
}
