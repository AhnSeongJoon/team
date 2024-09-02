package com.shop.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    String apiKey = "1348213571574161";
    String secretKey = "F5oxyjSmUrE4pqAapS8LH4j48K6GgzGSroly78jlRMG4HsImjNulXUPk90SVObASIogKkQRwTHoHNVjW";

    @Bean
    public IamportClient iamportClient() {

        return new IamportClient(apiKey, secretKey);
    }
}
