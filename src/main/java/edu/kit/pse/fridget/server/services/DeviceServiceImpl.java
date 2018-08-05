package edu.kit.pse.fridget.server.services;

import edu.kit.pse.fridget.server.exceptions.EntityNotFoundException;
import edu.kit.pse.fridget.server.models.Device;
import edu.kit.pse.fridget.server.repositories.DeviceRepository;
import edu.kit.pse.fridget.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Device saveDevice(Device device) {
        String userId = device.getUserId();
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));

        return deviceRepository.save(Device.buildNew(userId, device.getInstanceIdToken()));
    }
}
