/**
 * DictResDto.java
 * Created at 2020-03-28
 * Created by xieyb
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 数据字典响应Dto
 */
@Data
@ApiModel(value = "数据字典响应Dto")
public class DictResDto {

    /**
     * pkId
     */
    @ApiModelProperty(value = "pkId")
    private String pkId;

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    private String bizType;

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
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private String seq;

    /**
     * 字典描述
     */  
    @ApiModelProperty(value = "字典描述")
    private String description;
}
