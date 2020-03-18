package HSCI.HSCIFIVER;

import HSCI.HSCIFIVER.constant.Roles;
import HSCI.HSCIFIVER.entity.Physician;
import HSCI.HSCIFIVER.entity.User;
import HSCI.HSCIFIVER.filters.JwtRequestFilter;
import HSCI.HSCIFIVER.repositories.PhysicianRepository;
import HSCI.HSCIFIVER.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class HsciFiverApplication {
	@Autowired
	private PhysicianRepository physicianRepository;
	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(HsciFiverApplication.class, args);


	}
	@PostConstruct
	private void init(){
		physicianRepository.save(new Physician());
//		User user = new User();
//		user.setUsername("admin");
//		user.setPassword("admin");
//		user.setRole(Roles.ADMIN);
//		userRepository.save(user);
	}




	@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private UserDetailsService myUserDetailsService;
		@Autowired
		private JwtRequestFilter jwtRequestFilter;

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(myUserDetailsService);
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return NoOpPasswordEncoder.getInstance();
		}

		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {

			httpSecurity.csrf().disable().
					authorizeRequests().antMatchers("/authenticate","/register/**").permitAll().
					anyRequest().authenticated().and().
					exceptionHandling().and().sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		}
		@Bean
		public ModelMapper modelMapper() {
			return new ModelMapper();
		}
	}


}
