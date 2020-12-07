/**
 * TmOrderMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.order;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.entity.order.TmOrderEntityExample;
import org.apache.ibatis.annotations.Param;

/**
 * 订单 mapper类
 * @author chenlong  2020-11-24
 */
public interface TmOrderMapper extends IBaseMapper<TmOrderEntity, TmOrderEntityExample, String> {

    String querySerialNumber(@Param("date") String date);




}