package org.xmdf.homelabinventory.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface DeviceRepository extends CrudRepository<Device, UUID>, PagingAndSortingRepository<Device, UUID> {
}
