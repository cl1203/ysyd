/**
 * UserRoleController.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.sys;

import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.sys.req.TrUserRoleReqDto;
import com.cl.ysyd.dto.sys.res.TrUserRoleResDto;
import com.cl.ysyd.service.sys.IUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户角色关系控制层
 * @author chenlong  2020-11-24
 */
@RestController
@Slf4j
@Api(tags="用户角色关系接口")
@RequestMapping(value = "/v1/userRole")
@CrossOrigin
public class UserRoleController {
    /**
     * 用户角色关系service
     */
    @Autowired
    private IUserRoleService iUserRoleService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除用户角色关系")
    @DeleteMapping(value = "/{pkId}")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iUserRoleService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TrUserRoleResDto>
     */
    @ApiOperation(value = "根据主键查询用户角色关系")
    @GetMapping(value = "/{pkId}")
    public ResponseData<TrUserRoleResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TrUserRoleResDto resDto =  this.iUserRoleService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增用户角色关系")
    @PostMapping(value = "")
    public ResponseData<Integer> createUserRole(@RequestBody @Valid TrUserRoleReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iUserRoleService.createUserRole(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改用户角色关系")
    @PutMapping(value = "/{pkId}")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TrUserRoleReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iUserRoleService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }
}