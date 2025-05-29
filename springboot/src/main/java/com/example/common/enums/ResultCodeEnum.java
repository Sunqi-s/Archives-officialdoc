package com.example.common.enums;

public enum ResultCodeEnum {
    SUCCESS("200", "成功"),

    // 参数错误
    PARAM_ERROR("400", "参数异常"),
    PARAM_LOST_ERROR("4001", "参数缺失"),

    // 认证错误
    TOKEN_INVALID_ERROR("401", "无效的token"),
    TOKEN_CHECK_ERROR("401", "token验证失败，请重新登录"),
    USER_NOT_LOGIN("5002", "用户未登录"),

    // 业务错误
    SYSTEM_ERROR("500", "系统异常"),
    USER_EXIST_ERROR("5001", "用户名已存在"),
    USER_ACCOUNT_ERROR("5003", "账号或密码错误"),
    USER_NOT_EXIST_ERROR("5004", "用户不存在"),
    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),
    PARAM_END_TIME_ERROR("5006", "截止时间不能早于当前时间"),
    PARAM_TERM_ERROR("5007", "学期参数不合法"),

    // 新增收文相关错误
    DATA_EXIST_ERROR("5008", "数据已存在"),
    DATA_NOT_EXIST_ERROR("5009", "数据不存在"),
    OPERATION_ERROR("5010", "操作失败"),

    ;

    public final String code;
    public final String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
