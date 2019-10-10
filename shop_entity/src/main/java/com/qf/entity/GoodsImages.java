package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoodsImages extends  BaseEntity {

    private Integer gid;
    private String imageInfo;
    private String url;
    private Integer isDefault =0;
}
