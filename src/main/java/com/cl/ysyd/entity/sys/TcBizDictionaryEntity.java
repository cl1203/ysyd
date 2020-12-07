/**
 * TcBizDictionaryEntity.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典表model类
 */
public class TcBizDictionaryEntity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3733898199262184188L;

    /**
     * PK_ID
     */
    private String pkId;

    /**
     * 类型
     */
    private String bizType;

    /**
     * 编码
     */
    private String bizCode;

    /**
     * 文本
     */
    private String bizText;

    /**
     * 描述
     */
    private String description;

    /**
     * 顺序
     * 默认值:0
     */
    private Integer seq;

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
     * 返回字段:PK_ID
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * 设置字段值:PK_ID
     */
    public void setPkId(String pkId) {
        this.pkId = pkId == null ? null : pkId.trim();
    }

    /**
     * 返回字段:类型
     */
    public String getBizType() {
        return bizType;
    }

    /**
     * 设置字段值:类型
     */
    public void setBizType(String bizType) {
        this.bizType = bizType == null ? null : bizType.trim();
    }

    /**
     * 返回字段:编码
     */
    public String getBizCode() {
        return bizCode;
    }

    /**
     * 设置字段值:编码
     */
    public void setBizCode(String bizCode) {
        this.bizCode = bizCode == null ? null : bizCode.trim();
    }

    /**
     * 返回字段:文本
     */
    public String getBizText() {
        return bizText;
    }

    /**
     * 设置字段值:文本
     */
    public void setBizText(String bizText) {
        this.bizText = bizText == null ? null : bizText.trim();
    }

    /**
     * 返回字段:描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置字段值:描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 返回字段:顺序
     * 默认值:0
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置字段值:顺序
     * 默认值:0
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
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