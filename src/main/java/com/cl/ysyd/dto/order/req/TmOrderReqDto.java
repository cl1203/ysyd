/**
 * TmOrderReqDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.order.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * 订单输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "订单请求Dto")
public class TmOrderReqDto {
    /**
     * 订单号
     */
    @ApiModelProperty(value="订单号")
    private String orderNo;

    /**
     * 图片地址
     */
    @ApiModelProperty(value="图片地址" )
    @Length(max=255,message="图片地址字段过长, 最大长度为255")
    private String imgUrl;

    /**
     * 交货日期
     */
    @ApiModelProperty(value="交货日期" , required = true)
    @NotBlank(message="交货日期不能为空")
    private String deliveryDate;

    /**
     * 单价
     */
    @ApiModelProperty(value="单价" , required = true)
    @NotBlank(message = "单价不能为空")
    private String unitPrice;

    /**
     * 订单创建日期
     */
    @ApiModelProperty(value="订单创建日期")
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
    @Length(max=50,message="订单状态字段过长, 最大长度为50")
    private String orderStatus;

    /**
     * 订单所属用户
     */
    @ApiModelProperty(value="订单所属用户" )
    private String orderUser;

    /**
     * 文件夹地址
     */
    @ApiModelProperty(value="文件夹地址" )
    @Length(max=255,message="文件夹地址字段过长, 最大长度为255")
    private String folderUrl;

    /**
     * 订单sku
     */
    @ApiModelProperty(value="订单sku" )
    @Length(max=100,message="订单sku字段过长, 最大长度为100")
    private String sku;

    /**
     * 下单人
     */
    @ApiModelProperty(value="下单人" )
    @Length(max=50,message="下单人字段过长, 最大长度为50")
    private String orderPeople;

    /**
     * 尺码
     */
    @ApiModelProperty(value="尺码" )
    @Length(max=20,message="尺码字段过长, 最大长度为20")
    private String orderSize;

    /**
     * 订单类型
     */
    @ApiModelProperty(value="订单类型" )
    @Length(max=50,message="订单类型字段过长, 最大长度为50")
    private String orderType;

    /**
     * 审核状态 Y: 审核通过 N:审核未通过
     */
    @ApiModelProperty(value="审核状态 Y: 审核通过 N:审核未通过" )
    @Length(max=20,message="审核状态 Y: 审核通过 N:审核未通过字段过长, 最大长度为20")
    private String examineStatus;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    @ApiModelProperty(value="状态 0:禁用 1:可用 " )
    private String status;

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
