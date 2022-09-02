package com.group13.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xujinfengxu
 */
@Data
@Accessors(chain = true)
public class OrderQueryVo {

    private String userId;

    private Integer method;

    private Integer status;

    private Integer flowstatus;

    @ApiModelProperty(value = "start time", example = "2019-01-01 10:10:10")
    private String begin;

    @ApiModelProperty(value = "end time", example = "2019-01-01 10:10:10")
    private String end;
}
