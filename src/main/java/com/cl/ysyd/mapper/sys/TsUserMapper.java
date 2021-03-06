/**
 * TsUserMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.sys;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.IsValidEnum;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.entity.sys.TsUserEntityExample;

import java.util.List;

/**
 * 用户 mapper类
 * @author chenlong  2020-11-24
 */
public interface TsUserMapper extends IBaseMapper<TsUserEntity, TsUserEntityExample, String> {

    /**
     * 根据用户名查询用户信息数量
     * @param userName 用户名
     * @return 数量
     */
    default long countByUserName(String userName){
        TsUserEntityExample tsUserEntityExample = new TsUserEntityExample();
        TsUserEntityExample.Criteria criteria = tsUserEntityExample.createCriteria();
        criteria.andUserNameEqualTo(userName);
        //criteria.andStatusEqualTo(IsValidEnum.VALID.getCode().byteValue());
        long l = this.countByExample(tsUserEntityExample);
        return l;
    }

    /**
     * 查询所有用户
     * @return 用户集合
     */
    default List<TsUserEntity> queryAll(){
        TsUserEntityExample tsUserEntityExample = new TsUserEntityExample();
        TsUserEntityExample.Criteria criteria = tsUserEntityExample.createCriteria();
        criteria.andStatusEqualTo(SortConstant.ONE.byteValue());
        criteria.andPkIdNotEqualTo(SortConstant.ONE.toString());
        return this.selectByExample(tsUserEntityExample);
    }


    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     * @return 用户结果集
     */
    default List<TsUserEntity> queyByUserName(String userName){
        TsUserEntityExample tsUserEntityExample = new TsUserEntityExample();
        TsUserEntityExample.Criteria criteria = tsUserEntityExample.createCriteria();
        criteria.andUserNameEqualTo(userName);
        criteria.andStatusEqualTo(IsValidEnum.VALID.getCode().byteValue());
        List<TsUserEntity> userEntityList = this.selectByExample(tsUserEntityExample);
        return userEntityList;
    }

    /**
     * 查询用户总数量
     * @return 总数量
     */
    Long selectUserNum();
}
