package edu.kit.pse.fridget.server.controllers;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.kit.pse.fridget.server.exceptions.ExceptionResponseBody;
import edu.kit.pse.fridget.server.models.Device;
import edu.kit.pse.fridget.server.utilities.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final String DEVICE_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    public void saveDevice() throws Exception {
        ResponseEntity<Device> response = getTestRestTemplate().postForEntity("/devices", getFixture("device0.json", Device.class),
                Device.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(device -> {
            assertThat(device.getId()).matches(Pattern.UUID_PATTERN);
            assertThat(device.getUserId()).isEqualTo("00000000-0000-0000-0000-000000000002");
            assertThat(device.getInstanceIdToken()).isEqualTo("valid-instance-id-token-2");
        });
    }

    @Test
    public void saveDevice_WithIncorrectUserId() throws Exception {
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().postForEntity("/devices",
                getFixture("deviceWithIncorrectUserId0.json", Device.class), ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrorMessage()).isEqualTo(ENTITY_UNPROCESSABLE_ERROR_MESSAGE);
    }

    @Test
    public void updateDevice() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<Device> response = getTestRestTemplate().exchange(String.format("/devices/%s", DEVICE_ID), HttpMethod.PUT,
                new HttpEntity<>(getFixture("device1.json", Device.class), headers), Device.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON_UTF8)).isTrue();
        assertThat(response.getBody()).satisfies(device -> {
            assertThat(device.getId()).matches(Pattern.UUID_PATTERN);
            assertThat(device.getUserId()).isEqualTo("00000000-0000-0000-0000-000000000000");
            assertThat(device.getInstanceIdToken()).isEqualTo("new-valid-instance-id-token-0");
        });
    }

    @Test
    public void updateDevice_WithIncorrectUserId_ReturnsUnprocessableEntity() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<ExceptionResponseBody> response = getTestRestTemplate().exchange(String.format("/devices/%s", DEVICE_ID),
                HttpMethod.PUT, new HttpEntity<>(getFixture("deviceWithIncorrectUserId1.json", Device.class), headers),
                ExceptionResponseBody.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrorMessage()).isEqualTo(ENTITY_UNPROCESSABLE_ERROR_MESSAGE);
    }
}
