/**
 * OrderController.java
 * Created at 2020-12-07
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.order;

import com.cl.ysyd.aop.LoggerManage;
import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.order.req.TmOrderReqDto;
import com.cl.ysyd.dto.order.res.TmOrderResDto;
import com.cl.ysyd.service.order.IOrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 订单控制层
 * @author chenlong  2020-12-07
 */
@RestController
@Slf4j
@Api(tags="订单接口")
@RequestMapping(value = "order")
public class OrderController {
    /**
     * 订单service
     */
    @Autowired
    private IOrderService iOrderService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除订单")
    @DeleteMapping(value = "/{pkId}")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iOrderService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TmOrderResDto>
     */
    @ApiOperation(value = "根据主键查询订单")
    @GetMapping(value = "/{pkId}")
    public ResponseData<TmOrderResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TmOrderResDto resDto =  this.iOrderService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增订单")
    @PostMapping(value = "")
    public ResponseData<Integer> createOrder(@RequestBody @Valid TmOrderReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iOrderService.createOrder(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改订单")
    @PutMapping(value = "/{pkId}")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TmOrderReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iOrderService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }

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
    @ApiOperation(value = "查询用户列表")
    @GetMapping(path = "/{pageNum}/{pageSize}")
    @LoggerManage(description = "查询用户列表")
    public ResponseData<PageInfo<TmOrderResDto>> queryOrderByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, String orderUser,
                                                                  String orderStatus, String deliveryDate, String establishDate, String completeDate){
        log.info("Controller queryOrderByPage start.");
        PageInfo<TmOrderResDto> resDto = this.iOrderService.queryOrderByPage(pageNum, pageSize, orderUser, orderStatus, deliveryDate, establishDate, completeDate);
        log.info("Controller queryOrderByPage end.");
        return new ResponseData<>(resDto);
    }
}