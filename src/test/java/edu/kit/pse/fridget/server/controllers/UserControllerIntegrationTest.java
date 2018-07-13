package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.models.representations.UserWithJwtRepresentation;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Test
    public void registerOrLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Id-Token", "google-id-token");
        ResponseEntity<UserWithJwtRepresentation> response = getTestRestTemplate().postForEntity("/users", new HttpEntity<>(null, headers),
                UserWithJwtRepresentation.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(userWithJwtRepresentation -> {
            assertThat(userWithJwtRepresentation.getUser().getId()).matches(Pattern.UUID_PATTERN);
            assertThat(userWithJwtRepresentation.getUser().getGoogleUserId()).isEqualTo("valid-google-id");
            assertThat(userWithJwtRepresentation.getUser().getGoogleName()).isEqualTo("Dummy User");
            assertThat(userWithJwtRepresentation.getJwt()).isNotBlank();
        });
    }
}