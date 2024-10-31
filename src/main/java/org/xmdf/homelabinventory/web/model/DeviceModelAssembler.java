package org.xmdf.homelabinventory.web.model;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.xmdf.homelabinventory.domain.Device;
import org.xmdf.homelabinventory.web.controller.DeviceController;

@Component
public class DeviceModelAssembler extends RepresentationModelAssemblerSupport<Device, DeviceModel> {

    public DeviceModelAssembler() {
        super(DeviceController.class, DeviceModel.class);
    }

    @Override
    public DeviceModel toModel(@NonNull Device entity) {
        DeviceModel model = createModelWithId(entity.getId(), entity);
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setBrand(entity.getBrand());
        return model;
    }
}