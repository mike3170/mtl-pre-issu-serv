package com.stit.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

// dependence injection DI
// meta data
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService(DataSource ds) {
        // approach 1
        // return new JdbcUserDetailsManager(ds);

        // approach 2
        String usersByUsernameQuery
                = "select emp_no username, chg_pswd password, DECODE(stop_yn,'N',1,0) enabled from pwd_emp_pswd where login_id = ?";
//			= "select username, password, enabled from users where username = ?";
        String authsByUserQuery
                = "select username, authority from authorities where username = ?";

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(ds);

        userDetailsManager.setUsersByUsernameQuery(usersByUsernameQuery);
        userDetailsManager.setAuthoritiesByUsernameQuery(authsByUserQuery);
        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
		http.cors(c -> {
			CorsConfigurationSource source = request -> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOriginPatterns(List.of("*"));
				config.setAllowedHeaders(List.of("*"));
				config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
				config.setAllowCredentials(Boolean.TRUE);
				return config;
			};
			c.configurationSource(source);
		});
         */
        http.cors(c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOriginPatterns(this.getList("*"));
                config.setAllowedHeaders(this.getList("*"));
                config.setAllowedMethods(this.getList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowCredentials(Boolean.TRUE);
                return config;
            };
            c.configurationSource(source);
        });

        // work with jsp 
        http
                .authorizeRequests()
                .antMatchers("/index.html", "/*.css", "/*.js", "/*.ico").permitAll()
                .antMatchers("/login", "/api/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //                .defaultSuccessUrl("/login.html", true) MIKE
                .successHandler(new LoginSuccess())
                .failureHandler(new LoginFailure())
                .and()
                .csrf().disable();

        // authorities(privileage) example
        /*
		http
			.authorizeRequests()
				.antMatchers("/login").permitAll()
			  .antMatchers("/api/test/dav").hasAuthority("admin")
			  .antMatchers("/api/test/mia").hasAuthority("operator")
				.anyRequest().authenticated()
			.and()
			.formLogin()
			  .loginProcessingUrl("/login")
				.loginPage("/login")
		    .defaultSuccessUrl("/hello", false)
			.and()
			.csrf().disable();
         */
        // role-base  example
        /*
		http
			.authorizeRequests()
			.antMatchers("/login").permitAll()
			.mvcMatchers("/api/test/dav/**").hasRole("admin")
			.mvcMatchers("/api/test/mia/**").hasRole("operator")
			.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginProcessingUrl("/login")
			.loginPage("/login")
			.defaultSuccessUrl("/hello", true)
		.and()
		.csrf().disable();
         */
        // --------------------------------
        // for Angular client login
        /*
		http
			.authorizeRequests()
			.antMatchers("/ng1/index.html", "/ng1/*.css", "/ng1/*.js", "/ng1/*.ico").permitAll()
			.antMatchers("/ng1/**").permitAll()
			// .antMatchers("/index.html", "/*.css", "/*.js", "/*.ico").permitAll()
			.antMatchers("/", "/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.successHandler(new LoginSuccess())
			.failureHandler(new LoginFailure())
			.and()
			.csrf().disable();
         */
    }

    private void doSuccess(
            HttpServletRequest req,
            HttpServletResponse resp,
            Authentication auth) throws IOException {
        // -----
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"user\":\"" + auth.getName() + "\",\"isAuth\":\"" + auth.isAuthenticated() + "\"}");
    }

    private void doFailure(
            HttpServletRequest req,
            HttpServletResponse resp,
            AuthenticationException ex) throws IOException {
        // -----
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.getWriter().write("{\"status\":\"authtnticate failed\",\"isAuth\":\"false\"}");
    }

    private List<String> getList(String... strings) {
        List list = new ArrayList<String>();
        for (String str : strings) {
            list.add(str);
        }
        return list;
    }

}  // end class
