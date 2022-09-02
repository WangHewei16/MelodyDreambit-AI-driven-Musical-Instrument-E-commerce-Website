package com.group13.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author group13
 * @since 2022-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PostComment对象", description="")
public class PostComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "unique identification")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "content")
    private String content;

    @ApiModelProperty(value = "post id")
    private String postId;

    @ApiModelProperty(value = "user id")
    private String userId;

    private Integer likeAmount;

    @ApiModelProperty(value = "created time")
    private Date gmtCreate;

    @ApiModelProperty(value = "created time")
    private Date gmtModify;

}
