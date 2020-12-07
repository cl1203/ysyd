/**
 * TsUserResDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "用户响应Dto")
public class TsUserResDto {
    /**
     * 主键
     */
    @ApiModelProperty(value="主键" )
    private String pkId;

    /**
     * 用户类型code
     */
    @ApiModelProperty(value="用户类型code" )
    private String type;

    @ApiModelProperty(value="用户类型文本" )
    private String typeText;

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名" )
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value="密码" )
    private String password;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value="真实姓名" )
    private String realName;

    /**
     * 审核状态 N:未审核 Y:已审核
     */
    @ApiModelProperty(value="审核状态 N:未审核 Y:已审核" )
    private String auditStatus;


    @ApiModelProperty(value="审核状态文本" )
    private String auditStatusText;
    /**
     * 电话
     */
    @ApiModelProperty(value="电话" )
    private String mobile;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(value="邮箱地址" )
    private String email;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    @ApiModelProperty(value="状态 0:禁用 1:可用 " )
    private Byte status;

    @ApiModelProperty(value="状态文本" )
    private String statusText;

    /**
     * 备注 
     */
    @ApiModelProperty(value="备注 " )
    private String remarks;

    /**
     * 创建时间
     * 默认值:CURRENT_TIMESTAMP
     */
    @ApiModelProperty(value="创建时间" )
    private String createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人" )
    private String createUser;

    /**
     * 最后修改时间
     * 默认值:CURRENT_TIMESTAMP
     */
    @ApiModelProperty(value="最后修改时间" )
    private String lastUpdateTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value="最后修改人" )
    private String lastUpdateUser;

    /**
     * 角色list
     */
    @ApiModelProperty(value="角色信息" )
    private TsRoleResDto roleResDto;
}