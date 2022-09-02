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
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StatisticsDaily对象", description="")
public class StatisticsDaily implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "日期")
    private String dateCalculated;

    @ApiModelProperty(value = "注册人数")
    private Integer registerNum;

    @ApiModelProperty(value = "访问量")
    private Integer commodityVisitNum;

    @ApiModelProperty(value = "购买量")
    private Integer commodityBuyNum;

    @ApiModelProperty(value = "买家秀数量")
    private Integer postNum;

    @ApiModelProperty(value = "购物车type1量")
    private Integer type1CartNum;

    @ApiModelProperty(value = "购物车type2量")
    private Integer type2CartNum;

    @ApiModelProperty(value = "购物车type3量")
    private Integer type3CartNum;

    @ApiModelProperty(value = "购物车type4量")
    private Integer type4CartNum;

    @ApiModelProperty(value = "购物车type5量")
    private Integer type5CartNum;

    @ApiModelProperty(value = "购物车type6量")
    private Integer type6CartNum;


    @ApiModelProperty(value = "商品type1每日浏览量")
    private Integer type1VisitedNum;

    @ApiModelProperty(value = "商品type2每日浏览量")
    private Integer type2VisitedNum;

    @ApiModelProperty(value = "商品type3每日浏览量")
    private Integer type3VisitedNum;

    @ApiModelProperty(value = "商品type4每日浏览量")
    private Integer type4VisitedNum;

    @ApiModelProperty(value = "商品type5每日浏览量")
    private Integer type5VisitedNum;

    @ApiModelProperty(value = "商品type6每日浏览量")
    private Integer type6VisitedNum;

    @ApiModelProperty(value = "gmtCreate")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "gmtModify")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModify;

}
