package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

import edu.kit.pse.fridget.server.exceptions.ExceptionResponseBody;
import edu.kit.pse.fridget.server.models.CoolNote;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class CoolNoteControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String COOL_NOTE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String TITLE = "Hello";
    private static final String CONTENT = "Hello world!";
    private static final String CREATOR_MEMBERSHIP_ID = "00000000-0000-0000-0000-000000000000";
    private static final int IMPORTANCE = 0;
    private static final int POSITION = 0;
    private static final Instant CREATED_AT = LocalDateTime.of(2018, Month.JULY, 1, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant();

    @Test
    public void getAllCoolNotes() {
        ResponseEntity<CoolNote[]> response = getTestRestTemplate().getForEntity(String.format("/cool-notes?flatshare=%s", FLATSHARE_ID),
                CoolNote[].class);
        CoolNote[] coolNotes = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(coolNotes.length).isEqualTo(1);
        assertThat(coolNotes[0]).satisfies(coolNote -> {
            assertThat(coolNote.getId()).isEqualTo(COOL_NOTE_ID);
            assertThat(coolNote.getTitle()).isEqualTo(TITLE);
            assertThat(coolNote.getContent()).isEqualTo(CONTENT);
            assertThat(coolNote.getCreatorMembershipId()).isEqualTo(CREATOR_MEMBERSHIP_ID);
            assertThat(coolNote.getImportance()).isEqualTo(IMPORTANCE);
            assertThat(coolNote.getPosition()).isEqualTo(POSITION);
            assertThat(coolNote.getCreatedAt()).isEqualTo(CREATED_AT);
        });
    }

    @Test
    public void getAllCoolNotes_WithIncorrectFlatshareId() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().getForEntity(
                String.format("/cool-notes?flatshare=%s", "incorrect-flatshare-id"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Cool Notes not found.");
    }

    @Test
    public void getCoolNote() {
        ResponseEntity<CoolNote> response = getTestRestTemplate().getForEntity(String.format("/cool-notes/%s", COOL_NOTE_ID),
                CoolNote.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(coolNote -> {
            assertThat(coolNote.getId()).isEqualTo(COOL_NOTE_ID);
            assertThat(coolNote.getTitle()).isEqualTo(TITLE);
            assertThat(coolNote.getContent()).isEqualTo(CONTENT);
            assertThat(coolNote.getCreatorMembershipId()).isEqualTo(CREATOR_MEMBERSHIP_ID);
            assertThat(coolNote.getImportance()).isEqualTo(IMPORTANCE);
            assertThat(coolNote.getPosition()).isEqualTo(POSITION);
            assertThat(coolNote.getCreatedAt()).isEqualTo(CREATED_AT);
        });
    }

    @Test
    public void getCoolNote_WithIncorrectId() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().getForEntity(String.format("/cool-notes/%s", "incorrect-id"),
                ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Cool Note id=\"incorrect-id\" not found.");
    }

    @Test
    public void saveCoolNote() throws Exception {
        ResponseEntity<CoolNote> response = getTestRestTemplate().postForEntity("/cool-notes", getFixture("coolNote.json", CoolNote.class),
                CoolNote.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(coolNote -> {
            assertThat(coolNote.getId()).matches(Pattern.UUID_PATTERN);
            assertThat(coolNote.getTitle()).isEqualTo("Goodbye");
            assertThat(coolNote.getContent()).isEqualTo("Goodbye world!");
            assertThat(coolNote.getImportance()).isEqualTo(0);
            assertThat(coolNote.getPosition()).isEqualTo(0);
            assertThat(coolNote.getCreatedAt()).isNotNull();
        });
    }

    @Test
    public void deleteCoolNote() {
        ResponseEntity<Void> response = getTestRestTemplate().exchange(String.format("/cool-notes/%s", COOL_NOTE_ID), HttpMethod.DELETE,
                null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }
}