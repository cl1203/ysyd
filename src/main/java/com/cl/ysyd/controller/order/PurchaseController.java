/**
 * PurchaseController.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.order;

import com.cl.ysyd.aop.LoggerManage;
import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.order.req.TmPurchaseReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseBillResDto;
import com.cl.ysyd.dto.order.res.TmPurchaseResDto;
import com.cl.ysyd.service.order.IPurchaseService;
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
 * 采购单控制层
 * @author chenlong  2020-11-24
 */
@RestController
@Slf4j
@Api(tags="采购单接口")
@RequestMapping(value = "/v1/purchase")
public class PurchaseController {
    /**
     * 采购单service
     */
    @Autowired
    private IPurchaseService iPurchaseService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除采购单")
    @DeleteMapping(value = "/{pkId}")
    @LoggerManage(description = "删除采购单")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iPurchaseService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TmPurchaseResDto>
     */
    @ApiOperation(value = "根据主键查询采购单")
    @GetMapping(value = "/{pkId}")
    @LoggerManage(description = "根据主键查询采购单")
    public ResponseData<TmPurchaseResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TmPurchaseResDto resDto =  this.iPurchaseService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增采购单")
    @PostMapping(value = "")
    @LoggerManage(description = "新增采购单")
    public ResponseData<Integer> createPurchase(@RequestBody @Valid TmPurchaseReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iPurchaseService.createPurchase(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改采购单")
    @PutMapping(value = "/{pkId}")
    @LoggerManage(description = "修改采购单")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TmPurchaseReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iPurchaseService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param orderNo 订单号
     * @param purchaseNo 采购单号
     * @param purchaseStatus 采购状态
     * @param purchasePersonnel 采购员
     * @param orderStatus 订单状态
     * @return 采购列表结果集
     */
    @ApiOperation(value = "查询采购单列表")
    @GetMapping(path = "/query/{pageNum}/{pageSize}")
    @LoggerManage(description = "查询采购单列表")
    public ResponseData<PageInfo<TmPurchaseResDto>> queryPurchaseByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, String orderNo, String purchaseNo,
                                                                  String purchaseStatus, String purchasePersonnel, String orderStatus){
        log.info("Controller queryOrderByPage start.");
        PageInfo<TmPurchaseResDto> resDto = this.iPurchaseService.queryPurchaseByPage(pageNum, pageSize, orderNo, purchaseNo, purchaseStatus, purchasePersonnel, orderStatus);
        log.info("Controller queryOrderByPage end.");
        return new ResponseData<>(resDto);
    }

    /**
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param purchasePersonnel 采购员ID
     * @param supplier 供应商
     * @param purchaseDate 采购日期
     * @return 结果集
     */
    @ApiOperation(value = "查询采购单对账单列表")
    @GetMapping(path = "/bill/{pageNum}/{pageSize}")
    @LoggerManage(description = "查询采购单对账单列表")
    public ResponseData<PageInfo<TmPurchaseBillResDto>> queryPurchaseBillByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, String purchasePersonnel, String supplier, String purchaseDate){
        log.info("Controller queryPurchaseBillByPage start.");
        PageInfo<TmPurchaseBillResDto> resDto = this.iPurchaseService.queryPurchaseBillByPage(pageNum, pageSize, purchasePersonnel, supplier, purchaseDate);
        log.info("Controller queryPurchaseBillByPage end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 完成采购单
     * @param pkId 采购单ID
     * @param userId 用户ID
     * @return 修改结果
     */
    @ApiOperation(value = "公众号完成采购单")
    @PutMapping(value = "/app/complete/{pkId}/{userId}")
    @LoggerManage(description = "公众号完成采购单")
    public ResponseData<Integer> completeByPrimaryKeyApp(@PathVariable String pkId, @PathVariable String userId) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iPurchaseService.completeByPrimaryKeyApp(pkId, userId);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 完成采购单
     * @param pkId 采购单ID
     * @return 修改结果
     */
    @ApiOperation(value = "web端完成采购单")
    @PutMapping(value = "/complete/{pkId}/{userId}")
    @LoggerManage(description = "web端完成采购单")
    public ResponseData<Integer> completeByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iPurchaseService.completeByPrimaryKey(pkId);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     *
     * @param response 响应
     * @param orderNo  订单号
     * @param purchaseNo 采购单号
     * @param purchaseStatus 采购状态
     * @param purchasePersonnel 采购人
     * @param orderStatus 订单状态
     * @throws IOException io异常
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出采购列表以及采购明细数据")
    @LoggerManage(description = "导出采购列表以及采购明细数据")
    public void exportPurchase(HttpServletResponse response, String orderNo, String purchaseNo,
                               String purchaseStatus, String purchasePersonnel, String orderStatus) throws IOException{
        log.info("Controller exportPurchase start.");
        this.iPurchaseService.export(response,orderNo, purchaseNo, purchaseStatus, purchasePersonnel, orderStatus);
        log.info("Controller exportPurchase end.");
    }

    /**
     *
     * @param response 响应
     * @param purchasePersonnel 采购员
     * @param supplier 供应商
     * @param purchaseDate 采购日期
     * @throws IOException io异常
     */
    @GetMapping("/bill/export")
    @ApiOperation(value = "导出采购对账单")
    @LoggerManage(description = "导出采购对账单")
    public void exportPurchaseBill(HttpServletResponse response, String purchasePersonnel, String supplier, String purchaseDate) throws IOException{
        log.info("Controller exportPurchaseBill start.");
        this.iPurchaseService.exportPurchaseBill(response,purchasePersonnel, supplier, purchaseDate);
        log.info("Controller exportPurchaseBill end.");
    }
}
