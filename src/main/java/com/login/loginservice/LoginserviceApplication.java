package com.login.loginservice;

import com.login.loginservice.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoginserviceApplication {
    private final UserRepository userRepository;
    public LoginserviceApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public static void main(String[] args) {
        SpringApplication.run(LoginserviceApplication.class, args);
    }
}
