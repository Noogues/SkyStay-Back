package com.example.skystayback;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EntityScan("com.example.skystayback.models")
@EnableJpaRepositories("com.example.skystayback.repositories")
public class SkyStayBackApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SkyStayBackApplication.class);

        app.addInitializers((ApplicationContextInitializer<ConfigurableApplicationContext>) applicationContext -> {
            ConfigurableEnvironment env = applicationContext.getEnvironment();
            String activeProfile = env.getProperty("spring.profiles.active", "default");
            if ("dev".equals(activeProfile) && new java.io.File(".env").exists()) {
                Dotenv dotenv = Dotenv.load();
                setIfPresent(dotenv, "DB_HOST");
                setIfPresent(dotenv, "DB_PORT");
                setIfPresent(dotenv, "DB_NAME");
                setIfPresent(dotenv, "DB_USER");
                setIfPresent(dotenv, "DB_PASSWORD");
                setIfPresent(dotenv, "EM_HOST");
                setIfPresent(dotenv, "EM_PORT");
                setIfPresent(dotenv, "EM_USERNAME");
                setIfPresent(dotenv, "EM_PASSWORD");
            }
        });

        app.run(args);
    }

    private static void setIfPresent(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        if (value != null) {
            System.setProperty(key, value);
        }
    }

}
