package org.springbus.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="BrandBO")
public class BrandBO implements Serializable {

    /**
     * 品牌id
     */

    @ApiModelProperty("品牌id")
    private long brandId;
    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String name;
    /**
     * 品牌logo
     */
    @ApiModelProperty("品牌名称")
    private String logo;
}
