/**
 * TrUserOrderReqDto.java
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
 * 用户订单关系输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "用户订单关系请求Dto")
public class TrUserOrderReqDto {
    /**
     * 用户ID
     */
    @ApiModelProperty(value="用户ID" )
    @NotBlank(message="用户ID不能为空")
    @Length(max=32,message="用户ID字段过长, 最大长度为32")
    private String userId;

    /**
     * 订单ID
     */
    @ApiModelProperty(value="订单ID" )
    @NotBlank(message="订单ID不能为空")
    @Length(max=32,message="订单ID字段过长, 最大长度为32")
    private String orderId;

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