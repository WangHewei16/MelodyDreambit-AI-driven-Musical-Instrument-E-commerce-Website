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
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "unique identification")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String email;

    private String username;

    private String password;

    private String avatar;

    private String signature;

    @ApiModelProperty(value = "whether this user is active")
    private Boolean isActive;

    @ApiModelProperty(value = "frist create's time")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "time after modification")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModify;


}
