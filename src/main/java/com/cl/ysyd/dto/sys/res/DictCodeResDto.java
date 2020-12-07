/**
 * DictCodeResDto.java
 * Created at 2020-03-28
 * Created by xieyb
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据字典编码响应Dto
 */
@Data
@ApiModel(value = "数据字典编码响应Dto")
public class DictCodeResDto {

    private String pkId;

    /**
     * 字典编码
     */
    @ApiModelProperty(value = "字典编码")
    private String bizCode;

    /**
     * 字典文案
     */
    @ApiModelProperty(value = "字典文案")
    private String bizText;

    /**
     * 字典描述
     */
    @ApiModelProperty(value = "字典描述")
    private String description;

}
