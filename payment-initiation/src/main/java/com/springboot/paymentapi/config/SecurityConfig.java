package com.springboot.paymentapi.config;

import com.springboot.paymentapi.exception.UnknownCertificateException;
import com.springboot.paymentapi.filter.X509CustomAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf()
                .disable().exceptionHandling()
                .authenticationEntryPoint(new CustomEntryPoint()).and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/initiate-payment").permitAll()
                .anyRequest().authenticated().and().x509()
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .userDetailsService(userDetailsService())
                .x509AuthenticationFilter(new X509CustomAuthenticationFilter(http));
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            if (username.equals("Sandbox-TPP")) {
                return new User(username, "",
                        AuthorityUtils
                                .commaSeparatedStringToAuthorityList("ROLE_USER"));
            } else {
                throw new UnknownCertificateException("Unknown Certification");
            }
        };
    }
}