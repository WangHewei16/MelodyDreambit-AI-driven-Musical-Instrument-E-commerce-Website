package com.group13.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xujinfengxu
 */
@Data
@Accessors(chain = true)
public class PostQueryVo {

    @ApiModelProperty(value = "title")
    private String title;

    private String userId;

    private String commodityId;

    @ApiModelProperty(value = "start time", example = "2019-01-01 10:10:10")
    private String begin;

    @ApiModelProperty(value = "end time", example = "2019-01-01 10:10:10")
    private String end;
}
