package org.xmdf.homelabinventory.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Relation(itemRelation = "device", collectionRelation = "devices")
public class DeviceModel extends RepresentationModel<DeviceModel> {

    private UUID id;
    private String name;
    private String brand;
}
