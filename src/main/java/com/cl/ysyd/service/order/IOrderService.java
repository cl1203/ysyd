/**
 * IOrderService.java
 * Created at 2020-12-07
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order;

import com.cl.ysyd.dto.order.req.TmOrderReqDto;
import com.cl.ysyd.dto.order.res.TmOrderResDto;
import com.github.pagehelper.PageInfo;

/**
 * 订单 service接口类
 * @author chenlong  2020-12-07 14:42:40
 */
public interface IOrderService {
    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return int
     */
    void cancelByPrimaryKey(String pkId);

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
    void createOrder(TmOrderReqDto reqDto);

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return int
     */
    void updateByPrimaryKey(String pkId, TmOrderReqDto reqDto);

    /**
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param orderUser 所属用户
     * @param orderStatus 订单状态
     * @param deliveryDate 交货日期
     * @param establishDate 创建日期
     * @param completeDate 完成日期
     * @return 列表结果集
     */
    PageInfo<TmOrderResDto> queryOrderByPage(Integer pageNum, Integer pageSize, String orderUser, String orderStatus,
                                             String deliveryDate, String establishDate, String completeDate, String examineStatus);

    /**
     * 分配订单
     * @param orderId 订单ID
     * @param orderUserId 用户Id
     * @return 结果
     */
    void distributionUser(String orderId, String orderUserId);

    /**
     * 操作订单状态
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @return 结果
     */
    void updateOrderStatus(String orderId, String orderStatus);
}
