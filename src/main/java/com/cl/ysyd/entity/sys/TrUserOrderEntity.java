/**
 * TrUserOrderEntity.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户订单关系model类
 */
public class TrUserOrderEntity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3733898199262184188L;

    /**
     * 主键
     */
    private String pkId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 订单ID
     */
    private String orderId;

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
     * 返回字段:用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置字段值:用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 返回字段:订单ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置字段值:订单ID
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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