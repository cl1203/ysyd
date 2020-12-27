package com.cl.ysyd.dto.order.res;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "订单响应Dto")
public class FileResDto {

    private String fileUrl;

    private String fileName;
}
