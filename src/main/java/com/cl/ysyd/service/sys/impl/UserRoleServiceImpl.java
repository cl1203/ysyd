/**
 * UserRoleServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.impl;

import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.dto.sys.req.TrUserRoleReqDto;
import com.cl.ysyd.dto.sys.res.TrUserRoleResDto;
import com.cl.ysyd.entity.sys.TrUserRoleEntity;
import com.cl.ysyd.mapper.sys.TrUserRoleMapper;
import com.cl.ysyd.service.sys.IUserRoleService;
import com.cl.ysyd.service.sys.helper.UserRoleHelper;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户角色关系service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
public class UserRoleServiceImpl implements IUserRoleService {
    /**
     * 用户角色关系Mapper
     */
    @Autowired
    private TrUserRoleMapper trUserRoleMapper;

    /**
     * 用户角色关系Helper
     */
    @Autowired
    private UserRoleHelper userRoleHelper;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TrUserRoleEntity checkEntity = this.trUserRoleMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.trUserRoleMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TrUserRoleResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TrUserRoleEntity entity = this.trUserRoleMapper.selectByPrimaryKey(pkId);
        TrUserRoleResDto resDto = this.userRoleHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createUserRole(TrUserRoleReqDto reqDto) {
        log.info("Service createUserRole start. reqDto=【{}】",reqDto);
        TrUserRoleEntity entity = this.userRoleHelper.editEntity(reqDto);
        entity.setCreateTime(new Date());
        // TODO 添加主键
        int ret = this.trUserRoleMapper.insert(entity);
        log.info("Service createUserRole end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TrUserRoleReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TrUserRoleEntity checkEntity = this.trUserRoleMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TrUserRoleEntity entity = this.userRoleHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.trUserRoleMapper.updateByPrimaryKey(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }
}