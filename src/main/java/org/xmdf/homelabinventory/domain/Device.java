package org.xmdf.homelabinventory.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "hi_device")
public class Device {

    @Id
    private Long id;
    private String name;
    private String brand;

}
