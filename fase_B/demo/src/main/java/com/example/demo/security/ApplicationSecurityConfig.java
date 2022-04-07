package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

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
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*")
                    .permitAll()
                .antMatchers("api/group2/**")
                    .hasRole(USER.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails joseLuisUser = User.builder()
                .username("joseluis")
                .password(passwordEncoder.encode("gomespitgrupo2"))
                .roles(ADMIN.name()) // ROLE_ADMIN
                .build();
        UserDetails luisUser = User.builder()
                .username("luis")
                .password(passwordEncoder.encode("oliveirapitgrupo2"))
                .roles(ADMIN.name())
                .build();
        UserDetails alexandreUser = User.builder()
                .username("alex")
                .password(passwordEncoder.encode("cardosopitgrupo2"))
                .roles(ADMIN.name())
                .build();
        UserDetails catarinaUser = User.builder()
                .username("catarina")
                .password(passwordEncoder.encode("nevespitgrupo2"))
                .roles(ADMIN.name())
                .build();
        UserDetails teacherUser = User.builder()
                .username("teacher")
                .password(passwordEncoder.encode("teacherpitgrupo2"))
                .roles(USER.name())
                .build();

        return new InMemoryUserDetailsManager(List.of(
                joseLuisUser, luisUser,
                alexandreUser, catarinaUser,
                teacherUser
        ));
    }
}
