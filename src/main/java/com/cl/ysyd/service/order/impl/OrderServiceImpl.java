/**
 * OrderServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.impl;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.AuditStatusEnum;
import com.cl.ysyd.common.enums.DictType;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.ExcelUtils;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.order.req.TmOrderReqDto;
import com.cl.ysyd.dto.order.res.TmOrderResDto;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.mapper.order.TmOrderMapper;
import com.cl.ysyd.mapper.sys.TsUserMapper;
import com.cl.ysyd.service.order.IOrderService;
import com.cl.ysyd.service.order.helper.OrderHelper;
import com.cl.ysyd.service.sys.IBizDictionaryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 订单service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements IOrderService {
    /**
     * 订单Mapper
     */
    @Autowired
    private TmOrderMapper tmOrderMapper;

    @Autowired
    private TsUserMapper userMapper;

    @Autowired
    private IBizDictionaryService iTcDictService;

    /**
     * 订单Helper
     */
    @Autowired
    private OrderHelper orderHelper;

    private final static String SERIAL = "00001";
    private final static int LENGTH = 5;
    private final static int MAXNUMBER = 5;


    @Override
    public void cancelByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        //根据主键查询  校验数据是否存在
        TmOrderEntity checkEntity = this.getTmOrderEntity(pkId);
        checkEntity.setStatus(SortConstant.ZERO.byteValue());
        checkEntity.setLastUpdateTime(new Date());
        checkEntity.setLastUpdateUser(LoginUtil.getUserId());
        int ret = this.tmOrderMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(ret==SortConstant.ONE, "作废失败!");
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
    }

    /**
     * 根据主键查询  校验数据是否存在
     * @param pkId 订单ID
     * @return 订单对象
     */
    private TmOrderEntity getTmOrderEntity(String pkId) {
        TmOrderEntity checkEntity = this.tmOrderMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("根据ID查询订单数据不存在!");
        }
        return checkEntity;
    }

    @Override
    public TmOrderResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TmOrderEntity entity = this.tmOrderMapper.selectByPrimaryKey(pkId);
        TmOrderResDto resDto = this.orderHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public void createOrder(TmOrderReqDto reqDto) {
        log.info("Service createOrder start. reqDto=【{}】",reqDto);
        TmOrderEntity entity = this.orderHelper.editEntity(reqDto);
        //当前年月日
        String dateString = DateUtil.getDateString(new Date(), DateUtil.DATETIMESHOWFORMAT4);
        //获取当前流水号
        String date = DateUtil.getDateString(new Date(), DateUtil.DATESHOWFORMAT);
        String orderNoMax = this.tmOrderMapper.querySerialNumber(date);
        //订单号
        String orderNo;
        if(StringUtils.isNotBlank(orderNoMax)){
            int length = orderNoMax.length();
            String serialNumber = orderNoMax.substring(length - LENGTH, length);
            orderNo = String.valueOf(Long.parseLong(dateString + serialNumber) + SortConstant.ONE);
        }else{
            orderNo = dateString + SERIAL;
        }
        entity.setOrderNo(orderNo);
        entity.setCreateTime(new Date());
        entity.setPkId(UuidUtil.getUuid());
        entity.setEstablishDate(DateUtil.dateToDate(new Date(), DateUtil.DATESHOWFORMAT));
        int ret = this.tmOrderMapper.insertSelective(entity);
        Assert.isTrue(ret==SortConstant.ONE, "新增失败!");
        log.info("Service createOrder end. ret=【{}】",ret);
    }

    @Override
    public void updateByPrimaryKey(String pkId, TmOrderReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        //校验主键ID是否存在数据
        this.getTmOrderEntity(pkId);
        TmOrderEntity entity = this.orderHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        entity.setLastUpdateTime(new Date());
        entity.setLastUpdateUser(LoginUtil.getUserId());
        int ret = this.tmOrderMapper.updateByPrimaryKeySelective(entity);
        Assert.isTrue(ret==SortConstant.ONE, "修改失败!");
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
    }

    @Override
    public PageInfo<TmOrderResDto> queryOrderByPage(Integer pageNum, Integer pageSize, String orderUser, String orderStatus,
                                                    String deliveryDate, String establishDate, String completeDate, String examineStatus) {
        PageHelper.orderBy("CREATE_TIME DESC");
        Page<TmOrderResDto> startPage = PageHelper.startPage(pageNum, pageSize);
        String userId = LoginUtil.getUserId();
        TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(userId);
        /*if(null == userEntity){
            throw new BusiException("userId对应的用户不存在!");
        }*/
        List<TmOrderEntity> orderEntityList = this.tmOrderMapper.queryOrderList(orderUser, orderStatus, deliveryDate, establishDate, completeDate, examineStatus, null);
        List<TmOrderResDto> orderResDtoList = this.orderHelper.editResDtoList(orderEntityList);
        PageInfo<TmOrderResDto> pageInfo = new PageInfo<>(startPage);
        pageInfo.setList(orderResDtoList);
        return pageInfo;
    }

    @Override
    public void distributionUser(String orderId, String orderUserId) {
        //校验订单ID对应的订单是否存在
        log.info("Service updateByPrimaryKey start. orderId=【{}】, orderUserId =【{}】",orderId,orderUserId);
        //根据主键ID检验订单数据
        TmOrderEntity checkEntity = this.getTmOrderEntity(orderId);
        String orderUser = checkEntity.getOrderUser();
        if(StringUtils.isNotBlank(orderUser)){
            throw new BusiException("订单已经分配用户!");
        }
        TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(orderUserId);
        if(null == userEntity){
            throw new BusiException("选择的用户不存在!");
        }
        Byte status = userEntity.getStatus();
        if(status.equals(SortConstant.ZERO.byteValue())){
            throw new BusiException("选择的用户已经被禁用!");
        }
        String auditStatus = userEntity.getAuditStatus();
        if(auditStatus.equals(AuditStatusEnum.NOT_REVIEWED)){
            throw new BusiException("选择的用户还未审核!");
        }
        checkEntity.setOrderUser(orderUserId);
        checkEntity.setLastUpdateUser(LoginUtil.getUserId());
        checkEntity.setLastUpdateTime(new Date());
        int ret = this.tmOrderMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(ret==SortConstant.ONE, "分配失败!");
    }

    @Override
    public void updateOrderStatus(String orderId, String orderStatus) {
        //根据主键ID检验订单数据
        TmOrderEntity checkEntity = this.getTmOrderEntity(orderId);
        String orderStatusText = this.iTcDictService.getTextByBizCode(DictType.ORDER_STATUS.getCode(), orderStatus);
        Assert.hasText(orderStatusText, "选择的订单状态不存在!");
        checkEntity.setOrderStatus(orderStatus);
        checkEntity.setLastUpdateTime(new Date());
        checkEntity.setLastUpdateUser(LoginUtil.getUserId());
        int ret = this.tmOrderMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(ret==SortConstant.ONE, "修改订单状态失败!");
    }

    @Override
    public void export(HttpServletResponse response, String orderUser, String orderStatus, String deliveryDate, String establishDate, String completeDate) {
        //查询结果
        List<TmOrderResDto> list = this.queryOrderByPage(SortConstant.ONE, SortConstant.PAGE_SIZE, orderUser, orderStatus, deliveryDate, establishDate, completeDate, null).getList();
        //表头
        String[] headers = {"订单号(orderNo)" , "交货日期(deliveryDate)" , "订单单价(unitPrice)" , "所属用户(orderUserName)" ,"用户类型(userType)",
                "创建日期(establishDate)" , "完成日期(completeDate)", "订单状态(orderStatusText)" , "SKU(sku)",
                "下单人(orderPeople)" , "尺码(orderSize)", "类型(orderTypeText)" , "状态(statusText)",};
        /*String title = "";
        title= URLDecoder.decode(title, UTF_8);*/
        ExcelUtils.exportExcel("订单列表信息" , headers , list , response , DateUtil.DATESHOWFORMAT);
    }

    @Override
    public int queryNumByUserId() {
        String userId = LoginUtil.getUserId();
        TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(userId);
        if(null == userEntity){
            throw new BusiException("userId对应的用户数据不存在!");
        }
        return this.tmOrderMapper.queryNumByUserId(userId);
    }

    @Override
    public void connectOrder(String orderType, Integer number) {
        //所有类型的订单 待接单状态的数量是否为0
        List<TmOrderEntity> waitingList = this.tmOrderMapper.queryWaiting(number).getList();
        Assert.isTrue(waitingList.size() > SortConstant.ZERO, "未存在任何待接单状态的订单!");
        //此用户有多少未完成订单
        int i = this.queryNumByUserId();
        Assert.isTrue(i <= MAXNUMBER, "您名下未完成订单大于或等于5单, 不能再接单!");
        Assert.isTrue((i + number) <= MAXNUMBER, "接单数量加您未完成的订单已经大于或等于5单!");
        //查询所选择类型的订单还有多少待接单 按接单的数量分页
        List<TmOrderEntity> list = this.tmOrderMapper.queryWaitingByOrderType(orderType, number).getList();
        //如果 此类型的订单在待接单状态的数量和用户接单数量一样 直接分配
        if(list.size() == number){
            list.forEach(this::updateOrderUser);
        }else{
            //如果此类型待接单的订单数量为0 查询其他类型所有待接单的订单
            int size = list.size();
            if(size == SortConstant.ZERO){
                //直接分配其他类型的订单
                waitingList.forEach(this::updateOrderUser);
            }else{
                //当此类型订单有 但不够的时候 补其他订单
                waitingList = this.tmOrderMapper.queryWaiting(number -size).getList();
                waitingList.forEach(this::updateOrderUser);
            }
        }
    }

    @Override
    public void returnOrder(String pkId) {
        //根据主键ID检验订单数据
        TmOrderEntity checkEntity = this.getTmOrderEntity(pkId);
        checkEntity.setOrderUser("");
        checkEntity.setLastUpdateTime(new Date());
        checkEntity.setLastUpdateUser(LoginUtil.getUserId());
        int i = this.tmOrderMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(i == SortConstant.ONE, "退单失败!");
    }

    /**
     * 分配订单
     * @param tmOrderEntity 订单对象
     */
    private void updateOrderUser(TmOrderEntity tmOrderEntity) {
        tmOrderEntity.setOrderUser(LoginUtil.getUserId());
        tmOrderEntity.setLastUpdateTime(new Date());
        tmOrderEntity.setLastUpdateUser(LoginUtil.getUserId());
        int j = this.tmOrderMapper.updateByPrimaryKeySelective(tmOrderEntity);
        Assert.isTrue(j == SortConstant.ONE, "分配订单失败!");
    }
}
