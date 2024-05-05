package com.security.CookieBasedAuth;

import com.security.CookieBasedAuth.helpers.RefreshableCRUDRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = RefreshableCRUDRepositoryImpl.class)
public class CookieBasedAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookieBasedAuthApplication.class, args);
	}

}
