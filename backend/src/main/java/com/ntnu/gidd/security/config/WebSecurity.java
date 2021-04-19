package com.ntnu.gidd.security.config;

import com.ntnu.gidd.security.filter.JWTAuthenticationFilter;
import com.ntnu.gidd.security.filter.JWTUsernamePasswordAuthenticationFilter;
import com.ntnu.gidd.service.UserDetailsServiceImpl;
import com.ntnu.gidd.service.token.RefreshTokenService;
import com.ntnu.gidd.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@AllArgsConstructor
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private RefreshTokenService refreshTokenService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtUtil jwtUtil;
    private JWTConfig jwtConfig;

    /**
     * Sets up the web security configuration
     * @param httpSecurity
     * @throws Exception
     */
    //TODO: Make better research on what the configuration doe
    // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#jc-httpsecurity
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().configurationSource(request -> {
                  var cors = new CorsConfiguration();
                  cors.setAllowedOrigins(List.of("*"));
                  cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
                  cors.setAllowedHeaders(List.of("*"));
                  return cors;
                })
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtConfig.getUri() + "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/users/").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage()))
                .and()
                .addFilter(new JWTUsernamePasswordAuthenticationFilter(refreshTokenService, authenticationManager(), jwtConfig))
                .addFilterAfter(new JWTAuthenticationFilter(jwtConfig, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * This sets up the configuration for Cross-Origin Resource Sharing (CORS)
     *
     * Note:
     * Cors is not a protection agains CSRF attacks
     * Poor cors configuration opens for cross-domain based attacks
     *
     * @return the configuration source
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

}
