package com.dung.UniStore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS_POST={"api/v1/users/register","api/v1/auth/login","api/v1/auth/introspect","/api/v1/auth/logout","/api/v1/auth/refresh"};

    @Autowired
    private CustomJwtDecoder customJwtDecoder;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                       // .requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS_POST).permitAll()
                     //   .requestMatchers(HttpMethod.GET,"api/v1/users").hasAuthority("ROLE_ADMIN")
//                        .anyRequest().authenticated()
                                .requestMatchers("/v3/api-docs/**").permitAll()  // Cho phép truy cập đến API docs
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2->oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())

                        )
                        .authenticationEntryPoint(new JwtAuthenEntryPoint())
                )
//                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //da thuc thi cac method thuat toan ma hoa
    }
//    @Bean
//    JwtDecoder jwtDecoder()
//    {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(),"HS512");
//       return  NimbusJwtDecoder
//               .withSecretKey(secretKeySpec)
//               .macAlgorithm(MacAlgorithm.HS512)
//               .build();
//    }
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource=new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter=new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }


}
