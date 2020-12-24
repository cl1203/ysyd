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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 订单控制层
 * @author chenlong  2020-12-07
 */
@RestController
@Slf4j
@Api(tags="订单接口")
@RequestMapping(value = "/v1/order")
@CrossOrigin
public class OrderController {
    /**
     * 订单service
     */
    @Autowired
    private IOrderService iOrderService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<String>
     */
    @ApiOperation(value = "作废订单")
    @PutMapping(value = "/cancel/{pkId}")
    @LoggerManage(description = "作废订单")
    public ResponseData<String> cancelByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        this.iOrderService.cancelByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>("作废成功!");
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TmOrderResDto>
     */
    @ApiOperation(value = "根据主键查询订单")
    @GetMapping(value = "/{pkId}")
    @LoggerManage(description = "根据主键查询订单")
    public ResponseData<TmOrderResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TmOrderResDto resDto =  this.iOrderService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<String>
     */
    @ApiOperation(value = "新增订单")
    @PostMapping(value = "")
    @LoggerManage(description = "新增订单")
    public ResponseData<String> createOrder(@RequestBody @Valid TmOrderReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        this.iOrderService.createOrder(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>("新增成功!");
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<String>
     */
    @ApiOperation(value = "修改订单")
    @PutMapping(value = "/update/{pkId}")
    @LoggerManage(description = "修改订单")
    public ResponseData<String> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TmOrderReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        this.iOrderService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>("修改成功!");
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
    @ApiOperation(value = "查询订单列表")
    @GetMapping(path = "/query/{pageNum}/{pageSize}")
    @LoggerManage(description = "查询订单列表")
    public ResponseData<PageInfo<TmOrderResDto>> queryOrderByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, String orderUser, String orderStatus,
                                                                  String deliveryDate, String establishDate, String completeDate, String examineStatus){
        log.info("Controller queryOrderByPage start.");
        PageInfo<TmOrderResDto> resDto = this.iOrderService.queryOrderByPage(pageNum, pageSize, orderUser, orderStatus, deliveryDate, establishDate, completeDate, examineStatus);
        log.info("Controller queryOrderByPage end.");
        return new ResponseData<>(resDto);
    }

    /**
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param orderUser 所属用户
     * @param deliveryDate 交货日期
     * @param establishDate 创建日期
     * @param completeDate 完成日期
     * @return 结果集
     */
    @ApiOperation(value = "查询订单对账单列表")
    @GetMapping(path = "/bill/{pageNum}/{pageSize}")
    @LoggerManage(description = "查询订单对账单列表")
    public ResponseData<PageInfo<TmOrderResDto>> queryOrderBillByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, String orderUser,
                                                                  String deliveryDate, String establishDate, String completeDate){
        log.info("Controller queryOrderBillByPage start.");
        PageInfo<TmOrderResDto> resDto = this.iOrderService.queryOrderBillByPage(pageNum, pageSize, orderUser, deliveryDate, establishDate, completeDate);
        log.info("Controller queryOrderBillByPage end.");
        return new ResponseData<>(resDto);
    }


    /**
     * 分配订单
     * @param orderId 订单ID
     * @param orderUserId 用户Id
     * @return 结果
     */
    @ApiOperation(value = "分配订单")
    @PutMapping(path = "/distribution/{orderId}/{orderUserId}")
    @LoggerManage(description = "分配订单")
    public ResponseData<String> distributionUser(@PathVariable String orderId, @PathVariable String orderUserId){
        log.info("Controller distributionUser start.");
        this.iOrderService.distributionUser(orderId, orderUserId);
        log.info("Controller distributionUser end.");
        return new ResponseData<>("分配成功!");
    }

    /**
     * 操作订单状态
     * @param pkId 订单ID
     * @return 结果
     */
    @ApiOperation(value = "操作订单状态")
    @PutMapping(path = "/updateStatus/{pkId}")
    @LoggerManage(description = "操作订单状态")
    public ResponseData<String> updateOrderStatus(@PathVariable String pkId){
        log.info("Controller updateOrderStatus start.");
        this.iOrderService.updateOrderStatus(pkId);
        log.info("Controller updateOrderStatus end.");
        return new ResponseData<>("修改订单状态成功!");
    }

    /**
     * 导出
     * @param response 响应
     * @param orderUser 订单所属用户
     * @param orderStatus 订单状态
     * @param deliveryDate 交货日期
     * @param establishDate 订单创建日期
     * @param completeDate 完成日期
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出订单列表数据")
    @LoggerManage(description = "导出订单列表数据")
    public void exportOrder(HttpServletResponse response , String orderUser, String orderStatus,
                              String deliveryDate, String establishDate, String completeDate)throws IOException {
        log.info("Controller exportOrder start.");
        this.iOrderService.export(response ,orderUser , orderStatus, deliveryDate, establishDate, completeDate);
        log.info("Controller exportOrder end.");
    }

    /**
     *
     * @param response 响应
     * @param orderUser 所属用户
     * @param deliveryDate 交货日期
     * @param establishDate 创建日期
     * @param completeDate 完成日期
     * @throws IOException IO异常
     */
    @GetMapping("/export/bill")
    @ApiOperation(value = "导出订单对账单列表数据")
    @LoggerManage(description = "导出订单对账单列表数据")
    public void exportOrderBill(HttpServletResponse response , String orderUser, String deliveryDate, String establishDate, String completeDate)throws IOException {
        log.info("Controller exportOrder start.");
        this.iOrderService.exportBill(response ,orderUser, deliveryDate, establishDate, completeDate);
        log.info("Controller exportOrder end.");
    }

    /**
     * 接单之前的接口, 查询此用户还有多少未完成的订单
     * @return 未完成的订单数量
     */
    @GetMapping("/queryNum")
    @ApiOperation(value = "查询此用户还有多少未完成的订单")
    @LoggerManage(description = "查询此用户还有多少未完成的订单")
    public ResponseData<Integer> queryNumByUserId(){
        log.info("Controller queryNumByUserId start.");
        int num = this.iOrderService.queryNumByUserId();
        log.info("Controller queryNumByUserId end.");
        return new ResponseData<>(num);
    }

    /**
     * 接单
     * @param orderType 订单类型
     * @param number 数量
     */
    @PutMapping("/connect/{orderType}/{number}")
    @ApiOperation(value = "接单")
    @LoggerManage(description = "接单")
    public ResponseData<String> connectOrder(@PathVariable String orderType, @PathVariable Integer number){
        log.info("Controller connectOrder start.");
        this.iOrderService.connectOrder(orderType, number);
        log.info("Controller connectOrder end.");
        return new ResponseData<>("接单成功!");
    }

    /**
     * 退回订单
     * @param pkId 主键
     * @return 响应结果:ResponseData<String>
     */
    @ApiOperation(value = "退回订单")
    @PutMapping(value = "/returnOrder/{pkId}")
    @LoggerManage(description = "退回订单")
    public ResponseData<String> returnOrder(@PathVariable String pkId) {
        log.info("Controller returnOrder start.");
        this.iOrderService.returnOrder(pkId);
        log.info("Controller returnOrder end.");
        return new ResponseData<>("退回订单成功!");
    }

    /**
     *  审核订单状态
     * @param pkId 订单ID
     * @param examineStatus 审核状态
     * @return 结果
     */
    @ApiOperation(value = "审核订单状态")
    @PutMapping(path = "/examineOrder/{pkId}/{examineStatus}")
    @LoggerManage(description = "审核订单状态")
    public ResponseData<String> examineOrder(@PathVariable String pkId, @PathVariable String examineStatus){
        log.info("Controller updateOrderStatus start.");
        this.iOrderService.examineOrder(pkId, examineStatus);
        log.info("Controller updateOrderStatus end.");
        return new ResponseData<>("审核订单状态成功!");
    }

}
