package com.cl.ysyd.dto.sys.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(value = "登录请求Dto")
public class LoginReqDto {

    @NotBlank(message = "用户名不能为空!")
    @ApiModelProperty(value = "用户名"  , required = true)
    private String userName;

    @NotBlank(message = "密码不能为空!")
    @ApiModelProperty(value = "密码" , required = true)
    private String password;
}
