package edu.kit.pse.fridget.server.repositories;

import edu.kit.pse.fridget.server.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String> {
    Optional<Device> findByUserId(String userId);
}
