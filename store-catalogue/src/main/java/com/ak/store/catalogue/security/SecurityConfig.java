package com.ak.store.catalogue.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        http.csrf(CsrfConfigurer::disable);

//        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.cors(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/catalogue/products/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/catalogue/products/{id}/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/catalogue/products/poor").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/catalogue/products/search/*").permitAll()

                .requestMatchers("/api/v1/catalogue/products").hasRole("MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/v1/catalogue/products/price").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/v1/catalogue/products/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/v1/catalogue/products/exist/*").hasRole("MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/v1/catalogue/products/available/*").hasRole("MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/v1/catalogue/products/available/*").hasRole("MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/v1/catalogue/products/batch").hasRole("MANAGER")

                .requestMatchers(HttpMethod.GET, "/api/v1/catalogue/characteristics").permitAll()
                .requestMatchers("/api/v1/catalogue/characteristics/**").hasRole("MANAGER")

                .requestMatchers(HttpMethod.GET, "/api/v1/catalogue/categories").permitAll()
                .requestMatchers("/api/v1/catalogue/categories/**").hasRole("MANAGER")

                .anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        converter.setPrincipalClaimName("preferred_name");
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            var roles = (List<String>) jwt.getClaimAsMap("realm_access").get("roles");

            return Stream.concat(authorities.stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();
        });

        return converter;
    }
}
