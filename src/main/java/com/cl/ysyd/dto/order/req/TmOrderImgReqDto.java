/**
 * TmOrderImgReqDto.java
 * Created at 2021-04-21
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.order.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author chenlong  2021-04-21
 */
@Data
@ApiModel(value = "请求Dto")
public class TmOrderImgReqDto {
    /**
     * 订单号
     */
    @ApiModelProperty(value="订单号" )
    @NotBlank(message="订单号不能为空")
    @Length(max=50,message="订单号字段过长, 最大长度为50")
    private String orderNo;

    /**
     * 详情图片地址
     */
    @ApiModelProperty(value="详情图片地址" )
    @NotBlank(message="详情图片地址不能为空")
    @Length(max=255,message="详情图片地址字段过长, 最大长度为255")
    private String imgDetailUrl;

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