package com.mmall.common;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午4:13 18/11/6
 */
public enum  ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGLE_ARGUMENT(2, "ILLEGLE_ARGUMENT");


    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
