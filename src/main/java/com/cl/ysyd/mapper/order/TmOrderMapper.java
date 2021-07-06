/**
 * TmOrderMapper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.order;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.AuditStatusEnum;
import com.cl.ysyd.common.enums.ExamineStatusEnum;
import com.cl.ysyd.common.enums.IsValidEnum;
import com.cl.ysyd.common.enums.OrderStatusEnum;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.dto.order.res.CurveResDto;
import com.cl.ysyd.dto.order.res.SectorResDto;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.entity.order.TmOrderEntityExample;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 订单 mapper类
 * @author chenlong  2020-11-24
 */
public interface TmOrderMapper extends IBaseMapper<TmOrderEntity, TmOrderEntityExample, String> {

    String querySerialNumber(@Param("date") String date);

    List<TmOrderEntity> queryList(@Param("orderUserList") List<String > orderUserList, @Param("orderStatusList") List<String> orderStatusList,
                                  @Param("deliveryDateStart") String deliveryDateStart, @Param("deliveryDateEnd") String deliveryDateEnd,
                                  @Param("establishDateStart") String establishDateStart, @Param("establishDateEnd") String establishDateEnd,
                                  @Param("completeDateStart") String completeDateStart, @Param("completeDateEnd") String completeDateEnd,
                                  @Param("examineStatus") String examineStatus, @Param("isAll") String isAll,
                                  @Param("status") String status, @Param("orderNo") String orderNo, @Param("userId") String userId);

    /**
     * 分页查询
     * @param orderUser 订单所属用户
     * @param orderStatus 订单状态
     * @param deliveryDate 交货日期
     * @param establishDate 创建日期
     * @param completeDate 完成日期
     * @param examineStatus 审核状态
     * @param isAll 是否拥有所有数据权限
     * @return 列表结果集
     */
    default List<TmOrderEntity> queryOrderList(String orderUser, String orderStatus,String deliveryDate, String establishDate,
                                               String completeDate, String examineStatus, String isAll, String status, String orderNo){
        //查询
        TmOrderEntityExample orderEntityExample = new TmOrderEntityExample();
        TmOrderEntityExample.Criteria criteria = orderEntityExample.createCriteria();
        if(StringUtils.isNotBlank(orderNo)){
            criteria.andOrderNoEqualTo(orderNo);
        }
        if(isAll.equals(AuditStatusEnum.REVIEWED.getCode())){
            if(StringUtils.isNotBlank(orderUser)){
                if(orderUser.contains(",")){
                    List<String> orderUserList = Arrays.asList(orderUser.split(","));
                    criteria.andOrderUserIn(orderUserList);
                }else{
                    criteria.andOrderUserEqualTo(orderUser);
                }
            }
        }/*else{
            criteria.andOrderUserEqualTo(LoginUtil.getUserId());
        }*/
        if(StringUtils.isNotBlank(orderStatus)){
            if(orderStatus.contains(",")){
                List<String> orderStatusList = Arrays.asList(orderStatus.split(","));
                criteria.andOrderStatusIn(orderStatusList);
            }else{
                criteria.andOrderStatusEqualTo(orderStatus);
            }
        }
        if(StringUtils.isNotBlank(deliveryDate)){
            if(deliveryDate.contains(",")){
                List<String> deliveryDateList = Arrays.asList(deliveryDate.split(","));
                Date deliveryDateStart = DateUtil.getDateToString(deliveryDateList.get(SortConstant.ZERO), DateUtil.DATESHOWFORMAT);
                Date deliveryDateEnd = DateUtil.getDateToString(deliveryDateList.get(SortConstant.ONE), DateUtil.DATESHOWFORMAT);
                criteria.andDeliveryDateBetween(deliveryDateStart, deliveryDateEnd);
            }
            //criteria.andDeliveryDateEqualTo(DateUtil.getDateToString(deliveryDate, DateUtil.DATESHOWFORMAT));
        }
        if(StringUtils.isNotBlank(establishDate)){
            if(establishDate.contains(",")){
                List<String> establishDateList = Arrays.asList(establishDate.split(","));
                Date establishDateStart = DateUtil.getDateToString(establishDateList.get(SortConstant.ZERO), DateUtil.DATESHOWFORMAT);
                Date establishDateEnd = DateUtil.getDateToString(establishDateList.get(SortConstant.ONE), DateUtil.DATESHOWFORMAT);
                criteria.andEstablishDateBetween(establishDateStart, establishDateEnd);
            }
            //criteria.andEstablishDateEqualTo(DateUtil.getDateToString(establishDate, DateUtil.DATESHOWFORMAT));
        }
        if(StringUtils.isNotBlank(completeDate)){
            if(completeDate.contains(",")){
                List<String> completeDateList = Arrays.asList(completeDate.split(","));
                Date completeDateStart = DateUtil.getDateToString(completeDateList.get(SortConstant.ZERO), DateUtil.DATESHOWFORMAT);
                Date completeDateEnd = DateUtil.getDateToString(completeDateList.get(SortConstant.ONE), DateUtil.DATESHOWFORMAT);
                criteria.andCompleteDateBetween(completeDateStart, completeDateEnd);
            }
            //criteria.andCompleteDateEqualTo(DateUtil.getDateToString(completeDate, DateUtil.DATESHOWFORMAT));
        }

        if(StringUtils.isNotBlank(examineStatus)){
            criteria.andExamineStatusEqualTo(examineStatus);
        }else{
            List<String> examineStatusList = new ArrayList<>();
            examineStatusList.add(ExamineStatusEnum.ADOPT.getCode());
            examineStatusList.add(ExamineStatusEnum.NOT.getCode());
            criteria.andExamineStatusIn(examineStatusList);
        }
        if(StringUtils.isNotBlank(status)){
            criteria.andStatusEqualTo(Byte.valueOf(status));
        }
        return this.selectByExample(orderEntityExample);
    }


    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单对象
     */
    default TmOrderEntity queryByOrderNo(String orderNo){
        //查询
        TmOrderEntityExample orderEntityExample = new TmOrderEntityExample();
        TmOrderEntityExample.Criteria criteria = orderEntityExample.createCriteria();
        criteria.andStatusEqualTo(SortConstant.ONE.byteValue());
        criteria.andOrderNoEqualTo(orderNo);
        List<TmOrderEntity> orderEntityList = this.selectByExample(orderEntityExample);
        return CollectionUtils.isNotEmpty(orderEntityList) ? orderEntityList.get(SortConstant.ZERO) : null;
    }


    List<TmOrderEntity> queryBillList(@Param("orderUser") String orderUser, @Param("isAll") String isAll, @Param("userId") String userId,
                                      @Param("deliveryDateStart") String deliveryDateStart, @Param("deliveryDateEnd") String deliveryDateEnd,
                                      @Param("establishDateStart") String establishDateStart, @Param("establishDateEnd") String establishDateEnd,
                                      @Param("completeDateStart") String completeDateStart, @Param("completeDateEnd") String completeDateEnd);

    /**
     * 对账单分页查询
     * @param orderUser 订单所属用户
     * @param deliveryDate 交货日期
     * @param establishDate 创建日期
     * @param completeDate 完成日期
     * @param isAll 是否拥有所有数据权限
     * @return 列表结果集
     */
    default List<TmOrderEntity> queryOrderList(String orderUser, String deliveryDate, String establishDate,
                                               String completeDate, String isAll){
        //查询
        TmOrderEntityExample orderEntityExample = new TmOrderEntityExample();
        TmOrderEntityExample.Criteria criteria = orderEntityExample.createCriteria();
        if(isAll.equals(AuditStatusEnum.REVIEWED.getCode())){
            if(StringUtils.isNotBlank(orderUser)){
                criteria.andOrderUserEqualTo(orderUser);
            }
        }/*else{
            criteria.andOrderUserEqualTo(LoginUtil.getUserId());
        }*/
        if(StringUtils.isNotBlank(deliveryDate)){
            criteria.andDeliveryDateEqualTo(DateUtil.getDateToString(deliveryDate, DateUtil.DATESHOWFORMAT));
        }
        if(StringUtils.isNotBlank(establishDate)){
            criteria.andEstablishDateEqualTo(DateUtil.getDateToString(establishDate, DateUtil.DATESHOWFORMAT));
        }
        if(StringUtils.isNotBlank(completeDate)){
            criteria.andCompleteDateEqualTo(DateUtil.getDateToString(completeDate, DateUtil.DATESHOWFORMAT));
        }
        criteria.andStatusEqualTo(IsValidEnum.VALID.getCode().byteValue());
        return this.selectByExample(orderEntityExample);
    }

    /**
     * 根据用户查询名下未完成订单数量
     * @param userId 用户id
     * @return 未完成订单数量
     */
    default int queryNumByUserId(String userId){
        TmOrderEntityExample orderEntityExample = new TmOrderEntityExample();
        TmOrderEntityExample.Criteria criteria = orderEntityExample.createCriteria();
        criteria.andOrderUserEqualTo(userId);
        criteria.andOrderStatusNotEqualTo(OrderStatusEnum.COMPLETED.getCode());
        criteria.andStatusEqualTo(SortConstant.ONE.byteValue());
        List<TmOrderEntity> orderEntityList = this.selectByExample(orderEntityExample);
        return orderEntityList.size();
    }

    /**
     * 查询所选择类型的订单还有多少待接单
     * @param orderType 订单类型
     * @param pageSize 分页数量
     * @return 待结单结果集
     */
    default PageInfo<TmOrderEntity> queryWaitingByOrderType(String orderType, Integer pageSize){
        TmOrderEntityExample orderEntityExample = new TmOrderEntityExample();
        TmOrderEntityExample.Criteria criteria = orderEntityExample.createCriteria();
        criteria.andOrderTypeEqualTo(orderType);
        criteria.andOrderStatusEqualTo(OrderStatusEnum.WAITING.getCode());
        criteria.andStatusEqualTo(SortConstant.ONE.byteValue());
        PageHelper.orderBy("ORDER_NO");
        Page<TmOrderEntity> startPage = PageHelper.startPage(SortConstant.ONE, pageSize);
        PageInfo<TmOrderEntity> pageInfo = new PageInfo<>(startPage);
        List<TmOrderEntity> orderEntityList = this.selectByExample(orderEntityExample);
        pageInfo.setList(orderEntityList);
        return pageInfo;
    }

    /**
     * 查询所有类型的订单还有多少待接单
     * @param pageSize 分页数量
     * @return 待结单结果集
     */
    default PageInfo<TmOrderEntity> queryWaiting(Integer pageSize){
        TmOrderEntityExample orderEntityExample = new TmOrderEntityExample();
        TmOrderEntityExample.Criteria criteria = orderEntityExample.createCriteria();
        criteria.andOrderStatusEqualTo(OrderStatusEnum.WAITING.getCode());
        criteria.andStatusEqualTo(SortConstant.ONE.byteValue());
        PageHelper.orderBy("ORDER_NO");
        Page<TmOrderEntity> startPage = PageHelper.startPage(SortConstant.ONE, pageSize);
        PageInfo<TmOrderEntity> pageInfo = new PageInfo<>(startPage);
        List<TmOrderEntity> orderEntityList = this.selectByExample(orderEntityExample);
        pageInfo.setList(orderEntityList);
        return pageInfo;
    }

    /**
     * 修改审核状态
     * @param pkId 订单ID
     * @param examineStatus 审核状态
     * @return 修改结果
     */
    default int examineOrder(String pkId, String examineStatus){
        TmOrderEntity orderEntity = new TmOrderEntity();
        orderEntity.setPkId(pkId);
        orderEntity.setExamineStatus(examineStatus);
        return this.updateByPrimaryKeySelective(orderEntity);
    }

    /**
     * 根据主键和订单状态修改订单状态
     * @param pkId 订单ID
     * @param orderStatus 订单状态
     * @return 修改结果
     */
    default int updateOrderStatus(String pkId , String orderStatus){
        TmOrderEntity orderEntity = new TmOrderEntity();
        orderEntity.setPkId(pkId);
        orderEntity.setOrderStatus(orderStatus);
        return this.updateByPrimaryKeySelective(orderEntity);
    }


    /**
     * 按当前年份查询 订单总数
     */
    Long selectOrderNum();

    /**
     * 订单完成总数
     */
    Long selectOrderCompleteNum();

    /**
     * 订单作废总数
     */
    Long selectOrderAbolishNum();

    /**
     * 订单总金额
     */
    BigDecimal selectOrderTotalMoney();

    /**
     * 采购总金额
     */
    BigDecimal selectPuchaseTotalMoney();

    List<CurveResDto> queryCurve(@Param("year") String year, @Param("completed") String completed);

    List<SectorResDto> querySector(@Param("ym")String ym);

    List<SectorResDto> queryColumnar(@Param("ym")String ym);



}
