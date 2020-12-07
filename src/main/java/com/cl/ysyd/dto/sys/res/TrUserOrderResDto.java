/**
 * TrUserOrderResDto.java
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
 * 用户订单关系输出Dto类
 * @author chenlong  2020-11-24
 */
@Data
@ApiModel(value = "用户订单关系响应Dto")
public class TrUserOrderResDto {
    /**
     * 主键
     */
    @ApiModelProperty(value="主键" )
    private String pkId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value="用户ID" )
    private String userId;

    /**
     * 订单ID
     */
    @ApiModelProperty(value="订单ID" )
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