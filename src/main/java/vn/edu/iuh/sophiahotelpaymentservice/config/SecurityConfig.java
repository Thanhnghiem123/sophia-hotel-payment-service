package vn.edu.iuh.sophiahotelpaymentservice.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import vn.edu.iuh.sophiahotelpaymentservice.filters.JWTAuthenticationFilter;

/**
 * @description
 * @author: vie
 * @date: 8/4/25
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

   private final CorsConfigurationSource corsConfigurationSource;
   private final JWTAuthenticationFilter jwtAuthenticationFilter;

   public SecurityConfig(CorsConfigurationSource corsConfigurationSource, JWTAuthenticationFilter jwtAuthenticationFilter) {
      this.corsConfigurationSource = corsConfigurationSource;
      this.jwtAuthenticationFilter = jwtAuthenticationFilter;
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http.csrf(AbstractHttpConfigurer::disable)
              .cors(cors -> cors.configurationSource(corsConfigurationSource))
              .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                      // Cho phép tất cả các requests mà không cần xác thực
                      .anyRequest().permitAll()
              )
              // Comment lại filter JWT để tạm thời không cần xác thực JWT
              // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
      return http.build();
   }
}