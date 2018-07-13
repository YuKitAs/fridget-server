package edu.kit.pse.fridget.server.models;

import org.junit.Test;

import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    private static final String GOOGLE_USER_ID = "valid-google-id-0";
    private static final String GOOGLE_NAME = "John Doe";

    @Test
    public void buildNew() {
        User user = User.buildNew(GOOGLE_USER_ID, GOOGLE_NAME);

        assertThat(user.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(user.getGoogleUserId()).isEqualTo(GOOGLE_USER_ID);
        assertThat(user.getGoogleName()).isEqualTo(GOOGLE_NAME);
    }
}