/**
 * TmPurchaseMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.order;

import com.cl.ysyd.common.base.IBaseMapper;
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
                                             @Param("orderStatus")String orderStatus, @Param("userType")String userType, @Param("userId")String userId);

}
