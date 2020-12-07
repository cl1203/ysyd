/**
 * TrUserRoleMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.sys;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.entity.sys.TrUserRoleEntity;
import com.cl.ysyd.entity.sys.TrUserRoleEntityExample;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户角色关系 mapper类
 * @author chenlong  2020-11-24
 */
public interface TrUserRoleMapper extends IBaseMapper<TrUserRoleEntity, TrUserRoleEntityExample, String> {

    /**
     * 根据用户id获取用户角色id列表
     *
     * @param userId 用户id
     * @return 用户角色id列表
     */
    default List<String> getUserRoleIdList(String userId) {
        List<String> roleIds = new ArrayList<>();
        TrUserRoleEntityExample userRoleExample = new TrUserRoleEntityExample();
        userRoleExample.createCriteria().andUserIdEqualTo(userId);
        List<TrUserRoleEntity> userRoleEntityList = selectByExample(userRoleExample);
        userRoleEntityList.forEach(entity -> roleIds.add(entity.getRoleId()));
        return roleIds;
    }

    /**
     * 根据角色ID删除用户角色关系表
     * @param roleId
     */
    default void deleteUserRoleByRoleId(String roleId){
        TrUserRoleEntityExample userRoleExample = new TrUserRoleEntityExample();
        TrUserRoleEntityExample.Criteria criteria = userRoleExample.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        this.deleteByExample(userRoleExample);
    }

    /**
     * 根据用户ID删除用户角色关系表
     * @param userId
     */
    default void deleteUserRoleByUserId(String userId){
        TrUserRoleEntityExample userRoleExample = new TrUserRoleEntityExample();
        TrUserRoleEntityExample.Criteria criteria = userRoleExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        this.deleteByExample(userRoleExample);
    }
}