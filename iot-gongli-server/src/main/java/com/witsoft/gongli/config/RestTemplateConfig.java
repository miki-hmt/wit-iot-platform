package com.witsoft.gongli.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate customRestTemplate(){
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();

        httpRequestFactory.setConnectTimeout(5000);
        httpRequestFactory.setReadTimeout(3000);

        return new RestTemplate(httpRequestFactory);
    }
}
