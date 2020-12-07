/**
 * PurchaseDetailController.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.order;

import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.order.req.TmPurchaseDetailReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseDetailResDto;
import com.cl.ysyd.service.order.IPurchaseDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 采购单明细控制层
 * @author chenlong  2020-11-24
 */
@RestController
@Slf4j
@Api(tags="采购单明细接口")
@RequestMapping(value = "/v1/purchaseDetail")
public class PurchaseDetailController {
    /**
     * 采购单明细service
     */
    @Autowired
    private IPurchaseDetailService iPurchaseDetailService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除采购单明细")
    @DeleteMapping(value = "/{pkId}")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iPurchaseDetailService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TmPurchaseDetailResDto>
     */
    @ApiOperation(value = "根据主键查询采购单明细")
    @GetMapping(value = "/{pkId}")
    public ResponseData<TmPurchaseDetailResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TmPurchaseDetailResDto resDto =  this.iPurchaseDetailService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增采购单明细")
    @PostMapping(value = "")
    public ResponseData<Integer> createPurchaseDetail(@RequestBody @Valid TmPurchaseDetailReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iPurchaseDetailService.createPurchaseDetail(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改采购单明细")
    @PutMapping(value = "/{pkId}")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TmPurchaseDetailReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iPurchaseDetailService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }
}