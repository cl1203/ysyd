/**
 * PurchaseServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.impl;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.OrderStatusEnum;
import com.cl.ysyd.common.enums.PurchaseStatusEnum;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.CheckMatchAndSpaceUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.order.req.TmPurchaseDetailReqDto;
import com.cl.ysyd.dto.order.req.TmPurchaseReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseDetailResDto;
import com.cl.ysyd.dto.order.res.TmPurchaseResDto;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.entity.order.TmPurchaseDetailEntity;
import com.cl.ysyd.entity.order.TmPurchaseEntity;
import com.cl.ysyd.entity.sys.TsRoleEntity;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.mapper.order.TmOrderMapper;
import com.cl.ysyd.mapper.order.TmPurchaseDetailMapper;
import com.cl.ysyd.mapper.order.TmPurchaseMapper;
import com.cl.ysyd.mapper.sys.TsRoleMapper;
import com.cl.ysyd.mapper.sys.TsUserMapper;
import com.cl.ysyd.service.order.IOrderService;
import com.cl.ysyd.service.order.IPurchaseDetailService;
import com.cl.ysyd.service.order.IPurchaseService;
import com.cl.ysyd.service.order.helper.PurchaseDetailHelper;
import com.cl.ysyd.service.order.helper.PurchaseHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购单service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
@Transactional
public class PurchaseServiceImpl implements IPurchaseService {
    /**
     * 采购单Mapper
     */
    @Autowired
    private TmPurchaseMapper tmPurchaseMapper;

    @Autowired
    private TsUserMapper userMapper;

    private static final String P = "P";

    @Autowired
    private IPurchaseDetailService purchaseDetailService;

    @Autowired
    private TmPurchaseDetailMapper tmPurchaseDetailMapper;

    @Autowired
    private IOrderService iOrderService;
    /**
     * 采购单明细Helper
     */
    @Autowired
    private PurchaseDetailHelper purchaseDetailHelper;

    @Autowired
    private TsRoleMapper roleMapper;

    /**
     * 订单Mapper
     */
    @Autowired
    private TmOrderMapper tmOrderMapper;

    /**
     * 采购单Helper
     */
    @Autowired
    private PurchaseHelper purchaseHelper;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TmPurchaseEntity checkEntity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.tmPurchaseMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TmPurchaseResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TmPurchaseEntity entity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        TmPurchaseResDto resDto = this.purchaseHelper.editResDto(entity);
        List<TmPurchaseDetailEntity> tmPurchaseDetailEntityList = this.tmPurchaseDetailMapper.queryByPurchaseNo(entity.getPurchaseNo());
        List<TmPurchaseDetailResDto> tmPurchaseDetailResDtos = this.purchaseDetailHelper.editResDtoList(tmPurchaseDetailEntityList);
        resDto.setPurchaseDetailReqDtoList(tmPurchaseDetailResDtos);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createPurchase(TmPurchaseReqDto reqDto) {
        log.info("Service createPurchase start. reqDto=【{}】",reqDto);
        TmPurchaseEntity entity = this.purchaseHelper.editEntity(reqDto);
        TmOrderEntity tmOrderEntity = this.tmOrderMapper.queryByOrderNo(reqDto.getOrderNo());
        Assert.notNull(tmOrderEntity, "订单号对应的订单不存在!");
        entity.setPurchaseNo(P + reqDto.getOrderNo());
        entity.setCreateTime(new Date());
        entity.setPkId(UuidUtil.getUuid());
        //新增采购单
        int ret = this.tmPurchaseMapper.insertSelective(entity);
        log.info("Service createPurchase end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TmPurchaseReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TmPurchaseEntity checkEntity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TmPurchaseEntity entity = this.purchaseHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        //修改采购明细
        List<TmPurchaseDetailReqDto> purchaseDetailReqDtoList = reqDto.getPurchaseDetailReqDtoList();
        int i = SortConstant.ZERO;
        if(CollectionUtils.isNotEmpty(purchaseDetailReqDtoList)){
            //先删除所有的明细 再新增
            Assert.hasText(reqDto.getPurchaseNo(), "采购单号不能为空!");
            this.tmPurchaseDetailMapper.deleteByPurchaseNo(reqDto.getPurchaseNo());
            //采购总金额  等于每个明细总金额之和
            BigDecimal totalAmount = new BigDecimal("0");
            //订单状态 采购中
            TmOrderEntity tmOrderEntity = this.tmOrderMapper.queryByOrderNo(checkEntity.getOrderNo());
            //修改订单状态
            this.iOrderService.updateOrderStatus(tmOrderEntity.getPkId(), OrderStatusEnum.PURCHASING.getCode());
            //purchaseDetailReqDtoList.forEach(tmPurchaseDetailReqDto -> {
            for(TmPurchaseDetailReqDto tmPurchaseDetailReqDto :  purchaseDetailReqDtoList){
                tmPurchaseDetailReqDto.setPurchaseNo(reqDto.getPurchaseNo());
                tmPurchaseDetailReqDto.setPurchaseNumber(reqDto.getPurchaseNo() + "-" + (i + SortConstant.ONE));
                if(StringUtils.isNotBlank(tmPurchaseDetailReqDto.getUnitPrice())){
                    if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP, tmPurchaseDetailReqDto.getUnitPrice())) {
                        throw new BusiException("采购单价不符合规则, 整数位最多8位, 小数位最多2位!");
                    }
                }
                if(StringUtils.isNotBlank(tmPurchaseDetailReqDto.getQuantity())){
                    if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP_INT, tmPurchaseDetailReqDto.getQuantity())) {
                        throw new BusiException("数量不符合规则, 整数位最多10位, 不能有小数位!");
                    }
                }
                if(StringUtils.isNotBlank(tmPurchaseDetailReqDto.getUnitPrice()) && StringUtils.isNotBlank(tmPurchaseDetailReqDto.getQuantity())){
                    BigDecimal totalAmountDetail = new BigDecimal(tmPurchaseDetailReqDto.getUnitPrice()).multiply(new BigDecimal(tmPurchaseDetailReqDto.getQuantity())).setScale(SortConstant.TWO, BigDecimal.ROUND_HALF_UP);
                    tmPurchaseDetailReqDto.setTotalAmountDetail(totalAmountDetail);
                    totalAmount = totalAmount.add(totalAmountDetail);
                }
                this.purchaseDetailService.createPurchaseDetail(tmPurchaseDetailReqDto);
           }
            //采购状态 采购中
            entity.setPurchaseStatus(PurchaseStatusEnum.PURCHASE_ING.getCode());
            //采购总金额
            entity.setTotalAmount(totalAmount);
        }
        //修改采购表
        int ret = this.tmPurchaseMapper.updateByPrimaryKeySelective(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public PageInfo<TmPurchaseResDto> queryPurchaseByPage(Integer pageNum, Integer pageSize, String orderNo, String purchaseNo, String purchaseStatus, String purchasePersonnel, String orderStatus) {
        PageHelper.orderBy("CREATE_TIME DESC");
        Page<TmPurchaseResDto> startPage = PageHelper.startPage(pageNum, pageSize);
        String userId = LoginUtil.getUserId();
        Assert.hasText(userId, "用户ID为空!");
        TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(userId);
        Assert.notNull(userEntity, "userId对应的用户不存在!");
        TsRoleEntity tsRoleEntity = this.roleMapper.queryByUserId(userId);
        Assert.notNull(tsRoleEntity, "用户对应的角色为空!");
        String isAll = tsRoleEntity.getIsAll();
        List<TmPurchaseEntity> purchaseEntityList = this.tmPurchaseMapper.queryPurchaseList(orderNo, purchaseNo, purchaseStatus, purchasePersonnel, orderStatus, isAll, userId);
        List<TmPurchaseResDto> purchaseResDtoList = this.purchaseHelper.editResDtoList(purchaseEntityList);
        PageInfo<TmPurchaseResDto> pageInfo = new PageInfo<>(startPage);
        pageInfo.setList(purchaseResDtoList);
        return pageInfo;
    }

    @Override
    public int completeByPrimaryKey(String pkId, String userId) {
        TmPurchaseEntity checkEntity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        //采购单完成
        checkEntity.setPurchaseStatus(PurchaseStatusEnum.PURCHASE_COMPLETED.getCode());
        checkEntity.setPurchasePersonnel(userId);
        int i = this.tmPurchaseMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(i==SortConstant.ONE, "完成采购单,修改状态失败!");
        //修改订单状态
        TmOrderEntity tmOrderEntity = this.tmOrderMapper.queryByOrderNo(checkEntity.getOrderNo());
        Assert.notNull(tmOrderEntity, "采购单对应的订单不存在!");
        this.iOrderService.updateOrderStatus(tmOrderEntity.getPkId(), OrderStatusEnum.CUTTING.getCode());
        return i;
    }

}
