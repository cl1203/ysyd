/**
 * PurchaseHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.helper;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.DictType;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.CheckMatchAndSpaceUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.dto.order.req.TmPurchaseReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseResDto;
import com.cl.ysyd.entity.order.TmPurchaseEntity;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.mapper.sys.TsUserMapper;
import com.cl.ysyd.service.sys.IBizDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购单帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class PurchaseHelper {

    @Autowired
    private IBizDictionaryService iTcDictService;

    @Autowired
    private TsUserMapper userMapper;


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
        resDto.setOrderNo(TmPurchase.getOrderNo());
        resDto.setPurchaseNo(TmPurchase.getPurchaseNo());
        resDto.setTotalAmount(TmPurchase.getTotalAmount());
        resDto.setRemarks(TmPurchase.getRemarks());
        resDto.setPurchaseStatus(TmPurchase.getPurchaseStatus());
        if(StringUtils.isNotBlank(TmPurchase.getPurchaseStatus())){
            String purchaseStatusText = this.iTcDictService.getTextByBizCode(DictType.PURCHASE_STATUS.getCode(), TmPurchase.getPurchaseStatus());
            resDto.setPurchaseStatusText(purchaseStatusText);
        }
        resDto.setStatus(TmPurchase.getStatus());
        String statusText = this.iTcDictService.getTextByBizCode(DictType.VALID_STATUS.getCode(), TmPurchase.getStatus().toString());
        resDto.setStatusText(statusText);
        if(StringUtils.isNotBlank(TmPurchase.getPurchasePersonnel())){
            resDto.setPurchasePersonnel(TmPurchase.getPurchasePersonnel());
            TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(TmPurchase.getPurchasePersonnel());
            if(null != userEntity){
                resDto.setPurchasePersonnelName(userEntity.getRealName());
            }
        }
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
        //entity.setPurchaseNo(P + reqDto.getOrderNo());
        entity.setOrderNo(reqDto.getOrderNo());
        entity.setPurchaseStatus(reqDto.getPurchaseStatus());
        if(StringUtils.isNotBlank(reqDto.getPurchaseStatus())){
            String purchaseStatusText = this.iTcDictService.getTextByBizCode(DictType.PURCHASE_STATUS.getCode(), reqDto.getPurchaseStatus());
            Assert.hasText(purchaseStatusText, "采购状态不存在, 请修改!");
        }
        if(StringUtils.isNotBlank(reqDto.getPurchasePersonnel())){
            TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(reqDto.getPurchasePersonnel());
            if(null == userEntity){
                throw new BusiException("采购员不存在, 请修改!");
            }
            entity.setPurchasePersonnel(reqDto.getPurchasePersonnel());
        }
        if(StringUtils.isNotBlank(reqDto.getTotalAmount())){
            if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP, reqDto.getTotalAmount())) {
                throw new BusiException("采购总金额不符合规则, 整数位最多8位, 小数位最多2位!");
            }
            entity.setTotalAmount(new BigDecimal(reqDto.getTotalAmount()));
        }
        entity.setStatus(reqDto.getStatus());
        String statusText = this.iTcDictService.getTextByBizCode(DictType.VALID_STATUS.getCode(), reqDto.getStatus().toString());
        Assert.hasText(statusText, "所选状态不存在, 请修改!");
        entity.setRemarks(reqDto.getRemarks());
        entity.setCreateUser(LoginUtil.getUserId());
        entity.setLastUpdateTime(new Date());
        entity.setLastUpdateUser(LoginUtil.getUserId());
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
