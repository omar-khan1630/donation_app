package com.donation.api.security.config;
/**
 * 
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.donation.api.security.utll.AuthEntryPointJwt;
import com.donation.api.security.utll.AuthFilter;
import com.donation.api.service.impl.UserDetailsServiceImpl;


/**
 * @author TA Admin
 *
 * 
 */

@Configuration
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class WebSecurityConfig {
	
	  @Autowired
	  UserDetailsServiceImpl userDetailsService;

	  @Autowired
	  private AuthEntryPointJwt unauthorizedHandler;
	  
	  @Bean
	  public AuthFilter authenticationJwtTokenFilter() {
	    return new AuthFilter();
	  }
	  
	  @Bean
	  public DaoAuthenticationProvider authenticationProvider() {
	      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	       
	      authProvider.setUserDetailsService(userDetailsService);
	      authProvider.setPasswordEncoder(passwordEncoder());
	   
	      return authProvider;
	  }
	  
	  @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	    return authConfig.getAuthenticationManager();
	  }

	  @Bean
	  public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  }
	  
//	  @Bean
//	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	    http.cors().and().csrf().disable()
//	    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//        .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//        .antMatchers("/api/test/**").permitAll()
//        .antMatchers("/api/organization/**", "/api/donor/**").hasAnyRole("ADMIN", "ORGANIZATION")
//        .antMatchers("/api/volunteer/**").hasAnyRole("ADMIN", "VOLUNTEER")
//        .antMatchers("/api/needy/**").hasAnyRole("ADMIN", "NEEDY")
//        .anyRequest().authenticated();
//	    
//	    http.authenticationProvider(authenticationProvider());
//
//	    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//	    
//	    return http.build();
//	  }
	  @Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	      http.cors().and().csrf().disable()
	          .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
	          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	          .authorizeRequests()
	          .antMatchers("/api/auth/**").permitAll()
	            .antMatchers("/api/test/**").permitAll()
	            .antMatchers("/api/organizations/**").hasRole("ADMIN") // Only Admin can access Organization-related endpoints
	            .antMatchers("/api/donor/**").hasAnyRole("ADMIN", "ORGANIZATION","DONOR")
	            .antMatchers("/api/volunteer/**").hasAnyRole("ORGANIZATION", "VOLUNTEER")
	            .antMatchers("/api/needy/**").hasAnyRole("ORGANIZATION", "VOLUNTEER")
	            .anyRequest().authenticated();
//	              .antMatchers("/api/auth/**").permitAll()
//	              .antMatchers("/api/test/**").permitAll()
//	              .antMatchers("/api/organizations/**", "/api/donor/**").hasAnyRole("ADMIN", "ORGANIZATION")
//	              .antMatchers("/api/volunteer/**").hasAnyRole("ADMIN", "VOLUNTEER")
//	              .antMatchers("/api/needy/**").hasAnyRole("ADMIN", "NEEDY")
//	              .anyRequest().authenticated();
	      
	      http.authenticationProvider(authenticationProvider());

	      http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	      return http.build();
	  }

}
