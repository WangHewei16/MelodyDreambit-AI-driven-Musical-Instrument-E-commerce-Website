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
 * @since 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Commodity对象", description="")
public class Commodity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "unique identification")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

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

    @ApiModelProperty(value = "how many people collect this item")
    private Integer collectAmount;

    @ApiModelProperty(value = "visit_amount")
    private Integer visitAmount;

    @ApiModelProperty(value = "buy_amount")
    private Integer buyAmount;

    @ApiModelProperty(value = "like_amount")
    private Integer likeAmount;

    @ApiModelProperty(value = "created time")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "modified time")
    private Date gmtModify;

    private int discount;
}
