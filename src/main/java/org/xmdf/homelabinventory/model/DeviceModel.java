package org.xmdf.homelabinventory.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Relation(itemRelation = "device", collectionRelation = "devices")
public class DeviceModel extends RepresentationModel<DeviceModel> {

    private Long id;
    private String name;
    private String brand;
}
