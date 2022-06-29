package raf.osiguranje.accounttransaction.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceCommunicationConfiguration {
    @Bean
    public RestTemplate serviceCommunicationRestTemplate() {
        return new RestTemplate();
    }
}
