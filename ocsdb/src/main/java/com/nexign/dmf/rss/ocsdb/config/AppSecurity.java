package com.nexign.dmf.rss.ocsdb.config;


import com.nexign.dmf.rss.ocsdb.utils.KeycloakJwtRolesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AppSecurity {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter =
                // Using the delegating converter multiple converters can be combined
                new DelegatingJwtGrantedAuthoritiesConverter(
                        // First add the default converter
                        new JwtGrantedAuthoritiesConverter(),
                        // Second add our custom Keycloak specific converter
                        new KeycloakJwtRolesConverter());


        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtRolesConverter());


        http.authorizeHttpRequests((request) -> request
                        .requestMatchers("/ocsdb/**").hasRole("user"))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(authoritiesConverter.convert(jwt))))
        ;
        return http.build();
    }

}

