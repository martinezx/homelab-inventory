package org.xmdf.homelabinventory.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.xmdf.homelabinventory.domain.Device;
import org.xmdf.homelabinventory.domain.DeviceRepository;
import org.xmdf.homelabinventory.model.DeviceModel;
import org.xmdf.homelabinventory.model.DeviceModelAssembler;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceRepository deviceRepository;
    private final DeviceModelAssembler deviceModelAssembler;
    private final PagedResourcesAssembler<Device> pagedResourcesAssembler;

    public DeviceController(
            DeviceRepository deviceRepository,
            DeviceModelAssembler deviceModelAssembler,
            PagedResourcesAssembler<Device> pagedResourcesAssembler) {

        this.deviceRepository = deviceRepository;
        this.deviceModelAssembler = deviceModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<DeviceModel>> findAll(Pageable pageable) {
        Page<Device> page = deviceRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));

        PagedModel<DeviceModel> pagedModel = pagedResourcesAssembler
                .toModel(page, deviceModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<DeviceModel> findById(@PathVariable Long requestedId) {
        Optional<Device> device = deviceRepository.findById(requestedId);
        DeviceModel deviceModel = device.map(deviceModelAssembler::toModel).orElse(null);

        return deviceModel != null
                ? ResponseEntity.ok(deviceModel)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createDevice(@RequestBody Device newDeviceRequest, UriComponentsBuilder ucb) {
        Device device = Device.builder()
                .name(newDeviceRequest.getName())
                .brand(newDeviceRequest.getBrand())
                .build();
        Device savedDevice = deviceRepository.save(device);

        URI locationOfNewDevice = ucb
                .path("devices/{id}")
                .buildAndExpand(savedDevice.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewDevice).build();
    }

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putDevice(@PathVariable Long requestedId, @RequestBody Device deviceUpdate) {
        boolean deviceExists = deviceRepository.existsById(requestedId);

        if (!deviceExists) {
            return ResponseEntity.notFound().build();
        }

        deviceUpdate.setId(requestedId);
        deviceRepository.save(deviceUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
