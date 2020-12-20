/**
 * TmPurchaseMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.order;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.dto.order.res.TmPurchaseBillResDto;
import com.cl.ysyd.entity.order.TmPurchaseEntity;
import com.cl.ysyd.entity.order.TmPurchaseEntityExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购单 mapper类
 * @author chenlong  2020-11-24
 */
public interface TmPurchaseMapper extends IBaseMapper<TmPurchaseEntity, TmPurchaseEntityExample, String> {

    List<TmPurchaseEntity> queryPurchaseList(@Param("orderNo") String orderNo, @Param("purchaseNo")String purchaseNo, @Param("purchaseStatus")String purchaseStatus, @Param("purchasePersonnel")String purchasePersonnel,
                                             @Param("orderStatus")String orderStatus, @Param("isAll")String isAll, @Param("userId")String userId);

    List<TmPurchaseBillResDto> queryBillPurchaseList(@Param("purchasePersonnel")String purchasePersonnel, @Param("supplier")String supplier, @Param("purchaseDate") String purchaseDate, @Param("isAll")String isAll, @Param("userId")String userId);

    default List<TmPurchaseEntity> queryByOrderNo(String orderNo){
        TmPurchaseEntityExample purchaseEntityExample = new TmPurchaseEntityExample();
        TmPurchaseEntityExample.Criteria criteria = purchaseEntityExample.createCriteria();
        criteria.andOrderNoEqualTo(orderNo);
        criteria.andStatusEqualTo(SortConstant.ONE.byteValue());
        List<TmPurchaseEntity> purchaseEntityList = this.selectByExample(purchaseEntityExample);
        return purchaseEntityList;
    }

}
