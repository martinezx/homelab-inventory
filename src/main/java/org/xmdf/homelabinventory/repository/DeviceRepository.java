package org.xmdf.homelabinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmdf.homelabinventory.domain.Device;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
