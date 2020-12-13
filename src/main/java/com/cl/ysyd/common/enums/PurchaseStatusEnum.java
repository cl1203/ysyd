package com.cl.ysyd.common.enums;

public enum PurchaseStatusEnum {
    WAIR_PURCHASE("wait_purchase", "待采购"),
    PURCHASE_ING("purchase_ing", "采购中"),
    PURCHASE_COMPLETED("purchase_completed", "采购完成");

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
    PurchaseStatusEnum(String code, String msg) {
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
