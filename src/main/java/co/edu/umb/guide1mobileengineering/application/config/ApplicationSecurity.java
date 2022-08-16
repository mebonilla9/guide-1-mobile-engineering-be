package co.edu.umb.guide1mobileengineering.application.config;

import co.edu.umb.guide1mobileengineering.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public record ApplicationSecurity(
  UserRepository userRepository,
  JwtTokenFilter jwtTokenFilter) {

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
          .orElseThrow(() -> new UsernameNotFoundException("User with the mail" + email + " not found"));
      }
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.exceptionHandling().authenticationEntryPoint(
      (request, response, authException) -> {
        System.out.println(authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
          authException.getMessage()
        );
      }
    );
    http.authorizeRequests()
      .antMatchers(
        "/api/v1/auth/login",
        "/api/v1/auth/register",
        "/api/v1/auth/restore"
      ).permitAll()
      .anyRequest().authenticated();

    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
