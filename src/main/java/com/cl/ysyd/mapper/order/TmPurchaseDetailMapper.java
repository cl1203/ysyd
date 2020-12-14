/**
 * TmPurchaseDetailMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.order;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.entity.order.TmPurchaseDetailEntity;
import com.cl.ysyd.entity.order.TmPurchaseDetailEntityExample;

import java.util.List;

/**
 * 采购单明细 mapper类
 * @author chenlong  2020-11-24
 */
public interface TmPurchaseDetailMapper extends IBaseMapper<TmPurchaseDetailEntity, TmPurchaseDetailEntityExample, String> {

    /**
     * 根据采购单号删除明细
     * @param purchaseNo  采购单号
     */

    default void deleteByPurchaseNo(String purchaseNo) {
        TmPurchaseDetailEntityExample purchaseDetailEntityExample = new TmPurchaseDetailEntityExample();
        TmPurchaseDetailEntityExample.Criteria criteria = purchaseDetailEntityExample.createCriteria();
        criteria.andPurchaseNoEqualTo(purchaseNo);
        this.deleteByExample(purchaseDetailEntityExample);
    }

    /**
     * 根据采购单号查询所有明细
     * @param purchaseNo 采购单号
     * @return 采购明细list
     */
    default List<TmPurchaseDetailEntity> queryByPurchaseNo(String purchaseNo){
        TmPurchaseDetailEntityExample purchaseDetailEntityExample = new TmPurchaseDetailEntityExample();
        TmPurchaseDetailEntityExample.Criteria criteria = purchaseDetailEntityExample.createCriteria();
        criteria.andPurchaseNoEqualTo(purchaseNo);
        List<TmPurchaseDetailEntity> tmPurchaseDetailEntityList = this.selectByExample(purchaseDetailEntityExample);
        return tmPurchaseDetailEntityList;
    }
}
