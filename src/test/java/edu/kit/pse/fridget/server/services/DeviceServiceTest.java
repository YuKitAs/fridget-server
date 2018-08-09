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
        assertThatThrownBy(() -> deviceService.saveDevice(getFixture("deviceWithIncorrectUserId0.json", Device.class))).isInstanceOf(
                EntityUnprocessableException.class);
    }

    @Test
    public void updateDevice_WithIncorrectUserId() {
        assertThatThrownBy(() -> deviceService.updateDevice("00000000-0000-0000-0000-000000000000",
                getFixture("deviceWithIncorrectUserId1.json", Device.class))).isInstanceOf(EntityUnprocessableException.class);
    }
}
