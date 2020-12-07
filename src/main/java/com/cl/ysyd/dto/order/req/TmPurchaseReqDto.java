/**
 * TmPurchaseReqDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.order.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 采购单输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "采购单请求Dto")
public class TmPurchaseReqDto {
    /**
     * 采购单号
     */
    @ApiModelProperty(value="采购单号" )
    @NotBlank(message="采购单号不能为空")
    @Length(max=50,message="采购单号字段过长, 最大长度为50")
    private String purchaseNo;

    /**
     * 订单号
     */
    @ApiModelProperty(value="订单号" )
    @NotBlank(message="订单号不能为空")
    @Length(max=50,message="订单号字段过长, 最大长度为50")
    private String orderNo;

    /**
     * 采购状态
     */
    @ApiModelProperty(value="采购状态" )
    @NotBlank(message="采购状态不能为空")
    @Length(max=30,message="采购状态字段过长, 最大长度为30")
    private String purchaseStatus;

    /**
     * 采购人员
     */
    @ApiModelProperty(value="采购人员" )
    @NotBlank(message="采购人员不能为空")
    @Length(max=50,message="采购人员字段过长, 最大长度为50")
    private String purchasePersonnel;

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

    /**
     * 备注 
     */
    @ApiModelProperty(value="备注 " )
    @NotBlank(message="备注 不能为空")
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
    @NotBlank(message="创建人不能为空")
    @Length(max=32,message="创建人字段过长, 最大长度为32")
    private String createUser;

    /**
     * 最后修改时间
     * 默认值:CURRENT_TIMESTAMP
     */
    @ApiModelProperty(value="最后修改时间" )
    @NotNull(message="最后修改时间不能为空")
    private Date lastUpdateTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value="最后修改人" )
    @NotBlank(message="最后修改人不能为空")
    @Length(max=32,message="最后修改人字段过长, 最大长度为32")
    private String lastUpdateUser;
}