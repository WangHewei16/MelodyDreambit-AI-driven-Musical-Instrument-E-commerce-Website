package com.group13.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
@Accessors(chain = true)
public class CommodityBasicInfoDto {

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "price")
    private Integer price;

    @ApiModelProperty(value = "image for each commodity")
    private String imageOss;

    @ApiModelProperty(value = "amount")
    private Integer amount;

    @ApiModelProperty(value = "0 = piano 1 = guitar...")
    private Integer type;

}
