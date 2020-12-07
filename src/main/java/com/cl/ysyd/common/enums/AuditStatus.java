package com.cl.ysyd.common.enums;

public enum AuditStatus {

    REVIEWED("Y", "已审核"),
    NOT_REVIEWED("N", "未审核");

    /**
     * 状态编码
     */
    private String code;

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
    AuditStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
