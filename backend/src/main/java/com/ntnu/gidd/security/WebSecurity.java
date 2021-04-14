package com.ntnu.gidd.security;

import com.ntnu.gidd.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    /**
     * Sets up the web security configuration
     * @param httpSecurity
     * @throws Exception
     */
    //TODO: Make better research on what the configuration doe
    // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#jc-httpsecurity
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtConfig().getUri() + "/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage()))
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtConfig()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtConfig()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(bCryptPasswordEncoder());
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

    //TODO: Is this the right place to have this
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTConfig jwtConfig() {
        return new JWTConfig();
    }
}
