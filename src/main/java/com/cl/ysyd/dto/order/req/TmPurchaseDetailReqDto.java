/**
 * TmPurchaseDetailReqDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.order.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 采购单明细输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "采购单明细请求Dto")
public class TmPurchaseDetailReqDto {
    /**
     * 采购单号
     */
    @ApiModelProperty(value="采购单号" )
    @Length(max=32,message="采购单号字段过长, 最大长度为32")
    private String purchaseNo;

    /**
     * 采购编号
     */
    @ApiModelProperty(value="采购编号" )
    @Length(max=50,message="采购编号字段过长, 最大长度为50")
    private String purchaseNumber;

    /**
     * 物料名称
     */
    @ApiModelProperty(value="物料名称" )
    @Length(max=100,message="物料名称字段过长, 最大长度为100")
    private String materielName;

    /**
     * 克重
     */
    @ApiModelProperty(value="克重" )
    private String gramWeight;

    /**
     * 幅宽
     */
    @ApiModelProperty(value="幅宽" )
    private String widthOfCloth;

    /**
     * 采购单价
     */
    @ApiModelProperty(value="采购单价" )
    private String unitPrice;

    /**
     * 单位
     */
    @ApiModelProperty(value="单位" )
    @Length(max=50,message="单位字段过长, 最大长度为50")
    private String unit;

    /**
     * 供应商
     */
    @ApiModelProperty(value="供应商" )
    @Length(max=100,message="供应商字段过长, 最大长度为100")
    private String supplier;

    /**
     * 颜色
     */
    @ApiModelProperty(value="颜色" )
    @Length(max=50,message="颜色字段过长, 最大长度为50")
    private String colour;

    /**
     * 数量
     */
    @ApiModelProperty(value="数量" )
    private String quantity;

    /**
     * 总金额
     */
    @ApiModelProperty(value="总金额" )
    private String totalAmountDetail;

    /**
     * 采购日期(录入采购单日期)
     */
    @ApiModelProperty(value="采购日期(录入采购单日期)" )
    private String purchaseDate;

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
    @Length(max=128,message="备注 字段过长, 最大长度为128")
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
    @Length(max=32,message="创建人字段过长, 最大长度为32")
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
    @Length(max=32,message="最后修改人字段过长, 最大长度为32")
    private String lastUpdateUser;
}
