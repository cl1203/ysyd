/**
 * ResponseCode.java
 * Created at 2020-03-20
 * Created by xieyingbin
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 */
package com.cl.ysyd.common.enums;

/**
 * 请求响应码定义
 */
public enum ResponseCode {
    /**
     * <p>
     * Field SUCCESS: 请求成功.
     * </p>
     */
    SUCCESS("2000", "{success}"),

    /**
     * <p>
     * Field INVALID_PARAMETER: 无效参数.
     * </p>
     */
    INVALID_PARAMETER("4000", "{invalid.parameter}"),

    /**
     * <p>
     * Field AUTH_FAILURES: 认证失败.
     * </p>
     */
    AUTH_FAILURES("4001", "{auth.failures}"),

    /**
     * <p>
     * Field UNAUTHORIZED: 未授权.
     * </p>
     */
    UNAUTHORIZED("4002", "{unauthorized}"),

    /**
     * <p>
     * Field TOKEN_INVALID: TOEKN无效.
     * </p>
     */
    TOKEN_INVALID("4003", "{invalid.token}"),
    
    /**
     * <p>
     * Field LOGIN_FAILED: 登录失败.
     * </p>
     */
    LOGIN_FAILED("4004", "{login.failed}"),

    /**
     * <p>
     * Field ACCOUNT_LOCKED: 账号锁定.
     * </p>
     */
    ACCOUNT_LOCKED("4005", "{account.locked}"),

    /**
     * <p>
     * Field FAILURE: 请求失败.
     * </p>
     */
    FAILURE("5000", "{failure}"),
    
    /**
     * <p>
     * Field SYS_ERROR: 系统错误.
     * </p>
     */
    SYS_ERROR("5001", "{system.error}"),
    
    /**
     * <p>
     * Field DATA_BASE_ERROR: 数据库错误.
     * </p>
     */
    DATA_BASE_ERROR("5002", "{database.error}"),
    
    /**
     * <p>
     * Field NETWORK_TIMEOUT: 网络超时.
     * </p>
     */
    NETWORK_TIMEOUT("5003", "{network.timeout}");

    /**
     * <p>
     * Field code: code.
     * </p>
     */
    private String code;

    /**
     * <p>
     * Field msg: msg.
     * </p>
     */
    private String msg;

    /**
     * <p>
     * Description: CommonCode.
     * </p>
     *
     * @param code 代码
     * @param msg 文字表述
     */
    private ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * <p>
     * Description: code的get方法.
     * </p>
     *
     * @return 结果
     */
    public String getCode() {
        return this.code;
    }

    /**
     * <p>
     * Description: msg的get方法.
     * </p>
     *
     * @return 结果
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * <p>
     * Description: code的set方法.
     * </p>
     *
     * @param code code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * <p>
     * Description: msg的set方法.
     * </p>
     *
     * @param msg msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
