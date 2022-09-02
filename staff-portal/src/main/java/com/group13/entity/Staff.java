package com.group13.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Staff对象", description="")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "微信登陆")
    private String openid;

    @ApiModelProperty(value = "头像")
    private String avatar;

    private String nickname;

    @ApiModelProperty(value = "gmtCreate")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "gmtModify")
    private Date gmtModify;


}
