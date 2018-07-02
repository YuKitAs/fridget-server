package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.models.Flatshare;
import edu.kit.pse.fridget.server.models.commands.SaveFlatshareCommand;

import static org.assertj.core.api.Assertions.assertThat;

public class FlatshareControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    public void getFlatshare_ReturnsCorrectResponse() {
        ResponseEntity<Flatshare> response = getTestRestTemplate().getForEntity(String.format("/flatshares/%s", FLATSHARE_ID),
                Flatshare.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).satisfies(body -> {
            assertThat(body.getId()).isEqualTo(FLATSHARE_ID);
            assertThat(body.getName()).isEqualTo("An Awesome Flatshare");
        });
    }

    @Test
    public void saveFlatshare_ReturnsCorrectResponse() throws Exception {
        SaveFlatshareCommand saveFlatshareCommand = getFixture("saveFlatshareCommand.json", SaveFlatshareCommand.class);
        ResponseEntity<Flatshare> response = getTestRestTemplate().postForEntity("/flatshares", saveFlatshareCommand, Flatshare.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).satisfies(body -> {
            assertThat(body.getId()).matches(UUID_PATTERN);
            assertThat(body.getName()).isEqualTo("Another Awesome Flatshare");
        });
    }
}