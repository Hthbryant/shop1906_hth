package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Orders extends BaseEntity {

    private String oid;
    private Integer uid;
    private String person;
    private String address;
    private String phone;
    private String code;
    private BigDecimal orderPrice; //订单总价
    private String orderDetails;  //订单详情


    @TableField(exist = false)
    private List<OrderDetails> detailsList;


}
