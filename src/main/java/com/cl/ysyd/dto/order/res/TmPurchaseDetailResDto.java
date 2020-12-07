/**
 * TmPurchaseDetailResDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.order.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 采购单明细输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "采购单明细响应Dto")
public class TmPurchaseDetailResDto {
    /**
     * 主键
     */
    @ApiModelProperty(value="主键" )
    private String pkId;

    /**
     * 采购单号
     */
    @ApiModelProperty(value="采购单号" )
    private String purchaseNo;

    /**
     * 采购编号
     */
    @ApiModelProperty(value="采购编号" )
    private String purchaseNumber;

    /**
     * 物料名称
     */
    @ApiModelProperty(value="物料名称" )
    private String materielName;

    /**
     * 克重
     */
    @ApiModelProperty(value="克重" )
    private BigDecimal gramWeight;

    /**
     * 幅宽
     */
    @ApiModelProperty(value="幅宽" )
    private BigDecimal widthOfCloth;

    /**
     * 采购单价
     */
    @ApiModelProperty(value="采购单价" )
    private BigDecimal unitPrice;

    /**
     * 单位
     */
    @ApiModelProperty(value="单位" )
    private String unit;

    /**
     * 供应商
     */
    @ApiModelProperty(value="供应商" )
    private String supplier;

    /**
     * 颜色
     */
    @ApiModelProperty(value="颜色" )
    private String colour;

    /**
     * 数量
     */
    @ApiModelProperty(value="数量" )
    private Integer quantity;

    /**
     * 总金额
     */
    @ApiModelProperty(value="总金额" )
    private BigDecimal totalAmount;

    /**
     * 采购日期(录入采购单日期)
     */
    @ApiModelProperty(value="采购日期(录入采购单日期)" )
    private Date purchaseDate;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    @ApiModelProperty(value="状态 0:禁用 1:可用 " )
    private Byte status;

    /**
     * 备注 
     */
    @ApiModelProperty(value="备注 " )
    private String remarks;

    /**
     * 创建时间
     * 默认值:CURRENT_TIMESTAMP
     */
    @ApiModelProperty(value="创建时间" )
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人" )
    private String createUser;

    /**
     * 最后修改时间
     * 默认值:CURRENT_TIMESTAMP
     */
    @ApiModelProperty(value="最后修改时间" )
    private Date lastUpdateTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value="最后修改人" )
    private String lastUpdateUser;
}