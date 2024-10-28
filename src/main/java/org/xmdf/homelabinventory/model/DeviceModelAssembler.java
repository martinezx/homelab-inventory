package org.xmdf.homelabinventory.model;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import org.xmdf.homelabinventory.domain.Device;
import org.xmdf.homelabinventory.web.DeviceController;

@Component
public class DeviceModelAssembler extends RepresentationModelAssemblerSupport<Device, DeviceModel> {

    public DeviceModelAssembler() {
        super(DeviceController.class, DeviceModel.class);
    }

    @Override
    public DeviceModel toModel(Device entity) {
        DeviceModel model = createModelWithId(entity.getId(), entity);
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setBrand(entity.getBrand());
        return model;
    }
}
