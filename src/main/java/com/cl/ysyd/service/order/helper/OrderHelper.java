/**
 * OrderHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.helper;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.DictType;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.dto.order.req.TmOrderReqDto;
import com.cl.ysyd.dto.order.res.TmOrderResDto;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.service.sys.IBizDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class OrderHelper {

    @Autowired
    private IBizDictionaryService iTcDictService;

    /**
     * entity转resDto
     * @param TmOrder
     * @return TmOrderResDto
     */
    public TmOrderResDto editResDto(TmOrderEntity TmOrder) {
        if (TmOrder == null) {
            return null;
        }
        TmOrderResDto resDto = new TmOrderResDto();
        resDto.setCreateUser(TmOrder.getCreateUser());
        resDto.setCreateTime(DateUtil.getDateString(TmOrder.getCreateTime(), DateUtil.DATETIMESHOWFORMAT));
        resDto.setOrderNo(TmOrder.getOrderNo());
        resDto.setExamineStatus(TmOrder.getExamineStatus());
        String examineStatusText = this.iTcDictService.getTextByBizCode(DictType.EXAMINE_STATUS.getCode(), TmOrder.getExamineStatus());
        resDto.setExamineStatusText(examineStatusText);
        resDto.setOrderType(TmOrder.getOrderType());
        String orderTypeText = this.iTcDictService.getTextByBizCode(DictType.ORDER_TYPE.getCode(), TmOrder.getOrderType());
        resDto.setOrderTypeText(orderTypeText);
        resDto.setCompleteDate(DateUtil.getDateString(TmOrder.getCompleteDate(), DateUtil.DATESHOWFORMAT));
        resDto.setRemarks(TmOrder.getRemarks());
        resDto.setDeliveryDate(DateUtil.getDateString(TmOrder.getDeliveryDate(), DateUtil.DATESHOWFORMAT));
        resDto.setLastUpdateTime(DateUtil.getDateString(TmOrder.getLastUpdateTime(), DateUtil.DATETIMESHOWFORMAT));
        resDto.setLastUpdateUser(TmOrder.getLastUpdateUser());
        resDto.setOrderSize(TmOrder.getOrderSize());
        String statusText = this.iTcDictService.getTextByBizCode(DictType.VALID_STATUS.getCode(), String.valueOf(TmOrder.getStatus()));
        resDto.setStatus(String.valueOf(TmOrder.getStatus()));
        resDto.setStatusText(statusText);
        resDto.setPkId(TmOrder.getPkId());
        resDto.setSku(TmOrder.getSku());
        resDto.setOrderPeople(TmOrder.getOrderPeople());
        resDto.setOrderStatus(TmOrder.getOrderStatus());
        String orderStatusText = this.iTcDictService.getTextByBizCode(DictType.ORDER_STATUS.getCode(), TmOrder.getOrderStatus());
        resDto.setOrderStatusText(orderStatusText);
        resDto.setFolderUrl(TmOrder.getFolderUrl());
        resDto.setImgUrl(TmOrder.getImgUrl());
        resDto.setEstablishDate(DateUtil.getDateString(TmOrder.getEstablishDate(), DateUtil.DATESHOWFORMAT));
        resDto.setUnitPrice(String.valueOf(TmOrder.getUnitPrice()));
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TmOrderResDto>
     */
    public List<TmOrderResDto> editResDtoList(List<TmOrderEntity> entityList) {
        List<TmOrderResDto> resDtoList = new ArrayList<>();
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
     * @return TmOrderEntity
     */
    public TmOrderEntity editEntity(TmOrderReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TmOrderEntity entity = new TmOrderEntity();
        entity.setOrderNo(reqDto.getOrderNo());
        entity.setImgUrl(reqDto.getImgUrl());
        entity.setDeliveryDate(DateUtil.getDateToString(reqDto.getDeliveryDate(), DateUtil.DATESHOWFORMAT));
        entity.setUnitPrice(new BigDecimal(reqDto.getUnitPrice()));
        entity.setEstablishDate(DateUtil.dateToDate(new Date(), DateUtil.DATESHOWFORMAT));
        if(StringUtils.isNotBlank(reqDto.getCompleteDate())){
            entity.setCompleteDate(DateUtil.getDateToString(reqDto.getCompleteDate(), DateUtil.DATESHOWFORMAT));
        }
        entity.setOrderStatus(reqDto.getOrderStatus());
        entity.setFolderUrl(reqDto.getFolderUrl());
        entity.setSku(reqDto.getSku());
        entity.setOrderPeople(reqDto.getOrderPeople());
        entity.setOrderSize(reqDto.getOrderSize());
        entity.setOrderType(reqDto.getOrderType());
        entity.setExamineStatus(reqDto.getExamineStatus());
        if(StringUtils.isNotBlank(reqDto.getStatus())){
            entity.setStatus(Byte.valueOf(reqDto.getStatus()));
        }else{
            entity.setStatus(SortConstant.ONE.byteValue());
        }
        entity.setRemarks(reqDto.getRemarks());
        entity.setCreateUser(LoginUtil.getUserId());
        entity.setLastUpdateTime(new Date());
        entity.setLastUpdateUser(LoginUtil.getUserId());
        return entity;
    }

    /**
     * reqDto集合转entity集合
     * @param reqDtoList
     * @return List<TmOrderEntity>
     */
    public List<TmOrderEntity> editEntityList(List<TmOrderReqDto> reqDtoList) {
        List<TmOrderEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}