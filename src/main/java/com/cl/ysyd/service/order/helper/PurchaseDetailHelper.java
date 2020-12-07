/**
 * PurchaseDetailHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.helper;

import com.cl.ysyd.dto.order.req.TmPurchaseDetailReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseDetailResDto;
import com.cl.ysyd.entity.order.TmPurchaseDetailEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 采购单明细帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class PurchaseDetailHelper {

    /**
     * entity转resDto
     * @param TmPurchaseDetail
     * @return TmPurchaseDetailResDto
     */
    public TmPurchaseDetailResDto editResDto(TmPurchaseDetailEntity TmPurchaseDetail) {
        if (TmPurchaseDetail == null) {
            return null;
        }
        TmPurchaseDetailResDto resDto = new TmPurchaseDetailResDto();
        resDto.setCreateUser(TmPurchaseDetail.getCreateUser());
        resDto.setCreateTime(TmPurchaseDetail.getCreateTime());
        resDto.setQuantity(TmPurchaseDetail.getQuantity());
        resDto.setPurchaseNo(TmPurchaseDetail.getPurchaseNo());
        resDto.setTotalAmount(TmPurchaseDetail.getTotalAmount());
        resDto.setRemarks(TmPurchaseDetail.getRemarks());
        resDto.setLastUpdateTime(TmPurchaseDetail.getLastUpdateTime());
        resDto.setLastUpdateUser(TmPurchaseDetail.getLastUpdateUser());
        resDto.setStatus(TmPurchaseDetail.getStatus());
        resDto.setPurchaseNumber(TmPurchaseDetail.getPurchaseNumber());
        resDto.setMaterielName(TmPurchaseDetail.getMaterielName());
        resDto.setWidthOfCloth(TmPurchaseDetail.getWidthOfCloth());
        resDto.setPkId(TmPurchaseDetail.getPkId());
        resDto.setGramWeight(TmPurchaseDetail.getGramWeight());
        resDto.setSupplier(TmPurchaseDetail.getSupplier());
        resDto.setColour(TmPurchaseDetail.getColour());
        resDto.setPurchaseDate(TmPurchaseDetail.getPurchaseDate());
        resDto.setUnit(TmPurchaseDetail.getUnit());
        resDto.setUnitPrice(TmPurchaseDetail.getUnitPrice());
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TmPurchaseDetailResDto>
     */
    public List<TmPurchaseDetailResDto> editResDtoList(List<TmPurchaseDetailEntity> entityList) {
        List<TmPurchaseDetailResDto> resDtoList = new ArrayList<>();
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
     * @return TmPurchaseDetailEntity
     */
    public TmPurchaseDetailEntity editEntity(TmPurchaseDetailReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TmPurchaseDetailEntity entity = new TmPurchaseDetailEntity();
        entity.setPurchaseNo(reqDto.getPurchaseNo());
        entity.setPurchaseNumber(reqDto.getPurchaseNumber());
        entity.setMaterielName(reqDto.getMaterielName());
        entity.setGramWeight(reqDto.getGramWeight());
        entity.setWidthOfCloth(reqDto.getWidthOfCloth());
        entity.setUnitPrice(reqDto.getUnitPrice());
        entity.setUnit(reqDto.getUnit());
        entity.setSupplier(reqDto.getSupplier());
        entity.setColour(reqDto.getColour());
        entity.setQuantity(reqDto.getQuantity());
        entity.setTotalAmount(reqDto.getTotalAmount());
        entity.setPurchaseDate(reqDto.getPurchaseDate());
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
     * @return List<TmPurchaseDetailEntity>
     */
    public List<TmPurchaseDetailEntity> editEntityList(List<TmPurchaseDetailReqDto> reqDtoList) {
        List<TmPurchaseDetailEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}