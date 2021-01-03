package com.cl.ysyd.dto.order.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "首页&看板扇形部分响应Dto")
public class SectorResDto {

    @ApiModelProperty(value="数量" )
    private Integer value;

    @ApiModelProperty(value="订单状态" )
    private String name;

    @ApiModelProperty(value="序号" )
    private int seq;

}
