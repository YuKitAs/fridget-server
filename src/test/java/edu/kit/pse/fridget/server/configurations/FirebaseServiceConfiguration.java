package edu.kit.pse.fridget.server.configurations;

import edu.kit.pse.fridget.server.Application;
import edu.kit.pse.fridget.server.services.FirebaseService;
import edu.kit.pse.fridget.server.services.FirebaseServiceMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Application.class)
public class FirebaseServiceConfiguration {
    @Bean
    public FirebaseService firebaseService() {
        return new FirebaseServiceMock();
    }
}
