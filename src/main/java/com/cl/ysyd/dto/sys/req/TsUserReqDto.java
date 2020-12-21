/**
 * TsUserReqDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "用户请求Dto")
public class TsUserReqDto {
    /**
     * 用户类型code
     */
    @ApiModelProperty(value="用户类型code"  , required = true)
    @NotBlank(message="用户类型code不能为空")
    @Length(max=50,message="用户类型code字段过长, 最大长度为50")
    private String type;

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名"  , required = true)
    @NotBlank(message="用户名不能为空")
    @Length(max=100,message="用户名字段过长, 最大长度为100")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value="密码")
    @Length(max=100,message="密码字段过长, 最大长度为100")
    private String password;

    @ApiModelProperty(value = "新密码")
    private String newPassword;

    @ApiModelProperty(value = "确认新密码")
    private String newPasswordConfirm;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value="真实姓名"  , required = true)
    @NotBlank(message="真实姓名不能为空")
    @Length(max=100,message="真实姓名字段过长, 最大长度为100")
    private String realName;

    /**
     * 审核状态 N:未审核 Y:已审核
     */
    @ApiModelProperty(value="审核状态 N:未审核 Y:已审核"  , required = true)
    @NotBlank(message = "审核状态不能为空!")
    private String auditStatus;

    /**
     * 电话
     */
    @ApiModelProperty(value="电话" )
    @Length(max=50,message="电话字段过长, 最大长度为50")
    private String mobile;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(value="邮箱地址" )
    @Length(max=255,message="邮箱地址字段过长, 最大长度为255")
    private String email;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    @ApiModelProperty(value="状态 0:禁用 1:可用 " , required = true)
    @NotBlank(message = "状态不能为空!")
    private String status;

    /**
     * 备注 
     */
    @ApiModelProperty(value="备注" )
    @Length(max=128,message="备注字段过长, 最大长度为128")
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

    @ApiModelProperty(value="是否绑定公众号" )
    private String isBinding;
}
