package edu.kit.pse.fridget.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kit.pse.fridget.server.exceptions.EntityUnprocessableException;
import edu.kit.pse.fridget.server.models.Device;
import edu.kit.pse.fridget.server.repositories.DeviceRepository;
import edu.kit.pse.fridget.server.repositories.UserRepository;

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
        String instanceIdToken = device.getInstanceIdToken();

        userRepository.findById(userId).orElseThrow(EntityUnprocessableException::new);

        return deviceRepository.findByUserId(userId)
                .map(dev -> deviceRepository.save(Device.buildForUpdate(dev.getId(), userId, instanceIdToken)))
                .orElseGet(() -> deviceRepository.save(Device.buildNew(userId, instanceIdToken)));
    }

    @Override
    public Device updateDevice(String id, Device device) {
        deviceRepository.findById(id).orElseThrow(EntityUnprocessableException::new);

        if (!id.equals(device.getId()) || !deviceRepository.findById(id).get().getUserId().equals(device.getUserId())) {
            throw new EntityUnprocessableException();
        }

        return deviceRepository.save(device);
    }
}
