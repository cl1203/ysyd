/**
 * IOrderService.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order;

import com.cl.ysyd.dto.order.req.TmOrderReqDto;
import com.cl.ysyd.dto.order.res.TmOrderResDto;

/**
 * 订单 service接口类
 * @author chenlong  2020-11-24 23:41:31
 */
public interface IOrderService {
    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return int
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return TmOrderResDto
     */
    TmOrderResDto queryByPrimaryKey(String pkId);

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return int
     */
    int createOrder(TmOrderReqDto reqDto);

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return int
     */
    int updateByPrimaryKey(String pkId, TmOrderReqDto reqDto);
}