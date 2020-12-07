/**
 * ControllerExceptionAdvice.java
 * Created at 2020-03-28
 * Created by zcz
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.common.interceptor;

import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.common.enums.ResponseCode;
import com.cl.ysyd.common.exception.BusiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常监控处理
 * 
 * @author zcz
 */
@ControllerAdvice
@ResponseBody
public class ControllerExceptionAdvice {

    /**
     * <p>
     * Description: 参数验证异常监控
     * </p>
     *
     * @param e 参数无效异常
     * @return 响应
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        ResponseData resultJson = new ResponseData();
        resultJson.setCode(ResponseCode.INVALID_PARAMETER.getCode());
        StringBuilder errMsg = new StringBuilder();
        for (ObjectError error : result.getAllErrors()) {
            if (errMsg.length() < 1) {
                errMsg.append(error.getDefaultMessage());
            } else {
                errMsg.append(",").append(error.getDefaultMessage());
            }
        }
        resultJson.setMsg(errMsg.toString());
        logger.warn("参数验证失败:{}", errMsg.toString());
        return resultJson;
    }

    /**
     * <p>
     * Field logger: 日志
     * </p>
     */
    private static Logger logger = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

  
    /**
     * <p>Description: 业务验证异常监控</p>
     * @param ex 业务异常
     * @return 响应
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(BusiException.class)
    public ResponseData handleMethodArgumentNotValidException(BusiException ex) {
        ResponseData result = new ResponseData();
        result.setCode(ex.getErrorCode());
        result.setMsg(ex.getMessage());
        logger.warn("ajax请求发生异常：errorCode={}, errorMsg={}", ex.getErrorCode(), ex.getMessage());
        return result;
    }

    /**
     * <p>Description: 系统异常监控</p>
     * @param ex 异常
     * @return 响应
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(Exception.class)
    public ResponseData handleMethodArgumentNotValidException(Exception ex) {
        ResponseData result = new ResponseData();
        result.setCode(ResponseCode.SYS_ERROR.getCode());
        result.setMsg(ex.getMessage());
        logger.error("ajax请求发生异常：errorCode={}, errorMsg={}", result.getCode(), ex.getMessage(), ex);
        return result;
    }

}
