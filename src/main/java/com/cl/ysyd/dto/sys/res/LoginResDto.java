package com.cl.ysyd.dto.sys.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "登录响应Dto")
public class LoginResDto implements Serializable {

    private static final long serialVersionUID = 5988040630360408222L;

    @ApiModelProperty(value = "用户信息")
    private TsUserResDto userResDto;

    @ApiModelProperty(value = "角色信息/包含菜单信息 按钮信息")
    private TsRoleResDto roleResDto;

    @ApiModelProperty(value = "一级菜单集合")
    private List<TsMenuResDto> menuResDtoList;

    @ApiModelProperty(value = "token")
    private String token;

}
