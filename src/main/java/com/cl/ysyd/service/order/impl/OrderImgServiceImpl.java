/**
 * OrderImgServiceImpl.java
 * Created at 2021-04-21
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.impl;

import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.dto.order.req.TmOrderImgReqDto;
import com.cl.ysyd.dto.order.res.TmOrderImgResDto;
import com.cl.ysyd.entity.order.TmOrderImgEntity;
import com.cl.ysyd.mapper.order.TmOrderImgMapper;
import com.cl.ysyd.service.order.IOrderImgService;
import com.cl.ysyd.service.order.helper.OrderImgHelper;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author chenlong  2021-04-21
 */
@Service
@Slf4j
public class OrderImgServiceImpl implements IOrderImgService {
    /**
     * Mapper
     */
    @Autowired
    private TmOrderImgMapper tmOrderImgMapper;

    /**
     * Helper
     */
    @Autowired
    private OrderImgHelper orderImgHelper;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TmOrderImgEntity checkEntity = this.tmOrderImgMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.tmOrderImgMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TmOrderImgResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TmOrderImgEntity entity = this.tmOrderImgMapper.selectByPrimaryKey(pkId);
        TmOrderImgResDto resDto = this.orderImgHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createOrderImg(TmOrderImgReqDto reqDto) {
        log.info("Service createOrderImg start. reqDto=【{}】",reqDto);
        TmOrderImgEntity entity = this.orderImgHelper.editEntity(reqDto);
        entity.setCreateTime(new Date());
        // TODO 添加主键
        int ret = this.tmOrderImgMapper.insert(entity);
        log.info("Service createOrderImg end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TmOrderImgReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TmOrderImgEntity checkEntity = this.tmOrderImgMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TmOrderImgEntity entity = this.orderImgHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.tmOrderImgMapper.updateByPrimaryKey(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }
}