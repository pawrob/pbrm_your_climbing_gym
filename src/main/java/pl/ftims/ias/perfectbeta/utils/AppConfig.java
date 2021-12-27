package pl.ftims.ias.perfectbeta.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import pl.ftims.ias.perfectbeta.utils.loggers.TransactionListenerSupport;

@Configuration
public class AppConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.registerListener(new TransactionListenerSupport());
        return retryTemplate;
    }
}
