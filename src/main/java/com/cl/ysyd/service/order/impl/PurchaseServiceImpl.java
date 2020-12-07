/**
 * PurchaseServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.impl;

import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.dto.order.req.TmPurchaseReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseResDto;
import com.cl.ysyd.entity.order.TmPurchaseEntity;
import com.cl.ysyd.mapper.order.TmPurchaseMapper;
import com.cl.ysyd.service.order.IPurchaseService;
import com.cl.ysyd.service.order.helper.PurchaseHelper;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 采购单service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
public class PurchaseServiceImpl implements IPurchaseService {
    /**
     * 采购单Mapper
     */
    @Autowired
    private TmPurchaseMapper tmPurchaseMapper;

    /**
     * 采购单Helper
     */
    @Autowired
    private PurchaseHelper purchaseHelper;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TmPurchaseEntity checkEntity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.tmPurchaseMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TmPurchaseResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TmPurchaseEntity entity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        TmPurchaseResDto resDto = this.purchaseHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createPurchase(TmPurchaseReqDto reqDto) {
        log.info("Service createPurchase start. reqDto=【{}】",reqDto);
        TmPurchaseEntity entity = this.purchaseHelper.editEntity(reqDto);
        entity.setCreateTime(new Date());
        // TODO 添加主键
        int ret = this.tmPurchaseMapper.insert(entity);
        log.info("Service createPurchase end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TmPurchaseReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TmPurchaseEntity checkEntity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TmPurchaseEntity entity = this.purchaseHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.tmPurchaseMapper.updateByPrimaryKey(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }
}