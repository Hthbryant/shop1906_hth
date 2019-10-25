package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Order extends BaseEntity {

    private String oid;
    private Integer uid;
    private String subject;
    private String person;
    private String address;
    private String phone;
    private String code;
    private BigDecimal orderPrice; //订单总价
    private String orderDetails;  //订单详情


}
