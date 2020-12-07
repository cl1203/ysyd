/**
 * UserOrderServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.impl;

import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.dto.sys.req.TrUserOrderReqDto;
import com.cl.ysyd.dto.sys.res.TrUserOrderResDto;
import com.cl.ysyd.entity.sys.TrUserOrderEntity;
import com.cl.ysyd.mapper.sys.TrUserOrderMapper;
import com.cl.ysyd.service.sys.IUserOrderService;
import com.cl.ysyd.service.sys.helper.UserOrderHelper;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户订单关系service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
public class UserOrderServiceImpl implements IUserOrderService {
    /**
     * 用户订单关系Mapper
     */
    @Autowired
    private TrUserOrderMapper trUserOrderMapper;

    /**
     * 用户订单关系Helper
     */
    @Autowired
    private UserOrderHelper userOrderHelper;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TrUserOrderEntity checkEntity = this.trUserOrderMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.trUserOrderMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TrUserOrderResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TrUserOrderEntity entity = this.trUserOrderMapper.selectByPrimaryKey(pkId);
        TrUserOrderResDto resDto = this.userOrderHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createUserOrder(TrUserOrderReqDto reqDto) {
        log.info("Service createUserOrder start. reqDto=【{}】",reqDto);
        TrUserOrderEntity entity = this.userOrderHelper.editEntity(reqDto);
        entity.setCreateTime(new Date());
        // TODO 添加主键
        int ret = this.trUserOrderMapper.insert(entity);
        log.info("Service createUserOrder end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TrUserOrderReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TrUserOrderEntity checkEntity = this.trUserOrderMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TrUserOrderEntity entity = this.userOrderHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.trUserOrderMapper.updateByPrimaryKey(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }
}