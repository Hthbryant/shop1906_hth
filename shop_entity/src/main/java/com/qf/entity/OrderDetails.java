package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderDetails extends BaseEntity{

    private Integer oid;
    private Integer uid;
    private Integer gid;
    private Integer number;
    private String subject;
    private BigDecimal price;
    private BigDecimal detailPrice;

    @TableField(exist = false)
    private Goods goods;
}
