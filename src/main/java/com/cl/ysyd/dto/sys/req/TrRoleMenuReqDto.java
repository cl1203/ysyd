/**
 * TrRoleMenuReqDto.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 角色菜单关系表输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "角色菜单关系表请求Dto")
public class TrRoleMenuReqDto {
    /**
     * 角色id
     */
    @ApiModelProperty(value="角色id" )
    @NotBlank(message="角色id不能为空")
    @Length(max=32,message="角色id字段过长, 最大长度为32")
    private String roleId;

    /**
     * 菜单id
     */
    @ApiModelProperty(value="菜单id" )
    @NotBlank(message="菜单id不能为空")
    @Length(max=32,message="菜单id字段过长, 最大长度为32")
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