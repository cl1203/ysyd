/**
 * TsMenuReqDto.java
 * Created at 2020-11-26
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 菜单输出Dto类
 * @author chenlong  2020-11-26
 */
@Data
@ApiModel(value = "菜单请求Dto")
public class TsMenuReqDto {
    /**
     * 菜单标题
     */
    @ApiModelProperty(value="菜单标题" )
    @NotBlank(message="菜单标题不能为空")
    @Length(max=50,message="菜单标题字段过长, 最大长度为50")
    private String title;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称" )
    @NotBlank(message="名称不能为空")
    @Length(max=50,message="名称字段过长, 最大长度为50")
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
    @ApiModelProperty(value="父菜单id" )
    @NotBlank(message="父菜单id不能为空")
    @Length(max=32,message="父菜单id字段过长, 最大长度为32")
    private String parentId;

    /**
     * 权限类型(菜单or按钮)
     */
    @ApiModelProperty(value="权限类型(菜单or按钮)" )
    @NotBlank(message="权限类型(菜单or按钮)不能为空")
    @Length(max=50,message="权限类型(菜单or按钮)字段过长, 最大长度为50")
    private String type;

    /**
     * 图标
     */
    @ApiModelProperty(value="图标" )
    @NotBlank(message="图标不能为空")
    @Length(max=30,message="图标字段过长, 最大长度为30")
    private String icon;

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
    @NotBlank(message="创建人不能为空")
    @Length(max=32,message="创建人字段过长, 最大长度为32")
    private String createUser;

    /**
     * 最后修改时间
     * 默认值:CURRENT_TIMESTAMP
     */
    @ApiModelProperty(value="最后修改时间" )
    @NotNull(message="最后修改时间不能为空")
    private Date lastUpdateTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value="最后修改人" )
    @NotBlank(message="最后修改人不能为空")
    @Length(max=32,message="最后修改人字段过长, 最大长度为32")
    private String lastUpdateUser;

}