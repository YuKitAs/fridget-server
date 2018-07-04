package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.models.FrozenNote;

import static org.assertj.core.api.Assertions.assertThat;

public class FrozenNoteControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String FROZEN_NOTE_ID_0 = "00000000-0000-0000-0000-000000000000";
    private static final String FROZEN_NOTE_ID_1 = "00000000-0000-0000-0000-000000000001";
    private static final String FROZEN_NOTE_ID_2 = "00000000-0000-0000-0000-000000000002";

    @Test
    public void getAllFrozenNotes() {
        ResponseEntity<FrozenNote[]> response = getTestRestTemplate().getForEntity(
                String.format("/frozen-notes?flatshare=%s", FLATSHARE_ID), FrozenNote[].class);
        FrozenNote[] frozenNotes = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(frozenNotes.length).isEqualTo(3);
        assertThat(frozenNotes[0].getId()).isEqualTo(FROZEN_NOTE_ID_0);
        assertThat(frozenNotes[1].getId()).isEqualTo(FROZEN_NOTE_ID_1);
        assertThat(frozenNotes[2].getId()).isEqualTo(FROZEN_NOTE_ID_2);
    }

    @Test
    public void getFrozenNote() {
        ResponseEntity<FrozenNote> response = getTestRestTemplate().getForEntity(String.format("/frozen-notes/%s", FROZEN_NOTE_ID_0),
                FrozenNote.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody().getId()).isEqualTo(FROZEN_NOTE_ID_0);
        assertThat(response.getBody().getFlatshareId()).isEqualTo(FLATSHARE_ID);
        assertThat(response.getBody().getTitle()).isEmpty();
        assertThat(response.getBody().getContent()).isEmpty();
        assertThat(response.getBody().getPosition()).isEqualTo(0);
    }

    @Test
    public void updateFrozenNote() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<FrozenNote> frozenNote = new HttpEntity<>(getFixture("frozenNote.json", FrozenNote.class), headers);
        ResponseEntity<FrozenNote> response = getTestRestTemplate().exchange(String.format("/frozen-notes/%s", FROZEN_NOTE_ID_0),
                HttpMethod.PUT, frozenNote, FrozenNote.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody().getTitle()).isEqualTo("Notfallkontakt");
        assertThat(response.getBody().getContent()).isEqualTo("Bonbon");
    }
}