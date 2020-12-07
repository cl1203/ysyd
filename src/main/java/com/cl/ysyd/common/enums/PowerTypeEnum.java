package com.cl.ysyd.common.enums;

public enum PowerTypeEnum {

    MENU("menu", "菜单"),
    BUTTON("button", "按钮");

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
    PowerTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
