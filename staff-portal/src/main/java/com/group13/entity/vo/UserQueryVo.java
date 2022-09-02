package com.group13.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xujinfengxu
 */
@Data
@Accessors(chain = true)
public class UserQueryVo {

    @ApiModelProperty(value = "fuzzy search")
    private String username;

    @ApiModelProperty(value = "is active or not")
    private Boolean isActive;

    @ApiModelProperty(value = "start time", example = "2019-01-01 10:10:10")
    private String begin;

    @ApiModelProperty(value = "end time", example = "2019-01-01 10:10:10")
    private String end;
}
