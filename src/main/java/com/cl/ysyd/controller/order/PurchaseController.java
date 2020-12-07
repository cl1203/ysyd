/**
 * PurchaseController.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.order;

import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.order.req.TmPurchaseReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseResDto;
import com.cl.ysyd.service.order.IPurchaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TmPurchaseReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iPurchaseService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }
}