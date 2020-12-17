/**
 * IPurchaseService.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order;

import com.cl.ysyd.dto.order.req.TmPurchaseReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseResDto;
import com.github.pagehelper.PageInfo;

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

    PageInfo<TmPurchaseResDto> queryPurchaseByPage(Integer pageNum, Integer pageSize, String orderNo, String purchaseNo,
                                                   String purchaseStatus, String purchasePersonnel, String orderStatus);

    int completeByPrimaryKeyApp(String pkId, String userId);

    int completeByPrimaryKey(String pkId);
}
