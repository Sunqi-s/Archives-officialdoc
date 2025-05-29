package com.example.common;

public interface Constants {

    public static final  String TOKEN = "token";

    public static final String USER_DEFAULT_PASSWORD = "123";

    public static final String DEFAULT_PROCESS_TYPE = "未处理";

    public static final String PROCESS_TYPE_ARCHIVED = "存档";

    // 收文状态（0-待审核，1-流转中，2-已存档）
    public static final Integer STATUS_PENDING = 0;    // 待审核
    public static final Integer STATUS_PROCESSING = 1;  // 流转中
    public static final Integer STATUS_ARCHIVED = 2;    // 已存档

    // 密件类型（0-普通件，1-密件）
    public static final Integer SECRET_TYPE_NORMAL = 0;   // 普通件
    public static final Integer SECRET_TYPE_CONFIDENTIAL = 1; // 密件
}
