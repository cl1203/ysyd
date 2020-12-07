/**
 * TrRoleMenuMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.sys;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.entity.sys.TrRoleMenuEntity;
import com.cl.ysyd.entity.sys.TrRoleMenuEntityExample;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色菜单关系表 mapper类
 * @author chenlong  2020-11-24
 */
public interface TrRoleMenuMapper extends IBaseMapper<TrRoleMenuEntity, TrRoleMenuEntityExample, String> {

    /**
     * 根据角色ID查询所有菜单和按钮信息ID
     * @param roleId
     * @return
     */
    default List<String> queryMenuId(String roleId){
        List<String> menuIds = new ArrayList<>();
        TrRoleMenuEntityExample roleMenuEntityExample = new TrRoleMenuEntityExample();
        TrRoleMenuEntityExample.Criteria criteria = roleMenuEntityExample.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        List<TrRoleMenuEntity> trRoleMenuEntityList = this.selectByExample(roleMenuEntityExample);
        trRoleMenuEntityList.forEach(trRoleMenuEntity -> {
            menuIds.add(trRoleMenuEntity.getMenuId());
        });
        return menuIds;
    }


    /**
     * 根据角色ID删除角色菜单按钮关系表
     * @param roleId
     */
    default void deleteRoleMenuByRoleId(String roleId){
        TrRoleMenuEntityExample roleMenuEntityExample = new TrRoleMenuEntityExample();
        TrRoleMenuEntityExample.Criteria criteria = roleMenuEntityExample.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        this.deleteByExample(roleMenuEntityExample);
    }
}