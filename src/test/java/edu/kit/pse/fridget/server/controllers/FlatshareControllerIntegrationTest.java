package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.models.commands.SaveFlatshareCommand;

import static org.assertj.core.api.Assertions.assertThat;

public class FlatshareControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String FLATSHARE_NAME = "An Awesome Flatshare";

    @Test
    public void getFlatshare_ReturnsCorrectResponse() {
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
    public void saveFlatshare_ReturnsCorrectResponse() throws Exception {
        ResponseEntity<Flatshare> response = getTestRestTemplate().postForEntity("/flatshares",
                getFixture("saveFlatshareCommand.json", SaveFlatshareCommand.class), Flatshare.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(flatshare -> {
            assertThat(flatshare.getId()).matches(UUID_PATTERN);
            assertThat(flatshare.getName()).isEqualTo("Another Awesome Flatshare");
        });
    }
}