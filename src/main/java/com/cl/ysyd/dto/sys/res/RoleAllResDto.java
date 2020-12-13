package com.cl.ysyd.dto.sys.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "所有角色响应Dto")
public class RoleAllResDto {

    @ApiModelProperty(value="主键" )
    private String key;

    /**
     * 角色名称
     */
    @ApiModelProperty(value="角色名称" )
    private String label;
}
