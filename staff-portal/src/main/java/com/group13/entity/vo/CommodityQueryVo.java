package com.group13.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xujinfengxu
 */
@Data
@Accessors(chain = true)
public class CommodityQueryVo {

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "min price")
    private Double minPrice;

    @ApiModelProperty(value = "max pricet")
    private Double maxPrice;

    @ApiModelProperty(value = "1 = buyCount, 2 = collect count, 3 = visited count")
    private Integer sortBy;

    @ApiModelProperty(value = "true = asc, false = desc")
    private Boolean sortAsc;

    @ApiModelProperty(value = "start time", example = "2019-01-01 10:10:10")
    private String begin;

    @ApiModelProperty(value = "end time", example = "2019-01-01 10:10:10")
    private String end;
}
