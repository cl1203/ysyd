/**
 * DictReqDto.java
 * Created at 2020-03-28
 * Created by xieyb
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.dto.sys.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 数据字典reqDto
 * 
 * @author xieyb 2020-03-28
 */
@Data
@ApiModel(value = "字典请求Dto")
public class DictReqDto {

	/**
	 * 字典类型
	 */
	@ApiModelProperty(value = "字典类型")
	@NotEmpty(message = "I18n.NotEmpty({Dict.BizType})")
	@Length(max = 30, message = "I18n.Length({Dict.BizType}, 0, 30)")
	private String bizType;

	/**
	 * 字典编码
	 */
	@ApiModelProperty(value = "字典编码")
	@NotEmpty(message = "I18n.NotEmpty({Dict.BizCode})")
	@Length(max = 30, message = "I18n.Length({Dict.BizCode}, 0, 30)")
	private String bizCode;

	/**
	 * 字典文案
	 */
	@ApiModelProperty(value = "字典文本")
	@NotEmpty(message = "I18n.NotEmpty({Dict.BizText})")
	@Length(max = 100, message = "I18n.Length({Dict.BizText}, 0, 100)")
	private String bizText;

	/**
	 * 字典描述
	 */
	@ApiModelProperty(value = "字典描述")
	@Length(max = 500, message = "I18n.Length({Dict.Description}, 0, 500)")
	private String description;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private String seq;
}
