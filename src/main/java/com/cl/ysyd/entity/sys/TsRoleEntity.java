/**
 * TsRoleEntity.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色model类
 */
public class TsRoleEntity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3733898199262184188L;

    /**
     * 主键
     */
    private String pkId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 是否全部数据权限 Y: 是 N:否
     */
    private String isAll;

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
     * 返回字段:角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置字段值:角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }


    public String getIsAll() {
        return isAll;
    }

    public void setIsAll(String isAll) {
        this.isAll = isAll;
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