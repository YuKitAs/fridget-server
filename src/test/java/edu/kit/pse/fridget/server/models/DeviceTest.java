package edu.kit.pse.fridget.server.models;

import org.junit.Test;

import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceTest {
    private static final String ID = "00000000-0000-0000-0000-000000000000";
    private static final String USER_ID = "00000000-0000-0000-0000-000000000000";
    private static final String INSTANCE_ID_TOKEN = "valid-instance-id-token";

    @Test
    public void buildNew() {
        Device device = Device.buildNew(USER_ID, INSTANCE_ID_TOKEN);

        assertThat(device.getId()).matches(Pattern.UUID_PATTERN);
        assertThat(device.getUserId()).matches(USER_ID);
        assertThat(device.getInstanceIdToken()).matches(INSTANCE_ID_TOKEN);
    }

    @Test
    public void buildForUpdate() {
        Device device = Device.buildForUpdate(ID, USER_ID, INSTANCE_ID_TOKEN);

        assertThat(device.getId()).matches(ID);
        assertThat(device.getUserId()).matches(USER_ID);
        assertThat(device.getInstanceIdToken()).matches(INSTANCE_ID_TOKEN);
    }
}
