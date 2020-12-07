/**
 * ResponseData.java
 * Created at 2020-03-20
 * Created by xieyingbin
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 */
package com.cl.ysyd.common.constants;

import com.cl.ysyd.common.enums.ResponseCode;
import com.cl.ysyd.common.utils.MessageUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.text.MessageFormat;

/**
 * 请求响应数据对象
 */
@ApiModel
public class ResponseData<T> {

    /**
     * <p>
     * Field code: 返回码 .
     * </p>
     */
    @ApiModelProperty(name = "code", value = "返回码", required = true, dataType = "String", example = "2000")
    private String code;

    /**
     * <p>
     * Field msg: 描述.
     * </p>
     */
    @ApiModelProperty(name = "msg", value = "描述", required = true, dataType = "String", example = "success")
    private String msg;

    /**
     * <p>
     * Field data: 响应数据.
     * </p>
     */
   
    @ApiModelProperty(name = "data", value = "响应数据", required = true, example = "")
    private T data;

    /**
     * <p>
     * Description: 无参响应数据.
     * </p>
     */
    public ResponseData() {
        this.code = ResponseCode.SUCCESS.getCode();
        this.msg = ResponseCode.SUCCESS.getMsg();
    }

    /**
     * <p>
     * Description: 有参响应数据.
     * </p>
     *
     * @param respCode 响应码
     */
    public ResponseData(ResponseCode respCode) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
        this.data = null;
    }

    /**
     * <p>
     * Description: 有参响应方法.
     * </p>
     *
     * @param respCode 响应码
     * @param errMsg errMsg
     */
    public ResponseData(ResponseCode respCode, String errMsg) {
        this.code = respCode.getCode();
        this.msg = errMsg;
        this.data = null;
    }

    /**
     * 带参数消息的返回对象.< br> .
     *
     * 例子：<br>
     * new ResponseData<> (ResponseCode.INVALID_PARAMETER, new String[]
     * {"param1"});
     *
     * @param respCode 响应码
     * @param params 消息参数
     */
    public ResponseData(ResponseCode respCode, String[] params) {
        this.code = respCode.getCode();
        this.msg = MessageFormat.format(respCode.getMsg(), (Object[]) params);
        this.data = null;
    }

    /**
     * <p>
     * Description: 有参响应方法.
     * </p>
     *
     * @param code 编码
     * @param msg 信息
     */
    public ResponseData(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    /**
     * <p>
     * Description: 有参响应方法.
     * </p>
     *
     * @param code 编码
     * @param msg 信息
     * @param data 数据
     */
    public ResponseData(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * <p>
     * Description: 有参响应方法.
     * </p>
     *
     * @param data 数据
     */
    public ResponseData(T data) {
        this.code = ResponseCode.SUCCESS.getCode();
        this.msg = ResponseCode.SUCCESS.getMsg();
        this.data = data;
    }

    /**
     * <p>
     * Description: 有参响应方法.
     * </p>
     *
     * @param data 数据
     * @param respCode 响应码
     */
    public ResponseData(T data, ResponseCode respCode) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
        this.data = data;
    }

    /**
     * <p>
     * Description: code的get方法.
     * </p>
     *
     * @return 编码
     */
    public String getCode() {
        return this.code;
    }

    /**
     * <p>
     * Description: data的get方法.
     * </p>
     *
     * @return 对象数据
     */
    public T getData() {
        return this.data;
    }

    /**
     * <p>
     * Description: msg的get方法.
     * </p>
     *
     * @return 消息
     */
    public String getMsg() {
    	String message = "";
    	if (MessageUtil.isExpMessage(this.msg)) {
    		message = MessageUtil.getI18nExpMessage(this.msg);
    	} else {
    		message = MessageUtil.getI18nMessage(this.msg);
    	}
    	return message;
        //return this.msg;
    }

    /**
     * <p>
     * Description: code的set方法.
     * </p>
     *
     * @param code 返回编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * <p>
     * Description: data的set方法.
     * </p>
     *
     * @param data 对象数据
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * <p>
     * Description: msg的set方法.
     * </p>
     *
     * @param msg 消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
