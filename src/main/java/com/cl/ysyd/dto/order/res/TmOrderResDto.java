/**
 * TmOrderResDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.order.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "订单响应Dto")
public class TmOrderResDto {
    /**
     * 主键
     */
    @ApiModelProperty(value="主键" )
    private String pkId;

    /**
     * 订单号
     */
    @ApiModelProperty(value="订单号" )
    private String orderNo;

    /**
     * 图片地址
     */
    @ApiModelProperty(value="图片地址" )
    private String imgUrl;

    /**
     * 交货日期
     */
    @ApiModelProperty(value="交货日期" )
    private String deliveryDate;

    /**
     * 订单所属用户
     */
    @ApiModelProperty(value="订单所属用户" )
    private String orderUser;

    /**
     * 订单所属用户
     */
    @ApiModelProperty(value="订单所属用户姓名" )
    private String orderUserName;

    /**
     * 单价
     */
    @ApiModelProperty(value="单价" )
    private String unitPrice;

    /**
     * 订单创建日期
     */
    @ApiModelProperty(value="订单创建日期" )
    private String establishDate;

    /**
     * 订单完成日期
     */
    @ApiModelProperty(value="订单完成日期" )
    private String completeDate;

    /**
     * 订单状态
     */
    @ApiModelProperty(value="订单状态" )
    private String orderStatus;

    @ApiModelProperty(value="订单状态文本" )
    private String orderStatusText;

    /**
     * 文件夹地址
     */
    @ApiModelProperty(value="文件夹地址" )
    private String folderUrl;

    /**
     * 订单sku
     */
    @ApiModelProperty(value="订单sku" )
    private String sku;

    /**
     * 下单人
     */
    @ApiModelProperty(value="下单人" )
    private String orderPeople;

    /**
     * 尺码
     */
    @ApiModelProperty(value="尺码" )
    private String orderSize;

    /**
     * 订单类型
     */
    @ApiModelProperty(value="订单类型" )
    private String orderType;

    @ApiModelProperty(value="订单类型文本" )
    private String orderTypeText;

    /**
     * 审核状态 Y: 审核通过 N:审核未通过
     */
    @ApiModelProperty(value="审核状态 Y: 审核通过 N:审核未通过" )
    private String examineStatus;

    @ApiModelProperty(value="审核状态文本" )
    private String examineStatusText;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    @ApiModelProperty(value="状态 0:禁用 1:可用 " )
    private String status;

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
}