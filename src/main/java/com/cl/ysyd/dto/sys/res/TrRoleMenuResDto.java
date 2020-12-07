/**
 * TrRoleMenuResDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 角色菜单关系表输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "角色菜单关系表响应Dto")
public class TrRoleMenuResDto {
    /**
     * PK_ID
     */
    @ApiModelProperty(value="PK_ID" )
    private String pkId;

    /**
     * 角色id
     */
    @ApiModelProperty(value="角色id" )
    private String roleId;

    /**
     * 菜单id
     */
    @ApiModelProperty(value="菜单id" )
    private String menuId;

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
}