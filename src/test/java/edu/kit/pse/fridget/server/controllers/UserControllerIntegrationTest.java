package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import edu.kit.pse.fridget.server.models.User;
import edu.kit.pse.fridget.server.models.representations.UserWithJwtRepresentation;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Test
    public void registerOrLogin_WithIdToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> nameValuePairs = new LinkedMultiValueMap<>(1);
        nameValuePairs.add("idToken", "a-google-id-token");

        ResponseEntity<UserWithJwtRepresentation> response = getTestRestTemplate().postForEntity("/users/sign-in",
                new HttpEntity<>(nameValuePairs, headers), UserWithJwtRepresentation.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(userWithJwtRepresentation -> {
            assertThat(userWithJwtRepresentation.getUser().getId()).matches(Pattern.UUID_PATTERN);
            assertThat(userWithJwtRepresentation.getUser().getGoogleUserId()).isEqualTo("a-valid-google-id");
            assertThat(userWithJwtRepresentation.getUser().getGoogleName()).isEqualTo("Dummy User");
            assertThat(userWithJwtRepresentation.getJwt()).isNotBlank();
        });
    }

    @Test
    public void registerOrLogin() throws Exception {
        ResponseEntity<UserWithJwtRepresentation> response = getTestRestTemplate().postForEntity("/users",
                getFixture("user.json", User.class), UserWithJwtRepresentation.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(userWithJwtRepresentation -> {
            assertThat(userWithJwtRepresentation.getUser().getId()).matches(Pattern.UUID_PATTERN);
            assertThat(userWithJwtRepresentation.getUser().getGoogleUserId()).isEqualTo("a-valid-google-id");
            assertThat(userWithJwtRepresentation.getUser().getGoogleName()).isEqualTo("Dummy User");
            assertThat(userWithJwtRepresentation.getJwt()).isNotBlank();
        });
    }
}