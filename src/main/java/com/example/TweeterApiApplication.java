package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@SpringBootApplication
public class TweeterApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweeterApiApplication.class, args);
	}

	@Configuration
	@EnableResourceServer
	static class ResourceServerConfig extends ResourceServerConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and().authorizeRequests().antMatchers(HttpMethod.GET, "/v1/**")
					.access("#oauth2.hasScope('tweet.read')")
					.antMatchers(HttpMethod.POST, "/v1/**")
					.access("#oauth2.hasScope('tweet.write')")
					.antMatchers(HttpMethod.PUT, "/v1/**")
					.access("#oauth2.hasScope('tweet.write')")
					.antMatchers(HttpMethod.DELETE, "/v1/**")
					.access("#oauth2.hasScope('tweet.write')")
					.antMatchers(HttpMethod.PATCH, "/v1/**")
					.access("#oauth2.hasScope('tweet.write')");
		}
	}
}
