/**
 * TsRoleMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.sys;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.common.enums.IsValidEnum;
import com.cl.ysyd.dto.sys.res.RoleAllResDto;
import com.cl.ysyd.entity.sys.TsRoleEntity;
import com.cl.ysyd.entity.sys.TsRoleEntityExample;

import java.util.List;

/**
 * 角色 mapper类
 * @author chenlong  2020-11-24
 */
public interface TsRoleMapper extends IBaseMapper<TsRoleEntity, TsRoleEntityExample, String> {


    /**
     * 根据角色id列表获取角色名称字符串
     *
     * @param roleIds 角色ID集合
     * @return 角色名称字符串
     */
    default List<TsRoleEntity> getRolesTextLine(List<String> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return null;
        }

        TsRoleEntityExample roleExample = new TsRoleEntityExample();
        TsRoleEntityExample.Criteria criteria = roleExample.createCriteria();
        criteria.andPkIdIn(roleIds);
        criteria.andStatusEqualTo(IsValidEnum.VALID.getCode().byteValue());
        return this.selectByExample(roleExample);
    }

    default long countByRoleName(String roleName){
        //校验角色名
        TsRoleEntityExample roleEntityExample = new TsRoleEntityExample();
        TsRoleEntityExample.Criteria criteria = roleEntityExample.createCriteria();
        //criteria.andStatusEqualTo(IsValidEnum.VALID.getCode().byteValue());
        criteria.andRoleNameEqualTo(roleName);
        return this.countByExample(roleEntityExample);
    }

    List<RoleAllResDto> queryAll();

    TsRoleEntity queryByUserId(String userId);
}
