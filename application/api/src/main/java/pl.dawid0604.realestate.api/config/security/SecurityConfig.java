/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.security;

import static lombok.AccessLevel.PACKAGE;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor(access = PACKAGE)
class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final AccessDeniedHandlerCustom accessDeniedHandler;
    private final AuthenticationEntryPointCustom authenticationEntryPoint;
    private final MdcUserFilter mdcUserFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(getAuthorizedHttpRequests())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(mdcUserFilter, jwtAuthFilter.getClass())
                .exceptionHandling(
                        ex ->
                                ex.accessDeniedHandler(accessDeniedHandler)
                                        .authenticationEntryPoint(authenticationEntryPoint))
                .build();
    }

    private static @NonNull Customizer<
                    AuthorizeHttpRequestsConfigurer<HttpSecurity>
                            .AuthorizationManagerRequestMatcherRegistry>
            getAuthorizedHttpRequests() {

        return auth ->
                auth.requestMatchers("/api/auth/**")
                        .permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**")
                        .permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/advertisement/flat/find",
                                "/api/advertisement/house/find",
                                "/api/advertisement/commercial/find",
                                "/api/advertisement/plot/find")
                        .permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/advertisement/flat/{slug}",
                                "/api/advertisement/house/{slug}",
                                "/api/advertisement/commercial/{slug}",
                                "/api/advertisement/plot/{slug}")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/locality", "/api/locality/{id}")
                        .permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/user/activate")
                        .permitAll()
                        .requestMatchers("/actuator/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
