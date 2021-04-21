/**
 * OrderImgController.java
 * Created at 2021-04-21
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.order;

import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.order.req.TmOrderImgReqDto;
import com.cl.ysyd.dto.order.res.TmOrderImgResDto;
import com.cl.ysyd.service.order.IOrderImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenlong  2021-04-21
 */
@RestController
@Slf4j
@Api(tags="接口")
@RequestMapping(value = "orderImg")
public class OrderImgController {
    /**
     * service
     */
    @Autowired
    private IOrderImgService iOrderImgService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/{pkId}")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iOrderImgService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TmOrderImgResDto>
     */
    @ApiOperation(value = "根据主键查询")
    @GetMapping(value = "/{pkId}")
    public ResponseData<TmOrderImgResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TmOrderImgResDto resDto =  this.iOrderImgService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增")
    @PostMapping(value = "")
    public ResponseData<Integer> createOrderImg(@RequestBody @Valid TmOrderImgReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iOrderImgService.createOrderImg(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改")
    @PutMapping(value = "/{pkId}")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TmOrderImgReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iOrderImgService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }
}