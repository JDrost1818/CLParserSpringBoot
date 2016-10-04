package github.jdrost1818;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
@EnableOAuth2Client
public class ClparserServiceApplication {

    public static void main(String... args) {
        SpringApplication.run(ClparserServiceApplication.class, args);
    }

}
