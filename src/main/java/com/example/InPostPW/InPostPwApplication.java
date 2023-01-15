package com.example.InPostPW;

import com.example.InPostPW.builder.NewUserBuilder;
import com.example.InPostPW.dto.RegistrationFormDto;
import com.example.InPostPW.model.Role;
import com.example.InPostPW.model.User;
import com.example.InPostPW.repository.RoleRepository;
import com.example.InPostPW.repository.UserRepository;
import com.example.InPostPW.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@SpringBootApplication
public class InPostPwApplication {

    public static void main(String[] args) {
        SpringApplication.run(InPostPwApplication.class, args);
    }

}
