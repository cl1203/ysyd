/**
 * PurchaseDetailServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.impl;

import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.order.req.TmPurchaseDetailReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseDetailResDto;
import com.cl.ysyd.entity.order.TmPurchaseDetailEntity;
import com.cl.ysyd.mapper.order.TmPurchaseDetailMapper;
import com.cl.ysyd.service.order.IPurchaseDetailService;
import com.cl.ysyd.service.order.helper.PurchaseDetailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 采购单明细service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
public class PurchaseDetailServiceImpl implements IPurchaseDetailService {
    /**
     * 采购单明细Mapper
     */
    @Autowired
    private TmPurchaseDetailMapper tmPurchaseDetailMapper;

    /**
     * 采购单明细Helper
     */
    @Autowired
    private PurchaseDetailHelper purchaseDetailHelper;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TmPurchaseDetailEntity checkEntity = this.tmPurchaseDetailMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.tmPurchaseDetailMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TmPurchaseDetailResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TmPurchaseDetailEntity entity = this.tmPurchaseDetailMapper.selectByPrimaryKey(pkId);
        TmPurchaseDetailResDto resDto = this.purchaseDetailHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createPurchaseDetail(TmPurchaseDetailReqDto reqDto) {
        log.info("Service createPurchaseDetail start. reqDto=【{}】",reqDto);
        TmPurchaseDetailEntity entity = this.purchaseDetailHelper.editEntity(reqDto);
        entity.setCreateTime(new Date());
        entity.setPkId(UuidUtil.getUuid());
        int ret = this.tmPurchaseDetailMapper.insert(entity);
        log.info("Service createPurchaseDetail end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TmPurchaseDetailReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TmPurchaseDetailEntity checkEntity = this.tmPurchaseDetailMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TmPurchaseDetailEntity entity = this.purchaseDetailHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.tmPurchaseDetailMapper.updateByPrimaryKey(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }
}
