/**
 * TmPurchaseDetailEntity.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购单明细model类
 */
public class TmPurchaseDetailEntity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3733898199262184188L;

    /**
     * 主键
     */
    private String pkId;

    /**
     * 采购单号
     */
    private String purchaseNo;

    /**
     * 采购编号
     */
    private String purchaseNumber;

    /**
     * 物料名称
     */
    private String materielName;

    /**
     * 克重
     */
    private BigDecimal gramWeight;

    /**
     * 幅宽
     */
    private BigDecimal widthOfCloth;

    /**
     * 采购单价
     */
    private BigDecimal unitPrice;

    /**
     * 单位
     */
    private String unit;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 颜色
     */
    private String colour;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 采购日期(录入采购单日期)
     */
    private Date purchaseDate;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    private Byte status;

    /**
     * 备注 
     */
    private String remarks;

    /**
     * 创建时间
     * 默认值:CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 最后修改时间
     * 默认值:CURRENT_TIMESTAMP
     */
    private Date lastUpdateTime;

    /**
     * 最后修改人
     */
    private String lastUpdateUser;

    private String purchaseImg;

    public String getPurchaseImg() {
        return purchaseImg;
    }

    public void setPurchaseImg(String purchaseImg) {
        this.purchaseImg = purchaseImg;
    }

    /**
     * 返回字段:主键
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * 设置字段值:主键
     */
    public void setPkId(String pkId) {
        this.pkId = pkId == null ? null : pkId.trim();
    }

    /**
     * 返回字段:采购单号
     */
    public String getPurchaseNo() {
        return purchaseNo;
    }

    /**
     * 设置字段值:采购单号
     */
    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo == null ? null : purchaseNo.trim();
    }

    /**
     * 返回字段:采购编号
     */
    public String getPurchaseNumber() {
        return purchaseNumber;
    }

    /**
     * 设置字段值:采购编号
     */
    public void setPurchaseNumber(String purchaseNumber) {
        this.purchaseNumber = purchaseNumber == null ? null : purchaseNumber.trim();
    }

    /**
     * 返回字段:物料名称
     */
    public String getMaterielName() {
        return materielName;
    }

    /**
     * 设置字段值:物料名称
     */
    public void setMaterielName(String materielName) {
        this.materielName = materielName == null ? null : materielName.trim();
    }

    /**
     * 返回字段:克重
     */
    public BigDecimal getGramWeight() {
        return gramWeight;
    }

    /**
     * 设置字段值:克重
     */
    public void setGramWeight(BigDecimal gramWeight) {
        this.gramWeight = gramWeight;
    }

    /**
     * 返回字段:幅宽
     */
    public BigDecimal getWidthOfCloth() {
        return widthOfCloth;
    }

    /**
     * 设置字段值:幅宽
     */
    public void setWidthOfCloth(BigDecimal widthOfCloth) {
        this.widthOfCloth = widthOfCloth;
    }

    /**
     * 返回字段:采购单价
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * 设置字段值:采购单价
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 返回字段:单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置字段值:单位
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * 返回字段:供应商
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * 设置字段值:供应商
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier == null ? null : supplier.trim();
    }

    /**
     * 返回字段:颜色
     */
    public String getColour() {
        return colour;
    }

    /**
     * 设置字段值:颜色
     */
    public void setColour(String colour) {
        this.colour = colour == null ? null : colour.trim();
    }

    /**
     * 返回字段:数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置字段值:数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 返回字段:总金额
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * 设置字段值:总金额
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * 返回字段:采购日期(录入采购单日期)
     */
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * 设置字段值:采购日期(录入采购单日期)
     */
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * 返回字段:状态 0:禁用 1:可用 
     * 默认值:1
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置字段值:状态 0:禁用 1:可用 
     * 默认值:1
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 返回字段:备注 
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置字段值:备注 
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    /**
     * 返回字段:创建时间
     * 默认值:CURRENT_TIMESTAMP
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置字段值:创建时间
     * 默认值:CURRENT_TIMESTAMP
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 返回字段:创建人
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 设置字段值:创建人
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * 返回字段:最后修改时间
     * 默认值:CURRENT_TIMESTAMP
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * 设置字段值:最后修改时间
     * 默认值:CURRENT_TIMESTAMP
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * 返回字段:最后修改人
     */
    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    /**
     * 设置字段值:最后修改人
     */
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser == null ? null : lastUpdateUser.trim();
    }
}
