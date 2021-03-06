package edu.kit.pse.fridget.server.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.Device;
import edu.kit.pse.fridget.server.repositories.DeviceRepository;
import edu.kit.pse.fridget.server.repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class DeviceServiceTest extends AbstractServiceTest {
    private static final String DEVICE_ID = "00000000-0000-0000-0000-000000000000";
    @InjectMocks
    private DeviceServiceImpl deviceService;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        when(userRepository.findById(INCORRECT_USER_ID)).thenReturn(Optional.empty());
    }

    @Test
    public void saveDevice_WithIncorrectUserId() {
        assertThatThrownBy(() -> deviceService.saveDevice(getFixture("deviceForSaveWithIncorrectUserId.json", Device.class))).isInstanceOf(
                EntityUnprocessableException.class).hasMessage(ENTITY_UNPROCESSABLE_ERROR_MESSAGE);
    }

    @Test
    public void updateDevice_WithIncorrectId() {
        assertThatThrownBy(
                () -> deviceService.updateDevice("incorrect-device-id", getFixture("deviceForUpdate.json", Device.class))).isInstanceOf(
                EntityUnprocessableException.class).hasMessage(ENTITY_UNPROCESSABLE_ERROR_MESSAGE);

        assertThatThrownBy(
                () -> deviceService.updateDevice(DEVICE_ID, getFixture("deviceForUpdateWithIncorrectId.json", Device.class))).isInstanceOf(
                EntityUnprocessableException.class).hasMessage(ENTITY_UNPROCESSABLE_ERROR_MESSAGE);
    }

    @Test
    public void updateDevice_WithIncorrectUserId() {
        assertThatThrownBy(() -> deviceService.updateDevice(DEVICE_ID, getFixture("deviceForUpdateWithIncorrectUserId.json", Device.class)))
                .isInstanceOf(EntityUnprocessableException.class)
                .hasMessage(ENTITY_UNPROCESSABLE_ERROR_MESSAGE);
    }
}
