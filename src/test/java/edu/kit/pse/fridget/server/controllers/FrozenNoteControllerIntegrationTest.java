package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.exceptions.ExceptionResponseBody;
import edu.kit.pse.fridget.server.models.FrozenNote;

import static org.assertj.core.api.Assertions.assertThat;

public class FrozenNoteControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String FROZEN_NOTE_ID_0 = "00000000-0000-0000-0000-000000000000";
    private static final String FROZEN_NOTE_ID_1 = "00000000-0000-0000-0000-000000000001";
    private static final String FROZEN_NOTE_ID_2 = "00000000-0000-0000-0000-000000000002";
    private static final int FROZEN_NOTE_POSITION = 0;

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
    public void getAllFrozenNotes_WithIncorrectFlatshareId() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().getForEntity(
                String.format("/frozen-notes?flatshare=%s", "incorrect-flatshare-id"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getFrozenNote() {
        ResponseEntity<FrozenNote> response = getTestRestTemplate().getForEntity(String.format("/frozen-notes/%s", FROZEN_NOTE_ID_0),
                FrozenNote.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(frozenNote -> {
            assertThat(frozenNote.getId()).isEqualTo(FROZEN_NOTE_ID_0);
            assertThat(frozenNote.getFlatshareId()).isEqualTo(FLATSHARE_ID);
            assertThat(frozenNote.getTitle()).isEmpty();
            assertThat(frozenNote.getContent()).isEmpty();
            assertThat(frozenNote.getPosition()).isEqualTo(FROZEN_NOTE_POSITION);
        });
    }

    @Test
    public void getFrozenNote_WithIncorrectId() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().getForEntity(
                String.format("/frozen-notes/%s", "incorrect-id"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateFrozenNote() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<FrozenNote> response = getTestRestTemplate().exchange(String.format("/frozen-notes/%s", FROZEN_NOTE_ID_0),
                HttpMethod.PUT, new HttpEntity<>(getFixture("frozenNote.json", FrozenNote.class), headers), FrozenNote.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(frozenNote -> {
            assertThat(frozenNote.getTitle()).isEqualTo("Notfallkontakt");
            assertThat(frozenNote.getContent()).isEqualTo("Bonbon");
        });
    }

    @Test
    public void updateFrozenNote_WithIncorrectFlatshareId_ReturnsUnprocessableEntity() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().exchange(String.format("/frozen-notes/%s", FROZEN_NOTE_ID_0),
                HttpMethod.PUT, new HttpEntity<>(getFixture("frozenNoteWithIncorrectFlatshareId.json", FrozenNote.class), headers),
                ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void updateFrozenNote_WithIncorrectPosition_ReturnsConflict() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().exchange(String.format("/frozen-notes/%s", FROZEN_NOTE_ID_0),
                HttpMethod.PUT, new HttpEntity<>(getFixture("frozenNoteWithIncorrectPosition.json", FrozenNote.class), headers),
                ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}