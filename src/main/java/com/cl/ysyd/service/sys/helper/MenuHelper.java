/**
 * MenuHelper.java
 * Created at 2020-12-06
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.helper;

import com.cl.ysyd.dto.sys.req.TsMenuReqDto;
import com.cl.ysyd.dto.sys.res.TsMenuResDto;
import com.cl.ysyd.entity.sys.TsMenuEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单帮助类
 * @author chenlong  2020-12-06
 */
@Component
public class MenuHelper {

    /**
     * entity转resDto
     * @param TsMenu
     * @return TsMenuResDto
     */
    public TsMenuResDto editResDto(TsMenuEntity TsMenu) {
        if (TsMenu == null) {
            return null;
        }
        TsMenuResDto resDto = new TsMenuResDto();
        resDto.setCreateUser(TsMenu.getCreateUser());
        resDto.setCreateTime(TsMenu.getCreateTime());
        resDto.setName(TsMenu.getName());
        resDto.setLastUpdateTime(TsMenu.getLastUpdateTime());
        resDto.setTargetPage(TsMenu.getTargetPage());
        resDto.setLastUpdateUser(TsMenu.getLastUpdateUser());
        resDto.setStatus(TsMenu.getStatus());
        resDto.setPkId(TsMenu.getPkId());
        resDto.setIcon(TsMenu.getIcon());
        resDto.setSort(TsMenu.getSort());
        resDto.setTitle(TsMenu.getTitle());
        resDto.setType(TsMenu.getType());
        resDto.setParentId(TsMenu.getParentId());
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TsMenuResDto>
     */
    public List<TsMenuResDto> editResDtoList(List<TsMenuEntity> entityList) {
        List<TsMenuResDto> resDtoList = new ArrayList<>();
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
     * @return TsMenuEntity
     */
    public TsMenuEntity editEntity(TsMenuReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TsMenuEntity entity = new TsMenuEntity();
        entity.setTitle(reqDto.getTitle());
        entity.setName(reqDto.getName());
        entity.setSort(reqDto.getSort());
        entity.setParentId(reqDto.getParentId());
        entity.setType(reqDto.getType());
        entity.setIcon(reqDto.getIcon());
        entity.setStatus(reqDto.getStatus());
        entity.setCreateUser(reqDto.getCreateUser());
        entity.setLastUpdateTime(reqDto.getLastUpdateTime());
        entity.setLastUpdateUser(reqDto.getLastUpdateUser());
        return entity;
    }

    /**
     * reqDto集合转entity集合
     * @param reqDtoList
     * @return List<TsMenuEntity>
     */
    public List<TsMenuEntity> editEntityList(List<TsMenuReqDto> reqDtoList) {
        List<TsMenuEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}