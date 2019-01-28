package com.reddit.redditlight.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  private ApplicationUserDetailsService applicationUserDetailsService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public WebSecurity(ApplicationUserDetailsService applicationUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.applicationUserDetailsService = applicationUserDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public WebSecurity(boolean disableDefaults, ApplicationUserDetailsService applicationUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    super(disableDefaults);
    this.applicationUserDetailsService = applicationUserDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder
        .userDetailsService(applicationUserDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and().csrf().disable()
        .authorizeRequests()
        .antMatchers("/users/**").permitAll()
        .antMatchers("/").permitAll()
        .antMatchers("/**.ico", "/**.png", "/**.jpg", "/static/**", "/css/**", "/img/**", "/**.css").permitAll()
        .anyRequest().authenticated()
        .antMatchers("/api/**").authenticated().anyRequest().permitAll()
        .and().formLogin().loginPage("/login").permitAll()
        .and().logout().logoutSuccessUrl("/login?logout").permitAll()
        .and().addFilter(new JWTAuthenticationFilter(authenticationManager()))
        .addFilter(new JWTAuthorizationFilter(authenticationManager()));
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }

}

