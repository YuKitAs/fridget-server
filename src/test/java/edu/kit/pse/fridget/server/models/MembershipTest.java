package edu.kit.pse.fridget.server.models;

import org.junit.Test;

import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class MembershipTest {
    private static final String FLATSHARE_ID = "00000000-0000-0000-0000-000000000000";
    private static final String USER_ID = "00000000-0000-0000-0000-000000000003";
    private static final String MAGNET_COLOR = "0000ff";

    @Test
    public void build() {
        Membership membership = new Membership.Builder().setRandomId()
                .setFlatshareId(FLATSHARE_ID)
                .setUserId(USER_ID)
                .setMagnetColor(MAGNET_COLOR)
                .build();

        assertThat(membership.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(membership.getFlatshareId()).isEqualTo(FLATSHARE_ID);
        assertThat(membership.getUserId()).isEqualTo(USER_ID);
        assertThat(membership.getMagnetColor()).isEqualTo(MAGNET_COLOR);
    }
}