package com.sxpost.yyqdapi.util;

/**
 * @author Barkley.Chang
 * @className:ExampleEnum
 * @description: 枚举类Demo
 * @date 2021-06-08 08:54
 */
public enum ExampleEnum {
    /** 枚举相关 */
    ONE(1, "one(1)"),
    TWO(2, "two(2)"),
    THREE(3, "two(3)");

    /** 字段相关 */
    private final int value;
    private final String desc;

    /** 构造方法 */
    ExampleEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /** 获取取值 */
    public int getValue() {
        return value;
    }

    /** 获取描述 */
    public String getDesc() {
        return desc;
    }
}