package com.cl.ysyd.common.enums;

/**
 * @version V1.0
 */
public enum IsValidEnum {

    VALID(1, "启用"),
    INVALID(0, "禁用");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态描述
     */
    private String msg;

    /**
     * 构造函数
     *
     * @param code 状态编码
     * @param msg  状态描述
     */
    IsValidEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
