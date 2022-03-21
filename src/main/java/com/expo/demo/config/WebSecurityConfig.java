package com.expo.demo.config;

import com.expo.demo.util.XUrlAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new XUrlAuthenticationSuccessHandler();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * Main method to configure authorization and authentication
     * Please note, if you using ROLE to add permission or something else,
     * write role name without "ROLE_" prefix, Spring security will add it for you
     * @param http HttpSecurity file
     * @throws Exception exception if something is wrong
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/member/**").hasAnyRole("MEMBER")
                .antMatchers("/admin/**")
                .hasAnyRole("ADMIN", "MODERATOR")
                .antMatchers("/webjars/**", "/img/**", "/css/**", "/js/**").permitAll()
                .antMatchers("/expiredSession").permitAll()
                .antMatchers("/register*").permitAll()
                .antMatchers("/register/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successHandler(authenticationSuccessHandler()).permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied");

        http.sessionManagement().maximumSessions(1).expiredSessionStrategy(new ExpSessionStrategy()).expiredUrl("/expiredSession");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    class ExpSessionStrategy implements SessionInformationExpiredStrategy {

        @Override
        public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
            sessionInformationExpiredEvent.getResponse().sendRedirect(sessionInformationExpiredEvent.getRequest().getContextPath() + "/expiredSession");
        }
    }
}