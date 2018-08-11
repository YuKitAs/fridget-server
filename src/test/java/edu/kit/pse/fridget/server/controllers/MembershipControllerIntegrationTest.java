package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.exceptions.ExceptionResponseBody;
import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.commands.SaveMembershipCommand;
import edu.kit.pse.fridget.server.models.representations.UserMembershipRepresentation;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class MembershipControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String USER_ID = "00000000-0000-0000-0000-000000000000";
    private static final String MAGNET_COLOR = "0099cc";
    private static final String GOOGLE_NAME = "John Doe";
    private static final String MEMBERSHIP_CONFLICT_ERROR_MESSAGE = "Membership cannot be deleted, it does not exist.";

    @Test
    public void getAllMembers() {
        ResponseEntity<UserMembershipRepresentation[]> response = getTestRestTemplate().getForEntity(
                String.format("/memberships/users?flatshare=%s", FLATSHARE_ID), UserMembershipRepresentation[].class);
        UserMembershipRepresentation[] userMembershipRepresentations = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(userMembershipRepresentations.length).isEqualTo(3);
        assertThat(userMembershipRepresentations[0]).satisfies(representation -> {
            assertThat(representation.getMembershipId()).matches(Pattern.UUID_PATTERN);
            assertThat(representation.getMagnetColor()).matches(MAGNET_COLOR);
            assertThat(representation.getGoogleName()).isEqualTo(GOOGLE_NAME);
        });
    }

    @Test
    public void getAllMembers_WithIncorrectFlatshareId() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().getForEntity(
                String.format("/memberships/users?flatshare=%s", "incorrect-flatshare-id"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Memberships not found.");
    }

    @Test
    public void getMember() {
        ResponseEntity<UserMembershipRepresentation> response = getTestRestTemplate().getForEntity(
                String.format("/memberships?flatshare=%s&user=%s", FLATSHARE_ID, USER_ID), UserMembershipRepresentation.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(representation -> {
            assertThat(representation.getMembershipId()).matches(Pattern.UUID_PATTERN);
            assertThat(representation.getMagnetColor()).matches(MAGNET_COLOR);
            assertThat(representation.getGoogleName()).isEqualTo(GOOGLE_NAME);
        });
    }

    @Test
    public void getMember_WithIncorrectUserId() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().getForEntity(
                String.format("/memberships?flatshare=%s&user=%s", FLATSHARE_ID, "incorrect-user-id"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorMessage()).isEqualTo("User id=\"incorrect-user-id\" not found.");
    }

    @Test
    public void getMember_WithIncorrectFlatshareId() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().getForEntity(
                String.format("/memberships?flatshare=%s&user=%s", "incorrect-flatshare-id", USER_ID), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Membership not found.");
    }

    @Test
    public void saveMembership_WithAccessCodeDeleted() throws Exception {
        ResponseEntity<Membership> response = getTestRestTemplate().postForEntity("/memberships",
                getFixture("saveMembershipCommand0.json", SaveMembershipCommand.class), Membership.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(representation -> {
            assertThat(representation.getId()).matches(Pattern.UUID_PATTERN);
            assertThat(representation.getFlatshareId()).isEqualTo("00000000-0000-0000-0000-000000000000");
            assertThat(representation.getUserId()).isEqualTo("00000000-0000-0000-0000-000000000002");
            assertThat(representation.getMagnetColor()).matches(Pattern.HEX_COLOR_CODE_PATTERN);
        });

        assertThat(
                getTestRestTemplate().postForEntity("/memberships", getFixture("saveMembershipCommand0.json", SaveMembershipCommand.class),
                        Membership.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void saveMembership_WithIncorrectAccessCode_ReturnsNotFound() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().postForEntity("/memberships",
                new SaveMembershipCommand("12345", "00000000-0000-0000-0000-000000000002"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Access code 12345 not found.");
    }

    @Test
    public void saveMembership_WithIncorrectUserId_ReturnsUnprocessableEntity() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().postForEntity("/memberships",
                new SaveMembershipCommand("abc42", "incorrect-user-id"), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrorMessage()).isEqualTo(ENTITY_UNPROCESSABLE_ERROR_MESSAGE);
    }

    @Test
    public void saveMembership_WithExistedUserId_ReturnsConflict() throws Exception {
        getTestRestTemplate().postForEntity("/memberships", getFixture("saveMembershipCommand0.json", SaveMembershipCommand.class),
                Membership.class);

        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().postForEntity("/memberships",
                getFixture("saveMembershipCommand1.json", SaveMembershipCommand.class), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Membership already exists.");
    }

    @Test
    public void deleteMembership() {
        ResponseEntity<Void> response = getTestRestTemplate().exchange(
                String.format("/memberships?flatshare=%s&user=%s", FLATSHARE_ID, USER_ID), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void deleteMembership_WithIncorrectFlatshareId_ReturnsConflict() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().exchange(
                String.format("/memberships?flatshare=%s&user=%s", "incorrect-flatshare-id", USER_ID), HttpMethod.DELETE, null,
                ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getErrorMessage()).isEqualTo(MEMBERSHIP_CONFLICT_ERROR_MESSAGE);
    }

    @Test
    public void deleteMembership_WithIncorrectUserId_ReturnsConflict() {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().exchange(
                String.format("/memberships?flatshare=%s&user=%s", FLATSHARE_ID, "incorrect-user-id"), HttpMethod.DELETE, null,
                ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getErrorMessage()).isEqualTo(MEMBERSHIP_CONFLICT_ERROR_MESSAGE);
    }
}