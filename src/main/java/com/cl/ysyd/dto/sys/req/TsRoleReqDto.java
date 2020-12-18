/**
 * TsRoleReqDto.java
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
 * 角色输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "角色请求Dto")
public class TsRoleReqDto {
    /**
     * 角色名称
     */
    @ApiModelProperty(value="角色名称" , required = true)
    @NotBlank(message="角色名称不能为空")
    @Length(max=50,message="角色名称字段过长, 最大长度为50")
    private String roleName;

    /**
     * 是否全部数据权限 Y: 是 N:否
     */
    @ApiModelProperty(value="是否全部数据权限 Y: 是 N:否" , required = true)
    @NotBlank(message="是否全部数据权限不能为空")
    private String isAll;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    @ApiModelProperty(value="状态 0:禁用 1:可用 ")
    private String status;

    /**
     * 备注 
     */
    @ApiModelProperty(value="备注 " )
    @Length(max=128,message="备注 字段过长, 最大长度为128")
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
    @Length(max=32,message="创建人字段过长, 最大长度为32")
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
    @Length(max=32,message="最后修改人字段过长, 最大长度为32")
    private String lastUpdateUser;
}
