package com.ak.store.catalogue;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.hibernate.validator.HibernateValidator;
import org.modelmapper.ModelMapper;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.KafkaListener;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.endpoints.Endpoint;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.endpoints.S3EndpointParams;
import software.amazon.awssdk.services.s3.endpoints.S3EndpointProvider;

import javax.sql.DataSource;


@EnableJpaRepositories("com.ak.store.*")
@ComponentScan(basePackages = {"com.ak.store.*" })
@EntityScan("com.ak.store.*")
@SpringBootApplication
public class CatalogueProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogueProjectApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Validator validator() {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }

    @Bean
    public ElasticsearchClient ElasticsearchClient() {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "elastic_pass"));

        RestClientBuilder builder = RestClient.builder(
                        new HttpHost("localhost", 9200))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(
                            HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        ElasticsearchTransport transport = new RestClientTransport(
                builder.build(), new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

    @Bean
    public S3Client S3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create("m50GIYQsqE3ndcCiLESi",
                "GKcCNJ7RiEEZak8uuMkKhkLMaupZOTGQlQ4Cj81h");
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

        S3EndpointParams endpointParams = S3EndpointParams.builder()
                .endpoint("http://localhost:9000")
                .region(Region.US_EAST_1)
                .build();

        Endpoint endpoint = S3EndpointProvider
                .defaultProvider()
                .resolveEndpoint(endpointParams).join();

        S3Configuration s3Configuration = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        return S3Client.builder()
                .endpointOverride(endpoint.url())
                .credentialsProvider(provider)
                .region(Region.US_EAST_1)
                .serviceConfiguration(s3Configuration)
                .build();
    }
}