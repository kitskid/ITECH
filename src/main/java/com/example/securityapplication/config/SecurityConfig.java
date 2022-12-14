package com.example.securityapplication.config;

import com.example.securityapplication.services.PersonReactDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonReactDetailsService personReactDetailsService;
    private JWTTokenHelper jwtTokenHelper;

    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(PersonReactDetailsService personReactDetailsService, JWTTokenHelper jwtTokenHelper, AuthenticationEntryPoint authenticationEntryPoint) {
        this.personReactDetailsService = personReactDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }


    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //@Bean
    //CorsConfigurationSource corsConfigurationSource() {
    //    CorsConfiguration configuration = new CorsConfiguration();
    //    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    //    configuration.setAllowedMethods(Arrays.asList("GET","POST"));
    //    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //    source.registerCorsConfiguration("/**", configuration);
    //    return source;
    //}

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint).and()
                .authorizeRequests((request) -> request.antMatchers("/main", "/main/api/authentication", "/main/api/post", "/main/api/products", "/main/api/product/image/get/{filename:.+}", "/main/api/product/category/all").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/img/**", "/admin/search", "/admin/orders", "/admin/status", "/admin/update/status", "/main/api/product/category", "/main/api/providers", "/main/api/provider/delete/{id}", "/main/api/provider/update", "main/api/provider/add", "/main/api/provider/avatar", "/main/api/provider/image/get/{filename:.+}", "/main/api/product/avatar/get/{filename:.+}", "/main/api/product/avatar", "/main/api/product/add","/main/api/delete/{id}", "/main/api/update", "main/api/postadmin/avatar/get/", "main/api/postadmin/avatar/get/{filename:.+}", "/main/api", "/main/api/postadmin", "main/api/postadmin/avatar").hasRole("ADMIN")
                .antMatchers("/main/api/providers/{login}","/main/api/products/{id}", "/main/api/product/category/provider", "/main/api/product/avatar" ).hasRole("PROVIDER")
                .antMatchers("/cart", "/cart/delete/{id}", "/cart/add/{id}", "/order/create", "/orders", "/orders/sale").hasRole("USER")
                .antMatchers("/main/api/product/update", "/main/api/product/add").hasAnyRole("ADMIN", "PROVIDER")
                .anyRequest().authenticated())
                .addFilterBefore(new JWTAuthenticationFilter(personReactDetailsService, jwtTokenHelper), UsernamePasswordAuthenticationFilter.class);

        httpSecurity.csrf().disable().cors().and().headers().frameOptions().disable();
        //        .csrf().disable()
        //        .authorizeRequests()
        //        .antMatchers("/reqres.in/api/**").permitAll()
        //        .antMatchers("/authentication/login", "/error", "/main", "/main/api", "/main/1", "/main/api/post", "/authentication/registration").permitAll()
        //        .antMatchers("/admin", "/restapi", "/api/info").hasRole("ADMIN")
        //        .anyRequest().hasAnyRole("USER", "ADMIN")
        //        //.authenticated()
        //        .and()
        //        .formLogin().loginPage("/authentication/login")
        //        .loginProcessingUrl("/process_login")
        //        .defaultSuccessUrl("/index", true)
        //        .failureUrl("/authentication/login?error")
        //        .and()
        //        .logout().logoutUrl("/logout").logoutSuccessUrl("/main");

    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(personReactDetailsService).passwordEncoder(getPasswordEncoder());
    }

    //public void addCorsMapping(CorsRegistry registry) {
    //    registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedMethods("*");
    //}


    //WebSecurityConfiguration {
    //   private final AuthenticationProvider authenticationProvider;

    //
            //   public SecurityConfig(AuthenticationProvider authenticationProvider) {
        //       this.authenticationProvider = authenticationProvider;
        //   }

    //   protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder){
        //       authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        //   }

}
