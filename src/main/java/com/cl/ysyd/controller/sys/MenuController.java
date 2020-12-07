/**
 * MenuController.java
 * Created at 2020-11-26
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.sys;

import com.cl.ysyd.aop.LoggerManage;
import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.sys.req.TsMenuReqDto;
import com.cl.ysyd.dto.sys.res.TsMenuResDto;
import com.cl.ysyd.service.sys.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单控制层
 * @author chenlong  2020-11-26
 */
@RestController
@Slf4j
@Api(tags="菜单接口")
@RequestMapping(value = "/v1/menu")
@CrossOrigin
public class MenuController {
    /**
     * 菜单service
     */
    @Autowired
    private IMenuService iMenuService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping(value = "/{pkId}")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iMenuService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TsMenuResDto>
     */
    @ApiOperation(value = "根据主键查询菜单")
    @GetMapping(value = "/{pkId}")
    public ResponseData<TsMenuResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TsMenuResDto resDto =  this.iMenuService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增菜单")
    @PostMapping(value = "")
    public ResponseData<Integer> createMenu(@RequestBody @Valid TsMenuReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iMenuService.createMenu(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改菜单")
    @PutMapping(value = "/{pkId}")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TsMenuReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iMenuService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 获取所有菜单和按钮
     * @return 响应结果
     */
    @ApiOperation(value = "获取所有菜单和按钮")
    @GetMapping(value = "/query")
    @LoggerManage(description = "")
    public ResponseData<List<TsMenuResDto>> queryMenuAndButton(){
        log.info("Controller queryMenuAndButton start.");
        List<TsMenuResDto> resDtoList = this.iMenuService.queryMenuAndButton();
        log.info("Controller queryMenuAndButton end.");
        return new ResponseData<>(resDtoList);
    }
}