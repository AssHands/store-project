package com.ak.store.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Configuration
public class ElasticsearchRestTemplate {

    @Value("${api.base.url}")
    private String baseUrl;

    @Value("${api.username}")
    private String username;

    @Value("${api.password}")
    private String password;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(basicAuthInterceptor()));
        return restTemplate;
    }

    private ClientHttpRequestInterceptor basicAuthInterceptor() {
        return new ClientHttpRequestInterceptor() {
            @Override
            public org.springframework.http.client.ClientHttpResponse intercept(HttpRequest request,
                                                                                byte[] body,
                                                                                ClientHttpRequestExecution execution) throws IOException {
                String auth = username + ":" + password;
                String basicAuth = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                request.getHeaders().add("Authorization", basicAuth);
                return execution.execute(request, body);
            }
        };
    }
}