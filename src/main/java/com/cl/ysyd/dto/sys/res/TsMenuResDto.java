/**
 * TsMenuResDto.java
 * Created at 2020-11-26
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 菜单输出Dto类
 * @author chenlong  2020-11-26
 */
@Data
@ApiModel(value = "菜单&按钮响应Dto")
public class TsMenuResDto {
    /**
     * PK_ID
     */
    @ApiModelProperty(value="PK_ID" )
    private String pkId;

    /**
     * 菜单标题
     */
    @ApiModelProperty(value="标题" )
    private String title;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称" )
    private String name;

    /**
     * 菜单路径
     */
    @ApiModelProperty(value="排序" )
    private Integer sort;

    /**
     * 父菜单id
     * 默认值:0
     */
    @ApiModelProperty(value="父id" )
    private String parentId;

    /**
     * 权限类型(菜单or按钮)
     */
    @ApiModelProperty(value="权限类型(菜单or按钮)" )
    private String type;

    /**
     * 图标
     */
    @ApiModelProperty(value="图标" )
    private String icon;

    @ApiModelProperty(value="路径" )
    private String targetPage;

    /**
     * 状态 0:禁用 1:可用 
     * 默认值:1
     */
    @ApiModelProperty(value="状态 0:禁用 1:可用 " )
    private Byte status;

    /**
     * 创建时间
     * 默认值:CURRENT_TIMESTAMP
     */
    @ApiModelProperty(value="创建时间" )
    private Date createTime;

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
    private Date lastUpdateTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value="最后修改人" )
    private String lastUpdateUser;

    /**
     * 按钮集合
     */
    @ApiModelProperty(value = "二级菜单")
    private List<TsMenuResDto> menuResDtoList;

    /**
     * 按钮集合
     */
    @ApiModelProperty(value = "按钮集合")
    private List<TsMenuResDto> buttonResDtoList;

    /**
     * 按钮集合
     */
    @ApiModelProperty(value = "权限集合")
    private List<TsMenuResDto> children;

    @ApiModelProperty(value = "是否绑定")
    private Byte binding;
}
