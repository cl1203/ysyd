/**
 * TrUserRoleResDto.java
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
 * 用户角色关系输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "用户角色关系响应Dto")
public class TrUserRoleResDto {
    /**
     * PK_ID
     */
    @ApiModelProperty(value="PK_ID" )
    private String pkId;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id" )
    private String userId;

    /**
     * 角色id
     */
    @ApiModelProperty(value="角色id" )
    private String roleId;

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