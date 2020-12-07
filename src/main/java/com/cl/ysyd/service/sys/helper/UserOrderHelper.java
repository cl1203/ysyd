/**
 * UserOrderHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.helper;

import com.cl.ysyd.dto.sys.req.TrUserOrderReqDto;
import com.cl.ysyd.dto.sys.res.TrUserOrderResDto;
import com.cl.ysyd.entity.sys.TrUserOrderEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 用户订单关系帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class UserOrderHelper {

    /**
     * entity转resDto
     * @param TrUserOrder
     * @return TrUserOrderResDto
     */
    public TrUserOrderResDto editResDto(TrUserOrderEntity TrUserOrder) {
        if (TrUserOrder == null) {
            return null;
        }
        TrUserOrderResDto resDto = new TrUserOrderResDto();
        resDto.setCreateUser(TrUserOrder.getCreateUser());
        resDto.setCreateTime(TrUserOrder.getCreateTime());
        resDto.setLastUpdateTime(TrUserOrder.getLastUpdateTime());
        resDto.setOrderId(TrUserOrder.getOrderId());
        resDto.setLastUpdateUser(TrUserOrder.getLastUpdateUser());
        resDto.setUserId(TrUserOrder.getUserId());
        resDto.setPkId(TrUserOrder.getPkId());
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TrUserOrderResDto>
     */
    public List<TrUserOrderResDto> editResDtoList(List<TrUserOrderEntity> entityList) {
        List<TrUserOrderResDto> resDtoList = new ArrayList<>();
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
     * @return TrUserOrderEntity
     */
    public TrUserOrderEntity editEntity(TrUserOrderReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TrUserOrderEntity entity = new TrUserOrderEntity();
        entity.setUserId(reqDto.getUserId());
        entity.setOrderId(reqDto.getOrderId());
        entity.setCreateUser(reqDto.getCreateUser());
        entity.setLastUpdateTime(reqDto.getLastUpdateTime());
        entity.setLastUpdateUser(reqDto.getLastUpdateUser());
        return entity;
    }

    /**
     * reqDto集合转entity集合
     * @param reqDtoList
     * @return List<TrUserOrderEntity>
     */
    public List<TrUserOrderEntity> editEntityList(List<TrUserOrderReqDto> reqDtoList) {
        List<TrUserOrderEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}