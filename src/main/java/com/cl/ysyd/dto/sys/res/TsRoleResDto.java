/**
 * TsRoleResDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "角色响应Dto")
public class TsRoleResDto {
    /**
     * 主键
     */
    @ApiModelProperty(value="主键" )
    private String pkId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value="角色名称" )
    private String roleName;

    /**
     * 是否全部数据权限 Y: 是 N:否
     */
    @ApiModelProperty(value="是否全部数据权限 Y: 是 N:否" )
    private String isAll;

    @ApiModelProperty(value="是否全部数据权限文本" )
    private String isAllText;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    @ApiModelProperty(value="状态 0:禁用 1:可用 " )
    private Byte status;

    @ApiModelProperty(value="角色状态文本" )
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

    @ApiModelProperty(value="创建人名称" )
    private String createUserName;

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
     * 菜单集合
     */
    @ApiModelProperty(value = "一级菜单集合")
    private List<TsMenuResDto> menuResDtoList;


}
