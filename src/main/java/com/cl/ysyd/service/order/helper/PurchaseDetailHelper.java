/**
 * PurchaseDetailHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.helper;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.DictType;
import com.cl.ysyd.common.enums.IsValidEnum;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.CheckMatchAndSpaceUtil;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.dto.order.req.TmPurchaseDetailReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseDetailResDto;
import com.cl.ysyd.entity.order.TmPurchaseDetailEntity;
import com.cl.ysyd.service.sys.IBizDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购单明细帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class PurchaseDetailHelper {

    @Autowired
    private IBizDictionaryService iTcDictService;

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
        resDto.setCreateTime(DateUtil.getDateString(TmPurchaseDetail.getCreateTime(), DateUtil.DATETIMESHOWFORMAT));
        resDto.setQuantity(TmPurchaseDetail.getQuantity());
        resDto.setPurchaseNo(TmPurchaseDetail.getPurchaseNo());
        resDto.setTotalAmount(TmPurchaseDetail.getTotalAmount());
        resDto.setRemarks(TmPurchaseDetail.getRemarks());
        resDto.setLastUpdateTime(DateUtil.getDateString(TmPurchaseDetail.getLastUpdateTime(), DateUtil.DATETIMESHOWFORMAT));
        resDto.setLastUpdateUser(TmPurchaseDetail.getLastUpdateUser());
        resDto.setStatus(TmPurchaseDetail.getStatus());
        String statusText = this.iTcDictService.getTextByBizCode(DictType.VALID_STATUS.getCode(), String.valueOf(TmPurchaseDetail.getStatus()));
        resDto.setStatusText(statusText);
        resDto.setPurchaseNumber(TmPurchaseDetail.getPurchaseNumber());
        resDto.setMaterielName(TmPurchaseDetail.getMaterielName());
        resDto.setWidthOfCloth(TmPurchaseDetail.getWidthOfCloth());
        resDto.setPkId(TmPurchaseDetail.getPkId());
        resDto.setGramWeight(TmPurchaseDetail.getGramWeight());
        resDto.setSupplier(TmPurchaseDetail.getSupplier());
        resDto.setColour(TmPurchaseDetail.getColour());
        resDto.setPurchaseDate(DateUtil.getDateString(TmPurchaseDetail.getPurchaseDate(), DateUtil.DATESHOWFORMAT));
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
        if(StringUtils.isNotBlank(reqDto.getGramWeight())){
            if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP, reqDto.getGramWeight())) {
                throw new BusiException("克重不符合规则, 整数位最多8位, 小数位最多2位!");
            }
            entity.setGramWeight(new BigDecimal(reqDto.getGramWeight()));
        }

        if(StringUtils.isNotBlank(reqDto.getWidthOfCloth())){
            if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP, reqDto.getWidthOfCloth())) {
                throw new BusiException("幅宽不符合规则, 整数位最多8位, 小数位最多2位!");
            }
            entity.setWidthOfCloth(new BigDecimal(reqDto.getWidthOfCloth()));
        }
        if(StringUtils.isNotBlank(reqDto.getUnitPrice())){
            /*if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP, reqDto.getUnitPrice())) {
                throw new BusiException("采购单价不符合规则, 整数位最多8位, 小数位最多2位!");
            }*/
            entity.setUnitPrice(new BigDecimal(reqDto.getUnitPrice()));
        }
        entity.setUnit(reqDto.getUnit());
        entity.setSupplier(reqDto.getSupplier());
        entity.setColour(reqDto.getColour());
        if(StringUtils.isNotBlank(reqDto.getQuantity())){
            /*if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP_INT, reqDto.getQuantity())) {
                throw new BusiException("数量不符合规则, 整数位最多10位, 不能有小数位!");
            }*/
            entity.setQuantity(Integer.valueOf(reqDto.getQuantity()));
        }
        if(null != reqDto.getTotalAmountDetail()){
            /*if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP, reqDto.getTotalAmountDetail())) {
                throw new BusiException("采购明细总价不符合规则, 整数位最多8位, 小数位最多2位!");
            }*/
            entity.setTotalAmount(reqDto.getTotalAmountDetail());
        }
        if(StringUtils.isNotBlank(reqDto.getPurchaseDate())){
            entity.setPurchaseDate(DateUtil.getDateToString(reqDto.getPurchaseDate(), DateUtil.DATESHOWFORMAT));
        }else{
            entity.setPurchaseDate(DateUtil.dateToDate(new Date(), DateUtil.DATESHOWFORMAT));
        }
        /*String statusText = this.iTcDictService.getTextByBizCode(DictType.VALID_STATUS.getCode(), reqDto.getStatus().toString());
        Assert.hasText(statusText, "所选状态不存在, 请修改!");*/
        entity.setStatus(IsValidEnum.VALID.getCode().byteValue());
        entity.setRemarks(reqDto.getRemarks());
        entity.setCreateUser(LoginUtil.getUserId());
        entity.setLastUpdateTime(new Date());
        entity.setLastUpdateUser(LoginUtil.getUserId());
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
