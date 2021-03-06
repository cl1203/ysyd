/**
 * OrderHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.helper;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.DictType;
import com.cl.ysyd.common.enums.ExamineStatusEnum;
import com.cl.ysyd.common.enums.OrderStatusEnum;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.CheckMatchAndSpaceUtil;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.dto.order.req.TmOrderReqDto;
import com.cl.ysyd.dto.order.res.TmOrderResDto;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.entity.order.TmPurchaseEntity;
import com.cl.ysyd.entity.order.TmPurchaseEntityExample;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.mapper.order.TmOrderMapper;
import com.cl.ysyd.mapper.order.TmPurchaseMapper;
import com.cl.ysyd.mapper.sys.TsUserMapper;
import com.cl.ysyd.service.sys.IBizDictionaryService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 订单帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class OrderHelper {

    @Autowired
    private IBizDictionaryService iTcDictService;

    @Autowired
    private TsUserMapper userMapper;

    @Autowired
    private TmPurchaseMapper purchaseMapper;

    @Autowired
    private TmOrderMapper tmOrderMapper;

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
        String statusText = this.iTcDictService.getTextByBizCode(DictType.IS_CANCEL.getCode(), String.valueOf(TmOrder.getStatus()));
        resDto.setStatus(String.valueOf(TmOrder.getStatus()));
        resDto.setStatusText(statusText);
        resDto.setPkId(TmOrder.getPkId());
        resDto.setSku(TmOrder.getSku());
        resDto.setOrderPeople(TmOrder.getOrderPeople());
        TsUserEntity userEntityPeople = this.userMapper.selectByPrimaryKey(TmOrder.getOrderPeople());
        resDto.setOrderPeopleName(null != userEntityPeople ? userEntityPeople.getRealName() : "");
        resDto.setOrderStatus(TmOrder.getOrderStatus());
        String orderStatusText = this.iTcDictService.getTextByBizCode(DictType.ORDER_STATUS.getCode(), TmOrder.getOrderStatus());
        resDto.setOrderStatusText(orderStatusText);
        resDto.setFolderUrl(TmOrder.getFolderUrl());
        resDto.setFolderUrl2(TmOrder.getFolderUrl2());
        if(null != TmOrder.getOrderNumber()){
            resDto.setOrderNumber(TmOrder.getOrderNumber().toString());
        }
        resDto.setImgUrl(TmOrder.getImgUrl());
        resDto.setEstablishDate(DateUtil.getDateString(TmOrder.getEstablishDate(), DateUtil.DATESHOWFORMAT));
        resDto.setUnitPrice(String.valueOf(TmOrder.getUnitPrice()));
        resDto.setOrderUser(TmOrder.getOrderUser());
        TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(TmOrder.getOrderUser());
        if(null != userEntity){
            resDto.setOrderUserName(userEntity.getRealName());
            String type = userEntity.getType();
            String userTypeText = this.iTcDictService.getTextByBizCode(DictType.USER_TYPE.getCode(), type);
            if(StringUtils.isNotBlank(userTypeText)){
                resDto.setUserTypeText(userTypeText);
            }
        }
        //根据订单号查询是否已经生成采购单
        TmPurchaseEntityExample purchaseEntityExample = new TmPurchaseEntityExample();
        TmPurchaseEntityExample.Criteria criteria = purchaseEntityExample.createCriteria();
        criteria.andOrderNoEqualTo(TmOrder.getOrderNo());
        criteria.andStatusEqualTo(SortConstant.ONE.byteValue());
        List<TmPurchaseEntity> purchaseEntityList = this.purchaseMapper.selectByExample(purchaseEntityExample);
        if(CollectionUtils.isNotEmpty(purchaseEntityList)){
            resDto.setIsPurchase(SortConstant.ZERO.byteValue());
        }else{
            resDto.setIsPurchase(SortConstant.ONE.byteValue());
        }
        resDto.setOrderColour(TmOrder.getOrderColour());
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
    public TmOrderEntity editEntity(TmOrderReqDto reqDto, TmOrderEntity tmOrderEntity) {
        if (reqDto == null) {
            return null;
        }
        TmOrderEntity entity = new TmOrderEntity();
        entity.setOrderNo(reqDto.getOrderNo());
        if(StringUtils.isNotBlank(reqDto.getImgUrl())){
            entity.setImgUrl(reqDto.getImgUrl());
        }else{
            entity.setImgUrl("http://47.106.34.32/img/替代图20210104104933.jpg");
        }
        entity.setDeliveryDate(DateUtil.getDateToString(reqDto.getDeliveryDate(), DateUtil.DATESHOWFORMAT));
        if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP, reqDto.getUnitPrice())) {
            throw new BusiException("单价不符合规则, 整数位最多8位, 小数位最多2位!");
        }
        entity.setUnitPrice(new BigDecimal(reqDto.getUnitPrice()));
        entity.setEstablishDate(DateUtil.dateToDate(new Date(), DateUtil.DATESHOWFORMAT));
        if(StringUtils.isNotBlank(reqDto.getCompleteDate())){
            entity.setCompleteDate(DateUtil.getDateToString(reqDto.getCompleteDate(), DateUtil.DATESHOWFORMAT));
        }
        entity.setOrderUser(reqDto.getOrderUser());
        if(StringUtils.isNotBlank(reqDto.getOrderUser())){
            TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(reqDto.getOrderUser());
            Assert.notNull(userEntity, "接单人不存在!");
            if(null != tmOrderEntity){
                if(tmOrderEntity.getOrderStatus().equals(OrderStatusEnum.WAITING.getCode())){
                    entity.setOrderStatus(OrderStatusEnum.ORDERING.getCode());
                }
            }else{
                entity.setOrderStatus(OrderStatusEnum.ORDERING.getCode());
            }
        }else{
            if(null == tmOrderEntity){
                entity.setOrderStatus(OrderStatusEnum.WAITING.getCode());
            }
        }
        entity.setFolderUrl(reqDto.getFolderUrl());
        entity.setFolderUrl2(reqDto.getFolderUrl2());
        if(StringUtils.isNotBlank(reqDto.getOrderNumber())){
            String regex="^[1-9]+[0-9]*$";
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(reqDto.getOrderNumber());
            Assert.isTrue(matcher.find(), "订单件数必须为正整数!");
            entity.setOrderNumber(Integer.valueOf(reqDto.getOrderNumber()));
        }
        entity.setSku(reqDto.getSku());
        entity.setOrderPeople(StringUtils.isNotBlank(reqDto.getOrderPeople()) ? reqDto.getOrderPeople() : LoginUtil.getUserId());
        if(StringUtils.isNotBlank(reqDto.getOrderSize())){
            String orderSizeText = this.iTcDictService.getTextByBizCode(DictType.ORDER_SIZE.getCode(), reqDto.getOrderSize());
            Assert.hasText(orderSizeText, "订单尺码不存在");
            entity.setOrderSize(reqDto.getOrderSize());
        }
        if(StringUtils.isNotBlank(reqDto.getOrderType())){
            String orderTypeText = this.iTcDictService.getTextByBizCode(DictType.ORDER_TYPE.getCode(), reqDto.getOrderType());
            Assert.hasText(orderTypeText, "订单类型不存在");
            entity.setOrderType(reqDto.getOrderType());
        }
        entity.setOrderColour(reqDto.getOrderColour());
        /*if(StringUtils.isNotBlank(reqDto.getExamineStatus())){
            String examinesStatusText = this.iTcDictService.getTextByBizCode(DictType.EXAMINE_STATUS.getCode(), reqDto.getExamineStatus());
            Assert.hasText(examinesStatusText, "订单审核状态不存在");
            entity.setExamineStatus(reqDto.getExamineStatus());
        }*/
        entity.setExamineStatus(ExamineStatusEnum.NOT.getCode());
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
            entityList.add(this.editEntity(reqDto, null));
        });
        return entityList;
    }
}
