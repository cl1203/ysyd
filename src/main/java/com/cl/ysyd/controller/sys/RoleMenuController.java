/**
 * RoleMenuController.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.sys;

import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.sys.req.TrRoleMenuReqDto;
import com.cl.ysyd.dto.sys.res.TrRoleMenuResDto;
import com.cl.ysyd.service.sys.IRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 角色菜单关系表控制层
 * @author chenlong  2020-11-24
 */
@RestController
@Slf4j
@Api(tags="角色菜单关系表接口")
@RequestMapping(value = "/v1/roleMenu")
@CrossOrigin
public class RoleMenuController {
    /**
     * 角色菜单关系表service
     */
    @Autowired
    private IRoleMenuService iRoleMenuService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除角色菜单关系表")
    @DeleteMapping(value = "/{pkId}")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iRoleMenuService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TrRoleMenuResDto>
     */
    @ApiOperation(value = "根据主键查询角色菜单关系表")
    @GetMapping(value = "/{pkId}")
    public ResponseData<TrRoleMenuResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TrRoleMenuResDto resDto =  this.iRoleMenuService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增角色菜单关系表")
    @PostMapping(value = "")
    public ResponseData<Integer> createRoleMenu(@RequestBody @Valid TrRoleMenuReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iRoleMenuService.createRoleMenu(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改角色菜单关系表")
    @PutMapping(value = "/{pkId}")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TrRoleMenuReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iRoleMenuService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }
}