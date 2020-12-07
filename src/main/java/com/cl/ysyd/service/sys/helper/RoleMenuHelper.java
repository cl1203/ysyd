/**
 * RoleMenuHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.helper;

import com.cl.ysyd.dto.sys.req.TrRoleMenuReqDto;
import com.cl.ysyd.dto.sys.res.TrRoleMenuResDto;
import com.cl.ysyd.entity.sys.TrRoleMenuEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 角色菜单关系表帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class RoleMenuHelper {

    /**
     * entity转resDto
     * @param TrRoleMenu
     * @return TrRoleMenuResDto
     */
    public TrRoleMenuResDto editResDto(TrRoleMenuEntity TrRoleMenu) {
        if (TrRoleMenu == null) {
            return null;
        }
        TrRoleMenuResDto resDto = new TrRoleMenuResDto();
        resDto.setCreateUser(TrRoleMenu.getCreateUser());
        resDto.setCreateTime(TrRoleMenu.getCreateTime());
        resDto.setLastUpdateTime(TrRoleMenu.getLastUpdateTime());
        resDto.setRoleId(TrRoleMenu.getRoleId());
        resDto.setMenuId(TrRoleMenu.getMenuId());
        resDto.setLastUpdateUser(TrRoleMenu.getLastUpdateUser());
        resDto.setPkId(TrRoleMenu.getPkId());
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TrRoleMenuResDto>
     */
    public List<TrRoleMenuResDto> editResDtoList(List<TrRoleMenuEntity> entityList) {
        List<TrRoleMenuResDto> resDtoList = new ArrayList<>();
        if (entityList == null || entityList.isEmpty()){
            return resDtoList;
        }
        entityList.forEach(entity -> {
            resDtoList.add(this.editResDto(entity));
        });
        return resDtoList;
    }

    /**
     * reqDto转Entity
     * @param reqDto 请求dto
     * @return TrRoleMenuEntity
     */
    public TrRoleMenuEntity editEntity(TrRoleMenuReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TrRoleMenuEntity entity = new TrRoleMenuEntity();
        entity.setRoleId(reqDto.getRoleId());
        entity.setMenuId(reqDto.getMenuId());
        entity.setCreateUser(reqDto.getCreateUser());
        entity.setLastUpdateTime(reqDto.getLastUpdateTime());
        entity.setLastUpdateUser(reqDto.getLastUpdateUser());
        return entity;
    }

    /**
     * reqDto集合转entity集合
     * @param reqDtoList
     * @return List<TrRoleMenuEntity>
     */
    public List<TrRoleMenuEntity> editEntityList(List<TrRoleMenuReqDto> reqDtoList) {
        List<TrRoleMenuEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}