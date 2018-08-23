package edu.kit.pse.fridget.server.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import edu.kit.pse.fridget.server.Application;
import edu.kit.pse.fridget.server.services.AuthenticationService;
import edu.kit.pse.fridget.server.services.AuthenticationServiceStub;

@Configuration
@Import(Application.class)
public class AuthenticationServiceConfiguration {
    @Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationServiceStub();
    }
}
