/**
 * DictType.java
 * Created at 2020-03-20
 * Created by xieyingbin
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 */
package com.cl.ysyd.common.enums;

/**
 * 数据字典公共编码
 *
 */
public enum DictType {


    ORDER_STATUS("order_status", "订单状态"),

    USER_TYPE("user_type", "用户类型"),

    ORDER_TYPE("order_type", "订单类型"),

    ORDER_SIZE("order_size", "订单尺码"),

    EXAMINE_STATUS("examine_status", "订单审核状态"),

    VALID_STATUS("valid_status", "有效状态"),

    AUDIT_STATUS("audit_status", "用户审核状态"),

    IS_ALL("is_all", "是否全部数据权限"),

    POWER_TYPE("power_type", "权限类型"),

    IS_CANCEL("is_cancel", "订单是否作废"),

    PURCHASE_STATUS("purchase_status", "采购状态状态");




    /**
     * 字典编码
     */
    private String code;

    /**
     * 字典描述
     */
    private String msg;

    /**
     * 构造函数
     *
     * @param code 字典编码
     * @param msg  字典描述
     */
    private DictType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
