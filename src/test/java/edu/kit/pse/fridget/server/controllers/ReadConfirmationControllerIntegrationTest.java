package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.exceptions.ExceptionResponseBody;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.ReadConfirmation;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadConfirmationControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String COOL_NOTE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String MEMBERSHIP_ID = "00000000-0000-0000-0000-000000000001";
    private static final String MAGNET_COLOR = "ffffff";

    @Test
    public void getAllMemberships() {
        ResponseEntity<Membership[]> response = getTestRestTemplate().getForEntity(
                String.format("/read-confirmations/users?cool-note=%s", COOL_NOTE_ID), Membership[].class);
        Membership[] memberships = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(memberships.length).isEqualTo(1);
        assertThat(memberships[0]).satisfies(membership -> {
            assertThat(membership.getId()).isEqualTo(MEMBERSHIP_ID);
            assertThat(membership.getFlatshareId()).matches(Pattern.UUID_PATTERN);
            assertThat(membership.getUserId()).matches(Pattern.UUID_PATTERN);
            assertThat(membership.getMagnetColor()).isEqualTo(MAGNET_COLOR);
        });
    }

    @Test
    public void getAllMemberships_WithIncorrectCoolNoteId() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().getForEntity(
                String.format("/read-confirmations/users?cool-note=%s", "incorrect-cool-note-id"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void saveReadConfirmation() throws Exception {
        ResponseEntity<ReadConfirmation> response = getTestRestTemplate().postForEntity("/read-confirmations",
                getFixture("readConfirmation.json", ReadConfirmation.class), ReadConfirmation.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(readConfirmation -> {
            assertThat(readConfirmation.getId()).matches(Pattern.UUID_PATTERN);
            assertThat(readConfirmation.getCoolNoteId()).isEqualTo(COOL_NOTE_ID);
            assertThat(readConfirmation.getMembershipId()).isEqualTo("00000000-0000-0000-0000-000000000002");
        });
    }

    @Test
    public void saveReadConfirmation_WithIncorrectMembershipId_ReturnsUnprocessableEntity() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().postForEntity("/read-confirmations",
                ReadConfirmation.buildNew("incorrect-membership-id", COOL_NOTE_ID), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void saveReadConfirmation_WithIncorrectCoolNoteId_ReturnsUnprocessableEntity() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().postForEntity("/read-confirmations",
                ReadConfirmation.buildNew("00000000-0000-0000-0000-000000000002", "incorrect-cool-note-id"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void saveReadConfirmation_WithExistedCoolNoteIdAndMembershipId() throws Exception {
        String readConfirmationId = getTestRestTemplate().postForEntity("/read-confirmations",
                getFixture("readConfirmation.json", ReadConfirmation.class), ReadConfirmation.class).getBody().getId();

        ResponseEntity<ReadConfirmation> response = getTestRestTemplate().postForEntity("/read-confirmations",
                getFixture("readConfirmation.json", ReadConfirmation.class), ReadConfirmation.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isEqualTo(readConfirmationId);
    }

    @Test
    public void deleteReadConfirmation() {
        ResponseEntity<Void> response = getTestRestTemplate().exchange(
                String.format("/read-confirmations?cool-note=%s&membership=%s", COOL_NOTE_ID, MEMBERSHIP_ID), HttpMethod.DELETE, null,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void deleteReadConfirmation_WithIncorrectCoolNoteId_ReturnsConflict() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().exchange(
                String.format("/read-confirmations?cool-note=%s&membership=%s", "incorrect-cool-note-id", MEMBERSHIP_ID), HttpMethod.DELETE,
                null, ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteReadConfirmation_WithIncorrectMembershipId_ReturnsConflict() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().exchange(
                String.format("/read-confirmations?cool-note=%s&membership=%s", COOL_NOTE_ID, "incorrect-membership-id"), HttpMethod.DELETE,
                null, ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}