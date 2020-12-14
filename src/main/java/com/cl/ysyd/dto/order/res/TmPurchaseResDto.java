/**
 * TmPurchaseResDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.order.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采购单输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "采购单响应Dto")
public class TmPurchaseResDto {
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
     * 订单号
     */
    @ApiModelProperty(value="订单号" )
    private String orderNo;

    /**
     * 采购状态
     */
    @ApiModelProperty(value="采购状态" )
    private String purchaseStatus;

    @ApiModelProperty(value="采购状态文本" )
    private String purchaseStatusText;

    /**
     * 采购人员
     */
    @ApiModelProperty(value="采购人员" )
    private String purchasePersonnel;

    @ApiModelProperty(value="采购人员姓名" )
    private String purchasePersonnelName;

    /**
     * 采购总金额
     */
    @ApiModelProperty(value="采购总金额" )
    private BigDecimal totalAmount;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    @ApiModelProperty(value="状态 0:禁用 1:可用 " )
    private Byte status;

    @ApiModelProperty(value="状态文本" )
    private String statusText;

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
    private String createTime;

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
    private String lastUpdateTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value="最后修改人" )
    private String lastUpdateUser;

    @ApiModelProperty(value="采购明细list" )
    private List<TmPurchaseDetailResDto> purchaseDetailReqDtoList;
}
