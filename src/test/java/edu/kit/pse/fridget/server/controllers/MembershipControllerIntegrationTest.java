package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.commands.SaveMembershipCommand;
import edu.kit.pse.fridget.server.models.representations.UserMembershipRepresentation;

import static org.assertj.core.api.Assertions.assertThat;

public class MembershipControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String USER_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    public void getAllMembers() {
        ResponseEntity<UserMembershipRepresentation[]> response = getTestRestTemplate().getForEntity(
                String.format("/memberships/users?flatshare=%s", FLATSHARE_ID), UserMembershipRepresentation[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();

        UserMembershipRepresentation[] userMembershipRepresentations = response.getBody();
        assertThat(userMembershipRepresentations.length).isEqualTo(2);
        assertThat(userMembershipRepresentations[0]).satisfies(representation -> {
            assertThat(representation.getMembershipId()).matches(UUID_PATTERN);
            assertThat(representation.getMagnetColor()).matches("0099cc");
            assertThat(representation.getGoogleName()).isEqualTo("John Doe");
        });
    }

    @Test
    public void getMember() {
        ResponseEntity<UserMembershipRepresentation> response = getTestRestTemplate().getForEntity(
                String.format("/memberships?flatshare=%s&user=%s", FLATSHARE_ID, USER_ID), UserMembershipRepresentation.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(representation -> {
            assertThat(representation.getMembershipId()).matches(UUID_PATTERN);
            assertThat(representation.getMagnetColor()).matches("0099cc");
            assertThat(representation.getGoogleName()).isEqualTo("John Doe");
        });
    }

    @Test
    public void saveMembership() throws Exception {
        SaveMembershipCommand saveMembershipCommand = getFixture("saveMembershipCommand.json", SaveMembershipCommand.class);
        ResponseEntity<Membership> response = getTestRestTemplate().postForEntity("/memberships", saveMembershipCommand, Membership.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(body -> {
            assertThat(body.getId()).matches(UUID_PATTERN);
            assertThat(body.getFlatshareId()).isEqualTo("00000000-0000-0000-0000-000000000000");
            assertThat(body.getUserId()).isEqualTo("00000000-0000-0000-0000-000000000002");
            assertThat(body.getMagnetColor()).matches("[0-9a-f]{6}");
        });
    }

    @Test
    public void deleteMembership() {
        ResponseEntity<Void> response = getTestRestTemplate().exchange(
                String.format("/memberships?flatshare=%s&user=%s", FLATSHARE_ID, USER_ID), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }
}