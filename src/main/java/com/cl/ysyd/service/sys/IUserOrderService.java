/**
 * IUserOrderService.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys;

import com.cl.ysyd.dto.sys.req.TrUserOrderReqDto;
import com.cl.ysyd.dto.sys.res.TrUserOrderResDto;

/**
 * 用户订单关系 service接口类
 * @author chenlong  2020-11-24 23:04:58
 */
public interface IUserOrderService {
    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return int
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return TrUserOrderResDto
     */
    TrUserOrderResDto queryByPrimaryKey(String pkId);

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return int
     */
    int createUserOrder(TrUserOrderReqDto reqDto);

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return int
     */
    int updateByPrimaryKey(String pkId, TrUserOrderReqDto reqDto);
}