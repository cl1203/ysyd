/**
 * UserController.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.sys;

import com.cl.ysyd.aop.LoggerManage;
import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.dto.order.req.BindingUserAndRoleReqDto;
import com.cl.ysyd.dto.sys.req.TsUserReqDto;
import com.cl.ysyd.dto.sys.res.TsUserResDto;
import com.cl.ysyd.service.sys.IUserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制层
 * @author chenlong  2020-11-24
 */
@RestController
@Slf4j
@Api(tags="用户接口")
@RequestMapping(value = "/v1/user")
@CrossOrigin
public class UserController {
    /**
     * 用户service
     */
    @Autowired
    private IUserService iUserService;

    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/{pkId}")
    @LoggerManage(description = "删除用户")
    public ResponseData<Integer> deleteByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller deleteByPrimaryKey start.");
        int result = this.iUserService.deleteByPrimaryKey(pkId);
        log.info("Controller deleteByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return 响应结果:ResponseData<TsUserResDto>
     */
    @ApiOperation(value = "根据主键查询用户")
    @GetMapping(value = "/{pkId}")
    @LoggerManage(description = "根据主键查询用户")
    public ResponseData<TsUserResDto> queryByPrimaryKey(@PathVariable String pkId) {
        log.info("Controller queryByPrimaryKey start.");
        TsUserResDto resDto =  this.iUserService.queryByPrimaryKey(pkId);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "新增用户/注册")
    @PostMapping(value = "")
    @LoggerManage(description = "新增用户")
    public ResponseData<Integer> createUser(@RequestBody @Valid TsUserReqDto reqDto) {
        log.info("Controller queryByPrimaryKey start.");
        int result = this.iUserService.createUser(reqDto);
        log.info("Controller queryByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return 响应结果:ResponseData<Integer>
     */
    @ApiOperation(value = "修改用户/启用/禁用")
    @PutMapping(value = "/{pkId}")
    @LoggerManage(description = "修改用户/启用/禁用")
    public ResponseData<Integer> updateByPrimaryKey(@PathVariable String pkId, @RequestBody @Valid TsUserReqDto reqDto) {
        log.info("Controller updateByPrimaryKey start.");
        int result = this.iUserService.updateByPrimaryKey(pkId, reqDto);
        log.info("Controller updateByPrimaryKey end.");
        return new ResponseData<>(result);
    }

    @ApiOperation(value = "查询用户列表")
    @GetMapping(path = "/{pageNum}/{pageSize}/{auditStatus}")
    @LoggerManage(description = "查询用户列表")
    public ResponseData<PageInfo<TsUserResDto>> queryUserByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @PathVariable String auditStatus,String userName, String realName, String type, String status) {
        log.info("Controller queryUserByPage start.");
        PageInfo<TsUserResDto> retDto = this.iUserService.queryUserByPage(pageNum, pageSize, auditStatus, userName, realName, type, status);
        log.info("Controller queryUserByPage end.");
        return new ResponseData<>(retDto);
    }

    /**
     * 重置密码
     *
     */
    @ApiOperation(value = "重置密码")
    @PutMapping(path = "/{pkId}/password/reset")
    @LoggerManage(description = "重置密码")
    public ResponseData<String> resetPassword(@PathVariable String pkId) {
        log.info("Controller resetPassword start.");
        if (StringUtils.isEmpty(pkId)) {
            throw new BusiException("{Common.Parameter.PkId.NotEmpty}");
        }
        this.iUserService.resetPassword(pkId);
        log.info("Controller resetPassword end. ");
        return new ResponseData<>("密码重置成功!");
    }

    /**
     * 修改密码
     *
     * @param reqDto 密码更新dto
     *
     */
    @ApiOperation(value = "修改密码")
    @PutMapping(path = "/password")
    @LoggerManage(description = "修改密码")
    public ResponseData<String> updatePassword(@RequestBody TsUserReqDto reqDto) {
        log.info("Controller updatePassword start.");
        this.iUserService.updatePassword(reqDto);
        log.info("Controller updatePassword end. ");
        return new ResponseData<>("密码修改成功!");
    }

    /**
     * 用户绑定角色
     * @param reqDto 请求对象
     * @return 返回结果
     */
    @ApiOperation(value = "用户绑定角色")
    @PutMapping(path = "/binding")
    @LoggerManage(description = "用户绑定角色")
    public ResponseData<String> bindingRole( @RequestBody @Valid BindingUserAndRoleReqDto reqDto){
        log.info("Controller bindingRole start.");
        this.iUserService.bindingRole(reqDto);
        log.info("Controller bindingRole end. ");
        return new ResponseData<>("分配角色成功!");
    }
}