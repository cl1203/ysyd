package com.cl.ysyd.common.enums;

public enum OrderStatusEnum {

    WAITING("waiting", "待接单"),
    ORDERING("ordering", "接单中"),
    PURCHASING("purchasing", "采购中"),
    CUTTING("cutting", "裁剪中"),
    SEWING("sewing", "缝制中"),
    SORTING("sorting", "后整中"),
    SETTLEMENTING("settlementing", "结算中"),
    COMPLETED("completed", "已完成");

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
    OrderStatusEnum(String code, String msg) {
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
