package buyingmarket.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class ServiceCommunicationConfiguration {
    @Bean
    public RestTemplate serviceCommunicationRestTemplate() {
        return new RestTemplate();
    }
}
