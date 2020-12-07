/**
 * UserOrderController.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.sys;

import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.sys.req.TrUserOrderReqDto;
import com.cl.ysyd.dto.sys.res.TrUserOrderResDto;
import com.cl.ysyd.service.sys.IUserOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户订单关系控制层
 * @author chenlong  2020-11-24
 */
@RestController
@Slf4j
@Api(tags="用户订单关系接口")
@RequestMapping(value = "/v1/userOrder")
@CrossOrigin
public class UserOrderController {
    /**
     * 用户订单关系service
     */
    @Autowired
    private IUserOrderService iUserOrderService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除用户订单关系")
    @DeleteMapping(value = "/{pkId}")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iUserOrderService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TrUserOrderResDto>
     */
    @ApiOperation(value = "根据主键查询用户订单关系")
    @GetMapping(value = "/{pkId}")
    public ResponseData<TrUserOrderResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TrUserOrderResDto resDto =  this.iUserOrderService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增用户订单关系")
    @PostMapping(value = "")
    public ResponseData<Integer> createUserOrder(@RequestBody @Valid TrUserOrderReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iUserOrderService.createUserOrder(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改用户订单关系")
    @PutMapping(value = "/{pkId}")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TrUserOrderReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iUserOrderService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }
}