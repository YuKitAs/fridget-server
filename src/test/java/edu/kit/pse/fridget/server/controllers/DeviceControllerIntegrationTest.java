package edu.kit.pse.fridget.server.controllers;

import edu.kit.pse.fridget.server.exceptions.ExceptionResponseBody;
import edu.kit.pse.fridget.server.models.Device;
import edu.kit.pse.fridget.server.utilities.Pattern;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceControllerIntegrationTest extends AbstractControllerIntegrationTest {
    @Test
    public void saveDevice() throws Exception {
        ResponseEntity<Device> response = getTestRestTemplate().postForEntity("/devices", getFixture
                ("device.json", Device.class), Device.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(device -> {
            assertThat(device.getId()).matches(Pattern.UUID_PATTERN);
            assertThat(device.getUserId()).isEqualTo("00000000-0000-0000-0000-000000000002");
            assertThat(device.getInstanceIdToken()).isEqualTo("valid-instance-id-token-2");
        });
    }

    @Test
    public void saveDevicd_WithIncorrectUserId() throws Exception {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().postForEntity("/devices", getFixture
                ("deviceWithIncorrectUserId.json", Device.class), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorMessage()).isEqualTo("User id=\"incorrect-user-id\" not found.");
    }
}
