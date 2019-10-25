package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Address extends  BaseEntity {

    private Integer uid;
    private String person;
    private String code;
    private String phone;
    private Integer isDefault;
    private String address;


}
