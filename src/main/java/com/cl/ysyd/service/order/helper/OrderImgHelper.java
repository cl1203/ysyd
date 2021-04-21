/**
 * OrderImgHelper.java
 * Created at 2021-04-21
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.helper;

import com.cl.ysyd.dto.order.req.TmOrderImgReqDto;
import com.cl.ysyd.dto.order.res.TmOrderImgResDto;
import com.cl.ysyd.entity.order.TmOrderImgEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author chenlong  2021-04-21
 */
@Component
public class OrderImgHelper {

    /**
     * entity转resDto
     * @param TmOrderImg
     * @return TmOrderImgResDto
     */
    public TmOrderImgResDto editResDto(TmOrderImgEntity TmOrderImg) {
        if (TmOrderImg == null) {
            return null;
        }
        TmOrderImgResDto resDto = new TmOrderImgResDto();
        resDto.setCreateUser(TmOrderImg.getCreateUser());
        resDto.setImgDetailUrl(TmOrderImg.getImgDetailUrl());
        resDto.setCreateTime(TmOrderImg.getCreateTime());
        resDto.setOrderNo(TmOrderImg.getOrderNo());
        resDto.setLastUpdateTime(TmOrderImg.getLastUpdateTime());
        resDto.setLastUpdateUser(TmOrderImg.getLastUpdateUser());
        resDto.setPkId(TmOrderImg.getPkId());
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TmOrderImgResDto>
     */
    public List<TmOrderImgResDto> editResDtoList(List<TmOrderImgEntity> entityList) {
        List<TmOrderImgResDto> resDtoList = new ArrayList<>();
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
     * @return TmOrderImgEntity
     */
    public TmOrderImgEntity editEntity(TmOrderImgReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TmOrderImgEntity entity = new TmOrderImgEntity();
        entity.setOrderNo(reqDto.getOrderNo());
        entity.setImgDetailUrl(reqDto.getImgDetailUrl());
        entity.setCreateUser(reqDto.getCreateUser());
        entity.setLastUpdateTime(reqDto.getLastUpdateTime());
        entity.setLastUpdateUser(reqDto.getLastUpdateUser());
        return entity;
    }

    /**
     * reqDto集合转entity集合
     * @param reqDtoList
     * @return List<TmOrderImgEntity>
     */
    public List<TmOrderImgEntity> editEntityList(List<TmOrderImgReqDto> reqDtoList) {
        List<TmOrderImgEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}