package com.cl.ysyd.dto.order.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 订单输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "绑定角色和菜单按钮请求dto")
public class BindingRoleAndMenuReqDto {

    /**
     * 订单号
     */
    @ApiModelProperty(value="角色ID"  , required = true)
    @NotBlank(message="角色ID不能为空")
    private String roleId;

    /**
     * 图片地址
     */
    @ApiModelProperty(value="菜单按钮ID集合"  , required = true)
    @NotEmpty(message="菜单按钮ID集合不能为空")
    private List<String> menuIdList;


}
