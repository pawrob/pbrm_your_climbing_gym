package pl.ftims.ias.your_climbing_gym.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import pl.ftims.ias.your_climbing_gym.utils.listeners.DefaultListenerSupport;

@Configuration
public class AppConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.registerListener(new DefaultListenerSupport());
        return retryTemplate;
    }
}
