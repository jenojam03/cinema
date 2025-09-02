package org.example.cinema.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
//import org.example.cinema.jwt.CustomJwtConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


   /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );

        return http.build();
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, EnsureClienteExistsFilter ensureClienteExistsFilter) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                //questi endpoint possono essere raggiunti da chiunque (quindi un utente può sfogliare
                //il catalogo senza essere loggato)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/clienti/registrazione",
                                "/film",
                                "/film/paged",
                                "/film/{id}",
                                "/film/{id}/proiezioni",
                                "/film/{id}/proiezioni/paged",
                                "/film/search/**",
                                "/proiezioni",
                                "/proiezioni/{id}",
                                "/proiezioni/{proiezioneId}/posti_disponibili",
                                "/proiezioni/{proiezioneId}/posti",
                                "/proiezioni/betweenDates",
                                "/proiezioni/byDate",
                                "/proiezioni/byDatePaged",
                                "/proiezioni/future",
                                "/proiezioni/betweenDatesPaged",
                                "/biglietti/prezzo",
                                "/biglietti/prezzoBase"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverterM()))
                );
        http.addFilterAfter(ensureClienteExistsFilter, org.springframework.security.web.authentication.AnonymousAuthenticationFilter.class);
        return http.build();

    }


    /*@Bean
        public Converter<Jwt, ? extends AbstractAuthenticationToken> customJwtConverterM() {
            return new CustomJwtConverter();
        }
    }*/

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterM() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = new ArrayList<GrantedAuthority>();

            var realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                var roles = realmAccess.get("roles");
                if (roles instanceof List<?> list) {
                    list.forEach(role ->
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
                    );
                }
            }

            return authorities;
        });
        return jwtAuthenticationConverter;
    }
}

