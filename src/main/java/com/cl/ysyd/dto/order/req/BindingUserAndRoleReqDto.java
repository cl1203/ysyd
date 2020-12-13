package com.cl.ysyd.dto.order.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 订单输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "绑定用户和角色请求dto")
public class BindingUserAndRoleReqDto {

    /**
     * 订单号
     */
    @ApiModelProperty(value="用户ID"  , required = true)
    @NotBlank(message="用户ID不能为空")
    private String userId;

    /**
     * 图片地址
     */
    @ApiModelProperty(value="角色ID"  , required = true)
    @NotEmpty(message="角色ID不能为空")
    private String roleId;


}
