package com.example.redditclone.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(
            AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {

        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        return multipartResolver;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean()
            throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter
                .setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        //Naglasavamo browser-u da ne cache-ira podatke koje dobije u header-ima
        //detaljnije: https://www.baeldung.com/spring-security-cache-control-headers
        httpSecurity.headers().cacheControl().disable();
        //Neophodno da ne bi proveravali autentifikaciju kod Preflight zahteva

        httpSecurity.cors();
        httpSecurity.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/sort-posts/{option}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/sort-posts/by-subreddit/{id}/{option}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/findOne/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/subreddit/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/subreddits/findOne/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/subreddits").permitAll()
                .antMatchers(HttpMethod.GET, "/api/comments").permitAll()
                .antMatchers(HttpMethod.GET, "/api/comments/sort-comments/by-post/{option}/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/flairs").permitAll()
                .antMatchers(HttpMethod.GET, "/api/flairs/findOne/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/flairs/bySubreddit/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/image/get/{imageName}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/comments/children/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/comments/post/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/byUsername/{username}").permitAll()


                .anyRequest().authenticated();



        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Configuration
    public static class WebConfig implements WebMvcConfigurer {
        public WebConfig() {
        }

        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOriginPatterns(new String[]{"*"})
                    .allowedMethods(new String[]{"HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS"})
                    .allowedHeaders(new String[]{"Authorization", "content-type"})
                    .exposedHeaders(new String[]{"Total-Pages, Sprint-Total"})
                    .allowCredentials(true).maxAge(3600L);
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            Path uploadDir = Paths.get("./post-images");
            String uploadPath = uploadDir.toFile().getAbsolutePath();

            registry.addResourceHandler("/post-images/**").addResourceLocations("file:/" + uploadPath + "/");

        }

    }
}
