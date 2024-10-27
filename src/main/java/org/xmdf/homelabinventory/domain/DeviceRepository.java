package org.xmdf.homelabinventory.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeviceRepository extends CrudRepository<Device, Long>, PagingAndSortingRepository<Device, Long> {
}
