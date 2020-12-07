/**
 * RoleMenuServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.impl;

import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.dto.sys.req.TrRoleMenuReqDto;
import com.cl.ysyd.dto.sys.res.TrRoleMenuResDto;
import com.cl.ysyd.entity.sys.TrRoleMenuEntity;
import com.cl.ysyd.mapper.sys.TrRoleMenuMapper;
import com.cl.ysyd.service.sys.IRoleMenuService;
import com.cl.ysyd.service.sys.helper.RoleMenuHelper;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 角色菜单关系表service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
public class RoleMenuServiceImpl implements IRoleMenuService {
    /**
     * 角色菜单关系表Mapper
     */
    @Autowired
    private TrRoleMenuMapper trRoleMenuMapper;

    /**
     * 角色菜单关系表Helper
     */
    @Autowired
    private RoleMenuHelper roleMenuHelper;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TrRoleMenuEntity checkEntity = this.trRoleMenuMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.trRoleMenuMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TrRoleMenuResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TrRoleMenuEntity entity = this.trRoleMenuMapper.selectByPrimaryKey(pkId);
        TrRoleMenuResDto resDto = this.roleMenuHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createRoleMenu(TrRoleMenuReqDto reqDto) {
        log.info("Service createRoleMenu start. reqDto=【{}】",reqDto);
        TrRoleMenuEntity entity = this.roleMenuHelper.editEntity(reqDto);
        entity.setCreateTime(new Date());
        // TODO 添加主键
        int ret = this.trRoleMenuMapper.insert(entity);
        log.info("Service createRoleMenu end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TrRoleMenuReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TrRoleMenuEntity checkEntity = this.trRoleMenuMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TrRoleMenuEntity entity = this.roleMenuHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.trRoleMenuMapper.updateByPrimaryKey(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }
}