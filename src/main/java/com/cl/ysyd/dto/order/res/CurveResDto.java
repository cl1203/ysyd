package com.cl.ysyd.dto.order.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "首页&看板曲线部分响应Dto")
public class CurveResDto {

    @ApiModelProperty(value="月份" )
    private String month;

    @ApiModelProperty(value="数量" )
    private Integer number;
}
