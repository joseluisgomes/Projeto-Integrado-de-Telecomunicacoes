package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.demo.security.ApplicationUserRole.ADMIN;
import static com.example.demo.security.ApplicationUserRole.USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/css/*", "/js/*").permitAll()
                .antMatchers("/index", "/data").hasAnyRole(ADMIN.name(), USER.name())
                .antMatchers("/api/**").hasRole(ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/index", true)
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(5)) // 5 days of duration
                    .key("security")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // CSRF is disabled
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/login"); // Key to hash the username and the expiration date
    }

    @Bean
    protected UserDetailsService userDetailsService(DataSource dataSource) {
        final var joseLuisUser = User.builder()
                .username("joseluis")
                .password(passwordEncoder.encode("gomespitgrupo2"))
                .roles(ADMIN.name()) // ROLE_ADMIN
                .build();

        final var luisUser = User.builder()
                .username("luis")
                .password(passwordEncoder.encode("oliveirapitgrupo2"))
                .roles(ADMIN.name())
                .build();

        final var alexandreUser = User.builder()
                .username("alex")
                .password(passwordEncoder.encode("cardosopitgrupo2"))
                .roles(ADMIN.name())
                .build();

        final var catarinaUser = User.builder()
                .username("catarina")
                .password(passwordEncoder.encode("nevespitgrupo2"))
                .roles(ADMIN.name())
                .build();

        final var teacherUser = User.builder()
                .username("teacher")
                .password(passwordEncoder.encode("teacherpitgrupo2"))
                .roles(USER.name())
                .build();

      //  final var manager = new JdbcUserDetailsManager(dataSource);

        return new InMemoryUserDetailsManager(List.of(
                joseLuisUser, luisUser, alexandreUser, catarinaUser, teacherUser
        ));
    }
}
