package com.mflores.telecomapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );

        configuration.setAllowCredentials(true);

        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .spa()
                        .csrfTokenRepository(cookieCsrfTokenRepository()))
                .authorizeHttpRequests(auth -> auth
                        //Allow preflight OPTIONS requests (critical for React/Angular/Vue CORS)
                        //Public endpoints (login, register, public assets)
                        .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/customer-registrations").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/registration/verify").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/registration/verify").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/csrf").permitAll()
                        //Role-based endpoints (optional)
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN")
                        //Lock down all other endpoints to authenticated users only
                        .anyRequest().authenticated()
                )
                .securityContext(context -> context
                        .securityContextRepository(contextRepository())
                        .requireExplicitSave(true)
                )
                //Return a clean 401 Unauthorized instead of a default HTML login redirect
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .logout(logout -> logout
                        // 1. Set the API endpoint URL (defaults to POST /logout)
                        .logoutUrl("/api/auth/logout")

                        // 2. Destroy the server-side HttpSession
                        .invalidateHttpSession(true)

                        // 3. Clear the SecurityContextHolder
                        .clearAuthentication(true)

                        // 4. Delete the session and CSRF cookies from the browser
                        .deleteCookies("JSESSIONID", "XSRF-TOKEN")

                        // 5. Return 200 OK instead of redirecting to /login?logout
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))

                )
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    "When you need to authenticate a customer, retrieve their information using my CustomUserDetailsService,
    and compare their submitted password against the stored hash using my PasswordEncoder."
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository contextRepository() {

        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(CsrfTokenRepository csrfTokenRepository) {

        return new CompositeSessionAuthenticationStrategy(List.of(new ChangeSessionIdAuthenticationStrategy(),
                new CsrfAuthenticationStrategy(csrfTokenRepository)));
    }

    //React needs to read the Csrf token, retrieve it and include it in the X-XSRF-TOKEN header
    //natively it can't do that.
    //This bean creates the repository with JavaScript-readable CSRF cookies.
    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }
}