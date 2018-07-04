package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.models.AccessCode;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessCodeControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Test
    public void generateAccessCode() throws Exception {
        AccessCode accessCode = getFixture("accessCode.json", AccessCode.class);
        ResponseEntity<AccessCode> response = getTestRestTemplate().postForEntity("/access-codes", accessCode, AccessCode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(body -> {
            assertThat(body.getId()).matches(UUID_PATTERN);
            assertThat(body.getFlatshareId()).isEqualTo("00000000-0000-0000-0000-000000000000");
            assertThat(body.getContent()).matches("[0-9a-zA-Z]{5}");
        });
    }
}