package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.ReadConfirmation;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadConfirmationControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String COOL_NOTE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String MEMBERSHIP_ID = "00000000-0000-0000-0000-000000000001";
    private static final String MAGNET_COLOR = "ffffff";
    private static final String READ_CONFIRMATION_ID = "00000000-0000-0000-0000-000000000000";

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
            assertThat(membership.getFlatshareId()).matches(UUID_PATTERN);
            assertThat(membership.getUserId()).matches(UUID_PATTERN);
            assertThat(membership.getMagnetColor()).isEqualTo(MAGNET_COLOR);
        });
    }

    @Test
    public void saveReadConfirmation() throws Exception {
        ResponseEntity<ReadConfirmation> response = getTestRestTemplate().postForEntity("/read-confirmations",
                getFixture("readConfirmation.json", ReadConfirmation.class), ReadConfirmation.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(readConfirmation -> {
            assertThat(readConfirmation.getId()).matches(UUID_PATTERN);
            assertThat(readConfirmation.getCoolNoteId()).isEqualTo(COOL_NOTE_ID);
            assertThat(readConfirmation.getMembershipId()).isEqualTo("00000000-0000-0000-0000-000000000002");
        });
    }

    @Test
    public void deleteReadConfirmation() {
        ResponseEntity<Void> response = getTestRestTemplate().exchange(
                String.format("/read-confirmations?cool-note=%s&membership=%s", COOL_NOTE_ID, MEMBERSHIP_ID), HttpMethod.DELETE, null,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }
}