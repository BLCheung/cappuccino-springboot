package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
public class UserAddress extends BaseEntity {
    @Id
    @GeneratedValue
    private Long    id;
    private Long    userId;
    private String  name;
    private String  mobile;
    private String  address;
    private String  addressDetail;
    private Boolean isDefault;

    public String getSnapAddress() {
        return this.address + this.addressDetail;
    }
}
