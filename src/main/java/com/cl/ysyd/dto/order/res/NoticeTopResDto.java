package com.cl.ysyd.dto.order.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "首页&看板响应Dto")
public class NoticeTopResDto {

    @ApiModelProperty(value="用户总数量" )
    private Long userNum;

    @ApiModelProperty(value="订单总数量" )
    private Long orderNum;

    @ApiModelProperty(value="订单完成总数量" )
    private Long orderCompleteNum;

    @ApiModelProperty(value="订单废除总数量" )
    private Long orderAbolishNum;

    @ApiModelProperty(value="订单总金额" )
    private BigDecimal orderTotalMoney;

    @ApiModelProperty(value="采购总金额" )
    private BigDecimal purchaseTotalMoney;
}
