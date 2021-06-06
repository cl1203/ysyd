/**
 * TmOrderEntity.java
 * Created at 2021-06-06
 * Created by 陈龙
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单model类
 */
public class TmOrderEntity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3733898199262184188L;

    /**
     * 主键
     */
    private String pkId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 交货日期
     */
    private Date deliveryDate;

    /**
     * 订单所属用户
     */
    private String orderUser;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 订单创建日期
     */
    private Date establishDate;

    /**
     * 订单完成日期
     */
    private Date completeDate;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 文件夹地址
     */
    private String folderUrl;

    /**
     * 文件夹地址2
     */
    private String folderUrl2;

    /**
     * 件数
     */
    private Integer orderNumber;

    /**
     * 订单sku
     */
    private String sku;

    /**
     * 下单人
     */
    private String orderPeople;

    /**
     * 尺码
     */
    private String orderSize;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 颜色
     */
    private String orderColour;

    /**
     * 审核状态
     */
    private String examineStatus;

    /**
     * 状态 0:作废 1:可用 
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
     * 返回字段:图片地址
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置字段值:图片地址
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    /**
     * 返回字段:交货日期
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * 设置字段值:交货日期
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * 返回字段:订单所属用户
     */
    public String getOrderUser() {
        return orderUser;
    }

    /**
     * 设置字段值:订单所属用户
     */
    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser == null ? null : orderUser.trim();
    }

    /**
     * 返回字段:单价
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * 设置字段值:单价
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 返回字段:订单创建日期
     */
    public Date getEstablishDate() {
        return establishDate;
    }

    /**
     * 设置字段值:订单创建日期
     */
    public void setEstablishDate(Date establishDate) {
        this.establishDate = establishDate;
    }

    /**
     * 返回字段:订单完成日期
     */
    public Date getCompleteDate() {
        return completeDate;
    }

    /**
     * 设置字段值:订单完成日期
     */
    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    /**
     * 返回字段:订单状态
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * 设置字段值:订单状态
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    /**
     * 返回字段:文件夹地址
     */
    public String getFolderUrl() {
        return folderUrl;
    }

    /**
     * 设置字段值:文件夹地址
     */
    public void setFolderUrl(String folderUrl) {
        this.folderUrl = folderUrl == null ? null : folderUrl.trim();
    }

    /**
     * 返回字段:文件夹地址2
     */
    public String getFolderUrl2() {
        return folderUrl2;
    }

    /**
     * 设置字段值:文件夹地址2
     */
    public void setFolderUrl2(String folderUrl2) {
        this.folderUrl2 = folderUrl2 == null ? null : folderUrl2.trim();
    }

    /**
     * 返回字段:件数
     */
    public Integer getOrderNumber() {
        return orderNumber;
    }

    /**
     * 设置字段值:件数
     */
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * 返回字段:订单sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * 设置字段值:订单sku
     */
    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    /**
     * 返回字段:下单人
     */
    public String getOrderPeople() {
        return orderPeople;
    }

    /**
     * 设置字段值:下单人
     */
    public void setOrderPeople(String orderPeople) {
        this.orderPeople = orderPeople == null ? null : orderPeople.trim();
    }

    /**
     * 返回字段:尺码
     */
    public String getOrderSize() {
        return orderSize;
    }

    /**
     * 设置字段值:尺码
     */
    public void setOrderSize(String orderSize) {
        this.orderSize = orderSize == null ? null : orderSize.trim();
    }

    /**
     * 返回字段:订单类型
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * 设置字段值:订单类型
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    /**
     * 返回字段:颜色
     */
    public String getOrderColour() {
        return orderColour;
    }

    /**
     * 设置字段值:颜色
     */
    public void setOrderColour(String orderColour) {
        this.orderColour = orderColour == null ? null : orderColour.trim();
    }

    /**
     * 返回字段:审核状态
     */
    public String getExamineStatus() {
        return examineStatus;
    }

    /**
     * 设置字段值:审核状态
     */
    public void setExamineStatus(String examineStatus) {
        this.examineStatus = examineStatus == null ? null : examineStatus.trim();
    }

    /**
     * 返回字段:状态 0:作废 1:可用 
     * 默认值:1
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置字段值:状态 0:作废 1:可用 
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