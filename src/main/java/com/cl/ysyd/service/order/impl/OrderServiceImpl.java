/**
 * OrderServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.impl;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.order.req.TmOrderReqDto;
import com.cl.ysyd.dto.order.res.TmOrderResDto;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.mapper.order.TmOrderMapper;
import com.cl.ysyd.service.order.IOrderService;
import com.cl.ysyd.service.order.helper.OrderHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 订单service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements IOrderService {
    /**
     * 订单Mapper
     */
    @Autowired
    private TmOrderMapper tmOrderMapper;

    /**
     * 订单Helper
     */
    @Autowired
    private OrderHelper orderHelper;

    private final static String SERIAL = "00001";
    private final static String END = "23:59:59";
    private final static int LENGTH = 5;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TmOrderEntity checkEntity = this.tmOrderMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.tmOrderMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TmOrderResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TmOrderEntity entity = this.tmOrderMapper.selectByPrimaryKey(pkId);
        TmOrderResDto resDto = this.orderHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createOrder(TmOrderReqDto reqDto) {
        log.info("Service createOrder start. reqDto=【{}】",reqDto);
        TmOrderEntity entity = this.orderHelper.editEntity(reqDto);
        //当前年月日
        String dateString = DateUtil.getDateString(new Date(), DateUtil.DATETIMESHOWFORMAT4);
        //获取当前流水号
        String date = DateUtil.getDateString(new Date(), DateUtil.DATESHOWFORMAT);
        String orderNoMax = this.tmOrderMapper.querySerialNumber(date);
        //订单号
        String orderNo;
        if(StringUtils.isNotBlank(orderNoMax)){
            int length = orderNoMax.length();
            String serialNumber = orderNoMax.substring(length - LENGTH, length);
            orderNo = String.valueOf(Long.parseLong(dateString + serialNumber) + SortConstant.ONE);
        }else{
            orderNo = dateString + SERIAL;
        }
        entity.setOrderNo(orderNo);
        entity.setCreateTime(new Date());
        entity.setPkId(UuidUtil.getUuid());
        int ret = this.tmOrderMapper.insertSelective(entity);
        log.info("Service createOrder end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TmOrderReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TmOrderEntity checkEntity = this.tmOrderMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TmOrderEntity entity = this.orderHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.tmOrderMapper.updateByPrimaryKey(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }
}