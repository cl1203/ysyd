/**
 * UserRoleHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.helper;

import com.cl.ysyd.dto.sys.req.TrUserRoleReqDto;
import com.cl.ysyd.dto.sys.res.TrUserRoleResDto;
import com.cl.ysyd.entity.sys.TrUserRoleEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 用户角色关系帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class UserRoleHelper {

    /**
     * entity转resDto
     * @param TrUserRole
     * @return TrUserRoleResDto
     */
    public TrUserRoleResDto editResDto(TrUserRoleEntity TrUserRole) {
        if (TrUserRole == null) {
            return null;
        }
        TrUserRoleResDto resDto = new TrUserRoleResDto();
        resDto.setCreateUser(TrUserRole.getCreateUser());
        resDto.setCreateTime(TrUserRole.getCreateTime());
        resDto.setLastUpdateTime(TrUserRole.getLastUpdateTime());
        resDto.setRoleId(TrUserRole.getRoleId());
        resDto.setLastUpdateUser(TrUserRole.getLastUpdateUser());
        resDto.setUserId(TrUserRole.getUserId());
        resDto.setPkId(TrUserRole.getPkId());
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TrUserRoleResDto>
     */
    public List<TrUserRoleResDto> editResDtoList(List<TrUserRoleEntity> entityList) {
        List<TrUserRoleResDto> resDtoList = new ArrayList<>();
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
     * @return TrUserRoleEntity
     */
    public TrUserRoleEntity editEntity(TrUserRoleReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TrUserRoleEntity entity = new TrUserRoleEntity();
        entity.setUserId(reqDto.getUserId());
        entity.setRoleId(reqDto.getRoleId());
        entity.setCreateUser(reqDto.getCreateUser());
        entity.setLastUpdateTime(reqDto.getLastUpdateTime());
        entity.setLastUpdateUser(reqDto.getLastUpdateUser());
        return entity;
    }

    /**
     * reqDto集合转entity集合
     * @param reqDtoList
     * @return List<TrUserRoleEntity>
     */
    public List<TrUserRoleEntity> editEntityList(List<TrUserRoleReqDto> reqDtoList) {
        List<TrUserRoleEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}