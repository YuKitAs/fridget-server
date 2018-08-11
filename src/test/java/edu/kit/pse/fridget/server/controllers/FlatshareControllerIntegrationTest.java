package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.exceptions.ExceptionResponseBody;
import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.models.commands.SaveFlatshareCommand;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class FlatshareControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String FLATSHARE_NAME = "An Awesome Flatshare";

    @Test
    public void getFlatshare() {
        ResponseEntity<Flatshare> response = getTestRestTemplate().getForEntity(String.format("/flatshares/%s", FLATSHARE_ID),
                Flatshare.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(flatshare -> {
            assertThat(flatshare.getId()).isEqualTo(FLATSHARE_ID);
            assertThat(flatshare.getName()).isEqualTo(FLATSHARE_NAME);
        });
    }

    @Test
    public void getFlatshre_WithIncorrectId_ReturnsNotFound() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().getForEntity(String.format("/flatshares/%s", "incorrect-id"),
                ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Flatshare id=\"incorrect-id\" not found.");
    }

    @Test
    public void saveFlatshare() throws Exception {
        ResponseEntity<Flatshare> response = getTestRestTemplate().postForEntity("/flatshares",
                getFixture("saveFlatshareCommand.json", SaveFlatshareCommand.class), Flatshare.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(flatshare -> {
            assertThat(flatshare.getId()).matches(Pattern.UUID_PATTERN);
            assertThat(flatshare.getName()).isEqualTo("Another Awesome Flatshare");
        });
    }

    @Test
    public void saveFlatshare_WithIncorrectUserId_ReturnsUnprocessableEntity() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().postForEntity("/flatshares",
                new SaveFlatshareCommand("incorrect-user-id", FLATSHARE_NAME), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrorMessage()).isEqualTo(ENTITY_UNPROCESSABLE_ERROR_MESSAGE);
    }
}