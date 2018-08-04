package edu.kit.pse.fridget.server.services;

import edu.kit.pse.fridget.server.models.Device;
import edu.kit.pse.fridget.server.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository repository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Device saveDevice(Device device) {
        return repository.save(Device.buildNew(device.getUserId(), device.getInstanceIdToken()));
    }
}
