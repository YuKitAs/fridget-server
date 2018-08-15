package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.exceptions.ExceptionResponseBody;
import edu.kit.pse.fridget.server.models.AccessCode;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessCodeControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Test
    public void generateAccessCode() throws Exception {
        ResponseEntity<AccessCode> response = getTestRestTemplate().postForEntity("/access-codes",
                getFixture("accessCode.json", AccessCode.class), AccessCode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(accessCode -> {
            assertThat(accessCode.getId()).matches(Pattern.UUID_PATTERN);
            assertThat(accessCode.getFlatshareId()).isEqualTo(FLATSHARE_ID);
            assertThat(accessCode.getContent()).matches(Pattern.ACCESS_CODE_PATTERN);
        });
    }

    @Test
    public void generateAccessCode_WithIncorrectFlatshareId_ReturnsUnprocessableEntity() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().postForEntity("/access-codes",
                AccessCode.buildNew(null, "incorrect-flatshare-id"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}