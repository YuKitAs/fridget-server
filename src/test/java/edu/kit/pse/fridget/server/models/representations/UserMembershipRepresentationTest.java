package edu.kit.pse.fridget.server.models.representations;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import edu.kit.pse.fridget.server.models.Membership;
import edu.kit.pse.fridget.server.models.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserMembershipRepresentationTest {
    @Test
    public void buildFromUserAndMembership() throws IOException {
        final String GOOGLE_NAME = "John Doe";

        User user = User.buildNew("valid-google-id-0", GOOGLE_NAME);
        Membership membership = new ObjectMapper().readValue(new File("src/test/resources/fixtures/membership.json"), Membership.class);

        UserMembershipRepresentation userMembershipRepresentation = UserMembershipRepresentation.buildFromUserAndMembership(user,
                membership);

        assertThat(userMembershipRepresentation.getMembershipId()).isEqualTo("00000000-0000-0000-0000-000000000000");
        assertThat(userMembershipRepresentation.getGoogleName()).isEqualTo(GOOGLE_NAME);
        assertThat(userMembershipRepresentation.getMagnetColor()).isEqualTo("0099cc");
    }
}