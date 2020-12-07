/**
 * PurchaseHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.helper;

import com.cl.ysyd.dto.order.req.TmPurchaseReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseResDto;
import com.cl.ysyd.entity.order.TmPurchaseEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 采购单帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class PurchaseHelper {

    /**
     * entity转resDto
     * @param TmPurchase
     * @return TmPurchaseResDto
     */
    public TmPurchaseResDto editResDto(TmPurchaseEntity TmPurchase) {
        if (TmPurchase == null) {
            return null;
        }
        TmPurchaseResDto resDto = new TmPurchaseResDto();
        resDto.setCreateUser(TmPurchase.getCreateUser());
        resDto.setCreateTime(TmPurchase.getCreateTime());
        resDto.setOrderNo(TmPurchase.getOrderNo());
        resDto.setPurchaseNo(TmPurchase.getPurchaseNo());
        resDto.setTotalAmount(TmPurchase.getTotalAmount());
        resDto.setRemarks(TmPurchase.getRemarks());
        resDto.setLastUpdateTime(TmPurchase.getLastUpdateTime());
        resDto.setPurchaseStatus(TmPurchase.getPurchaseStatus());
        resDto.setLastUpdateUser(TmPurchase.getLastUpdateUser());
        resDto.setStatus(TmPurchase.getStatus());
        resDto.setPurchasePersonnel(TmPurchase.getPurchasePersonnel());
        resDto.setPkId(TmPurchase.getPkId());
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TmPurchaseResDto>
     */
    public List<TmPurchaseResDto> editResDtoList(List<TmPurchaseEntity> entityList) {
        List<TmPurchaseResDto> resDtoList = new ArrayList<>();
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
     * @return TmPurchaseEntity
     */
    public TmPurchaseEntity editEntity(TmPurchaseReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TmPurchaseEntity entity = new TmPurchaseEntity();
        entity.setPurchaseNo(reqDto.getPurchaseNo());
        entity.setOrderNo(reqDto.getOrderNo());
        entity.setPurchaseStatus(reqDto.getPurchaseStatus());
        entity.setPurchasePersonnel(reqDto.getPurchasePersonnel());
        entity.setTotalAmount(reqDto.getTotalAmount());
        entity.setStatus(reqDto.getStatus());
        entity.setRemarks(reqDto.getRemarks());
        entity.setCreateUser(reqDto.getCreateUser());
        entity.setLastUpdateTime(reqDto.getLastUpdateTime());
        entity.setLastUpdateUser(reqDto.getLastUpdateUser());
        return entity;
    }

    /**
     * reqDto集合转entity集合
     * @param reqDtoList
     * @return List<TmPurchaseEntity>
     */
    public List<TmPurchaseEntity> editEntityList(List<TmPurchaseReqDto> reqDtoList) {
        List<TmPurchaseEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}