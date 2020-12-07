/**
 * TsMenuEntity.java
 * Created at 2020-12-06
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单model类
 */
public class TsMenuEntity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3733898199262184188L;

    /**
     * PK_ID
     */
    private String pkId;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 父菜单id
     * 默认值:0
     */
    private String parentId;

    /**
     * 权限类型(菜单or按钮)
     */
    private String type;

    /**
     * 菜单路径
     */
    private String targetPage;

    /**
     * 图标
     */
    private String icon;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    private Byte status;

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
     * 返回字段:菜单标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置字段值:菜单标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 返回字段:名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置字段值:名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 返回字段:排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置字段值:排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 返回字段:父菜单id
     * 默认值:0
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置字段值:父菜单id
     * 默认值:0
     */
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    /**
     * 返回字段:权限类型(菜单or按钮)
     */
    public String getType() {
        return type;
    }

    /**
     * 设置字段值:权限类型(菜单or按钮)
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 返回字段:菜单路径
     */
    public String getTargetPage() {
        return targetPage;
    }

    /**
     * 设置字段值:菜单路径
     */
    public void setTargetPage(String targetPage) {
        this.targetPage = targetPage == null ? null : targetPage.trim();
    }

    /**
     * 返回字段:图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置字段值:图标
     */
    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
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