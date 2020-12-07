/**
 * TmPurchaseEntity.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购单model类
 */
public class TmPurchaseEntity implements Serializable {
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
     * 订单号
     */
    private String orderNo;

    /**
     * 采购状态
     */
    private String purchaseStatus;

    /**
     * 采购人员
     */
    private String purchasePersonnel;

    /**
     * 采购总金额
     */
    private BigDecimal totalAmount;

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
     * 返回字段:订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置字段值:订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * 返回字段:采购状态
     */
    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    /**
     * 设置字段值:采购状态
     */
    public void setPurchaseStatus(String purchaseStatus) {
        this.purchaseStatus = purchaseStatus == null ? null : purchaseStatus.trim();
    }

    /**
     * 返回字段:采购人员
     */
    public String getPurchasePersonnel() {
        return purchasePersonnel;
    }

    /**
     * 设置字段值:采购人员
     */
    public void setPurchasePersonnel(String purchasePersonnel) {
        this.purchasePersonnel = purchasePersonnel == null ? null : purchasePersonnel.trim();
    }

    /**
     * 返回字段:采购总金额
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * 设置字段值:采购总金额
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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