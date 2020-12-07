/**
 * TrRoleMenuEntity.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色菜单关系表model类
 */
public class TrRoleMenuEntity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3733898199262184188L;

    /**
     * PK_ID
     */
    private String pkId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 菜单id
     */
    private String menuId;

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
     * 返回字段:角色id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置字段值:角色id
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * 返回字段:菜单id
     */
    public String getMenuId() {
        return menuId;
    }

    /**
     * 设置字段值:菜单id
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
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