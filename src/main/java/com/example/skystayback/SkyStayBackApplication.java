package com.example.skystayback;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SkyStayBackApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication app = new org.springframework.boot.SpringApplication(SkyStayBackApplication.class);
        Environment env = app.run(args).getEnvironment();

        String activeProfile = env.getProperty("spring.profiles.active", "default");

        if ("dev".equals(activeProfile)) {
            if (new java.io.File(".env").exists()) {
                Dotenv dotenv = Dotenv.load();
                System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
                System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
                System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
                System.setProperty("DB_USER", dotenv.get("DB_USER"));
                System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
                System.setProperty("EM_HOST", dotenv.get("EM_HOST"));
                System.setProperty("EM_PORT", dotenv.get("EM_PORT"));
                System.setProperty("EM_USERNAME", dotenv.get("EM_USERNAME"));
                System.setProperty("EM_PASSWORD", dotenv.get("EM_PASSWORD"));
            }
        }
    }

}
