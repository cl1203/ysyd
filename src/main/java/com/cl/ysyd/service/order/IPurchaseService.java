/**
 * IPurchaseService.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order;

import com.cl.ysyd.dto.order.req.TmPurchaseReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseBillResDto;
import com.cl.ysyd.dto.order.res.TmPurchaseResDto;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 采购单 service接口类
 * @author chenlong  2020-11-24 23:42:08
 */
public interface IPurchaseService {
    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return int
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return TmPurchaseResDto
     */
    TmPurchaseResDto queryByPrimaryKey(String pkId);

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return int
     */
    int createPurchase(TmPurchaseReqDto reqDto);

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return int
     */
    int updateByPrimaryKey(String pkId, TmPurchaseReqDto reqDto);

    /**
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param orderNo 订单号
     * @param purchaseNo 采购单号
     * @param purchaseStatus 采购状态
     * @param purchasePersonnel 采购员
     * @param orderStatus 订单状态
     * @return 采购列表结果集
     */
    PageInfo<TmPurchaseResDto> queryPurchaseByPage(Integer pageNum, Integer pageSize, String orderNo, String purchaseNo,
                                                   String purchaseStatus, String purchasePersonnel, String orderStatus);

    /**
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param purchasePersonnel 采购员ID
     * @param supplier 供应商
     * @param purchaseDate 采购日期
     * @return 结果集
     */
    PageInfo<TmPurchaseBillResDto> queryPurchaseBillByPage(Integer pageNum, Integer pageSize, String purchasePersonnel, String supplier, String purchaseDate);

    /**
     * 完成采购单
     * @param pkId 采购单ID
     * @param userId 用户ID
     * @return 修改结果
     */
    int completeByPrimaryKeyApp(String pkId, String userId);

    /**
     * 完成采购单
     * @param pkId 采购单ID
     * @return 修改结果
     */
    int completeByPrimaryKey(String pkId);

    /**
     *
     * @param response 响应
     * @param orderNo  订单号
     * @param purchaseNo 采购单号
     * @param purchaseStatus 采购状态
     * @param purchasePersonnel 采购人
     * @param orderStatus 订单状态
     * @throws IOException io异常
     */
    void export(HttpServletResponse response, String orderNo, String purchaseNo,
                String purchaseStatus, String purchasePersonnel, String orderStatus)throws IOException;

    /**
     *
     * @param response 响应
     * @param purchasePersonnel 采购员
     * @param supplier 供应商
     * @param purchaseDate 采购日期
     * @throws IOException io异常
     */
    void exportPurchaseBill(HttpServletResponse response, String purchasePersonnel, String supplier, String purchaseDate)throws IOException;
}
