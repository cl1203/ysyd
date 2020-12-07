/**
 * TsMenuMapper.java
 * Created at 2020-12-06
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.sys;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.entity.sys.TsMenuEntity;
import com.cl.ysyd.entity.sys.TsMenuEntityExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单 mapper类
 * @author chenlong  2020-12-06
 */
public interface TsMenuMapper extends IBaseMapper<TsMenuEntity, TsMenuEntityExample, String> {

    /**
     * 根据角色ID获取对应的所有一级菜单级别的权限
     * 根据角色ID和一级菜单ID获取所有的二级菜单
     * 根据角色ID和菜单ID查询所有按钮权限
     */
    List<TsMenuEntity> queryMenuListByRoleIdAndMenuId(@Param("roleId") String roleId, @Param("parentId") String parentId, @Param("type") String type);


    List<TsMenuEntity> queryMenuListAll(@Param("parentId") String parentId, @Param("type") String type);
}