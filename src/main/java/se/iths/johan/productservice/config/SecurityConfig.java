package se.iths.johan.productservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http

                .cors(Customizer.withDefaults())


                //Stänger av csrf
        .csrf(csrf -> csrf.disable())
                //Vi stänger av session, vi sparar inga cookies
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/error").permitAll()
//                        .requestMatchers("/products/**", "/products").permitAll() // Denna rad ska bord vi deployment
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                // vi gör ett OAuth2 anrop som pekar på jwtDecoder som kan tolka våran jwt token
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {

        //instansiera en ny CorsConfiguration
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //Vilka HTTP länker den får tillgång till som får göras
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));

        //Vilka anrop som får göras
        corsConfiguration.setAllowedMethods(List.of("GET","POST","DELETE","OPTIONS"));

        // Authorization är för att vi skcikar med en JWT token
        //Content-Type är för att vi skickar data i POST metoder
        // Accept är för att vi skickar DTO'er med json format
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));


        //instansiera en ny UrlBasedCorsConfigurationSource
        UrlBasedCorsConfigurationSource urlSource = new UrlBasedCorsConfigurationSource();

        //detta är så att CORS reglerna är aktiva på alla endpoints
        // pattern "/**" ger tillgång till alla sökvägar
        urlSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlSource;
    }


    // Denna metod behövs eftersom vi använder oss av roller, som vi plockar ut och mappar till spring security GrantedAuthority objekt
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) {
                return List.of();
            }
            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                    .toList();
        });
        return converter;
    }

    //JwtDecoder används för att läsa våran jwt token, den gör detta genom att hämta den public nyckeln via "auth/jwks". (Finns i AuthController i vårt huvud projekt)
    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String authServerUrl) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
                .withJwkSetUri(authServerUrl + "/auth/jwks")
                .build();
        jwtDecoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer(authServerUrl));
        return jwtDecoder;
    }

}
