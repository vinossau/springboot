package com.springboot.paymentapi.security;

import com.springboot.paymentapi.filter.X509AuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    protected void configure(HttpSecurity http) throws Exception
    {
        X509AuthenticationFilter customFilter = new X509AuthenticationFilter(http);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf()
                .disable().exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()).and()
                .authorizeRequests().antMatchers("/swagger-ui.html**","/customer/payment-initiate-request", "/swagger-resources/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security").permitAll()
                .anyRequest().authenticated().and().x509().x509AuthenticationFilter(customFilter);

    }

   /* @Configuration
    @EnableWebSecurity
    public class SpringBootSecurityConfiguration extends WebSecurityConfigurerAdapter  {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            auth.inMemoryAuthentication().withUser("user").password(encoder.encode("password")).roles("USER").and()
                    .withUser("admin").password(encoder.encode("admin")).roles("USER", "ADMIN");
        }
    }*/
}