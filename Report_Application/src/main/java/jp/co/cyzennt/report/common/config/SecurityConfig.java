package jp.co.cyzennt.report.common.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**
 * spring security config
 * @author lj
 * 9/21/2023
 *
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Autowired
	private DataSource dataSource;

	// bean definition
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	// get user login infomationL
	private static final String USER_ACCOUNT_SQL =  "SELECT USERNAME,PASSWORD,TRUE"
														+ " FROM M_USER_INFO_ACCOUNT "
														+ " INNER JOIN M_USER_INFORMATION "
														+ " ON M_USER_INFORMATION.ID_PK = M_USER_INFO_ACCOUNT.USER_ID_PK "
														+ " WHERE USERNAME = ?" 
														+ " AND M_USER_INFORMATION.DELETE_FLG = FALSE " 
														+ " AND M_USER_INFO_ACCOUNT.DELETE_FLG = FALSE ";
	
	// ユーザのロールを取得するSQL
	private static final String USER_ROLE_SQL = " SELECT USERNAME "
												+ " , ROLE "
												+ " FROM M_USER_INFORMATION "
												+ " WHERE USERNAME = ? ";
			

	   
	   
	@Bean
	public UserDetailsManager userDetailsService(){
	
	    JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
	      
	    users.setUsersByUsernameQuery(USER_ACCOUNT_SQL);
	    users.setAuthoritiesByUsernameQuery(USER_ROLE_SQL);
	
	    return users;
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }

	/**
	 *  control the link method
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		// *************************************************
		// no login required page (method chain describes)
		// *************************************************
		http.authorizeRequests()
		// access is admitted to webjars
		.antMatchers("/webjars/**").permitAll()
		// access is admitted to css
		.antMatchers("/css/**").permitAll()
		// access is permitted to script
		.antMatchers("/script/**").permitAll()
		// access is admitted to login
		.antMatchers("/login").permitAll()
		// testing 
		.antMatchers("/admin/**").hasAuthority("ADMIN")
		.antMatchers("/view/leader/**").hasAuthority("LEADER")
		.antMatchers("/userTop/**").hasAuthority("USER")
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic()
		.and()
		.headers()
		.frameOptions()
		.disable();
		
		http.formLogin()
		.loginProcessingUrl("/login")
		.loginPage("/login")
		.failureUrl("/login")
		.usernameParameter("username")
		.passwordParameter("password")
		.successHandler(authenticationSuccessHandler());
//		.defaultSuccessUrl("/top",true);
		
	}	
}
