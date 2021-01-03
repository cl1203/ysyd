package com.cl.ysyd.dto.order.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "采购单对账单响应Dto")
public class TmPurchaseBillResDto {

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
     * 采购人员
     */
    @ApiModelProperty(value="采购人员" )
    private String purchasePersonnel;

    @ApiModelProperty(value="采购人员姓名" )
    private String purchasePersonnelName;

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
     * 采购单价
     */
    @ApiModelProperty(value="采购总金额" )
    private String purchaseTotalMoney;

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
     * 采购日期(录入采购单日期)
     */
    @ApiModelProperty(value="采购日期(录入采购单日期)" )
    private String purchaseDate;

    @ApiModelProperty(value = "单价合计")
    private String totalMoney;
}
