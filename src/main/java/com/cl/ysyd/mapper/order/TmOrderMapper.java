/**
 * TmOrderMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.order;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.common.enums.ExamineStatusEnum;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.entity.order.TmOrderEntityExample;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单 mapper类
 * @author chenlong  2020-11-24
 */
public interface TmOrderMapper extends IBaseMapper<TmOrderEntity, TmOrderEntityExample, String> {

    String querySerialNumber(@Param("date") String date);

    default List<TmOrderEntity> queryOrderList(String orderUser, String orderStatus,
                                               String deliveryDate, String establishDate, String completeDate, String examineStatus, String userType){
        //查询
        TmOrderEntityExample orderEntityExample = new TmOrderEntityExample();
        TmOrderEntityExample.Criteria criteria = orderEntityExample.createCriteria();
        if(userType.equals("admin")){
            if(StringUtils.isNotBlank(orderUser)){
                criteria.andOrderUserEqualTo(orderUser);
            }
        }else{
            criteria.andOrderUserEqualTo(LoginUtil.getUserId());
        }
        if(StringUtils.isNotBlank(orderStatus)){
            criteria.andOrderStatusEqualTo(orderStatus);
        }
        if(StringUtils.isNotBlank(deliveryDate)){
            criteria.andDeliveryDateEqualTo(DateUtil.getDateToString(deliveryDate, DateUtil.DATESHOWFORMAT));
        }
        if(StringUtils.isNotBlank(establishDate)){
            criteria.andEstablishDateEqualTo(DateUtil.getDateToString(establishDate, DateUtil.DATESHOWFORMAT));
        }
        if(StringUtils.isNotBlank(completeDate)){
            criteria.andCompleteDateEqualTo(DateUtil.getDateToString(completeDate, DateUtil.DATESHOWFORMAT));
        }
        if(StringUtils.isNotBlank(examineStatus)){
            criteria.andExamineStatusEqualTo(examineStatus);
        }else{
            criteria.andExamineStatusNotEqualTo(ExamineStatusEnum.FAILED.getCode());
        }
        List<TmOrderEntity> orderEntityList = this.selectByExample(orderEntityExample);
        return orderEntityList;
    }

}
