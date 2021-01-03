/**
 * IOrderService.java
 * Created at 2020-12-07
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order;

import com.cl.ysyd.dto.order.req.TmOrderReqDto;
import com.cl.ysyd.dto.order.res.NoticeTopResDto;
import com.cl.ysyd.dto.order.res.SectorResDto;
import com.cl.ysyd.dto.order.res.TmOrderResDto;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    PageInfo<TmOrderResDto> queryOrderByPage(Integer pageNum, Integer pageSize, String orderUser, String orderStatus, String deliveryDate,
                                             String establishDate, String completeDate, String examineStatus, String status, String orderNo);

    /**
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param orderUser 所属用户
     * @param deliveryDate 交货日期
     * @param establishDate 创建日期
     * @param completeDate 完成日期
     * @return 结果集
     */
    PageInfo<TmOrderResDto> queryOrderBillByPage(Integer pageNum, Integer pageSize, String orderUser,
                                             String deliveryDate, String establishDate, String completeDate);

    /**
     * 分配订单
     * @param orderId 订单ID
     * @param orderUserId 用户Id
     * @return 结果
     */
    void distributionUser(String orderId, String orderUserId);

    /**
     * 操作订单状态
     * @param pkId 订单ID
     * @return 结果
     */
    void updateOrderStatus(String pkId);

    /**
     * 导出
     * @param response 响应
     * @param orderUser 订单所属用户
     * @param orderStatus 订单状态
     * @param deliveryDate 交货日期
     * @param establishDate 订单创建日期
     * @param completeDate 完成日期
     * @throws IOException IO异常
     */
    void export(HttpServletResponse response , String orderUser, String orderStatus,
                String deliveryDate, String establishDate, String completeDate, String status)throws IOException;

    /**
     *
     * @param response 响应
     * @param orderUser 所属用户
     * @param deliveryDate 交货日期
     * @param establishDate 创建日期
     * @param completeDate 完成日期
     * @throws IOException IO异常
     */
    void exportBill(HttpServletResponse response , String orderUser,
                String deliveryDate, String establishDate, String completeDate)throws IOException;

    /**
     * 接单之前的接口, 查询此用户还有多少未完成的订单
     * @return 未完成的订单数量
     */
    int queryNumByUserId();

    /**
     * 接单
     * @param orderType 订单类型
     * @param number 数量
     */
    void connectOrder(String orderType, Integer number);

    /**
     * 退回订单
     * @param pkId 主键订单ID
     */
    void returnOrder(String pkId);

    /**
     *  审核订单状态
     * @param pkId 订单ID
     * @param examineStatus 审核状态
     * @return 结果
     */
    void examineOrder(String pkId, String examineStatus);

    /**
     * 查询首页&看板 上部分内容
     * @return 查询结果
     */
    NoticeTopResDto queryTop();

    /**
     * 查询首页&看板 曲线部分内容
     * @return 查询结果
     */
    Map<String, List<Integer>> queryCurve(String year);

    List<SectorResDto> querySector(String year, String month);

    List<Integer> queryColumnar(String year, String month);
}
