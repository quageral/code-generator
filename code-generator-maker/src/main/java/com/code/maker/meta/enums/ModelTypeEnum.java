package com.code.maker.meta.enums;

/**
 * 模型类型枚举
 *
 * @author ctc
 * @date 2024/1/25
 */
public enum ModelTypeEnum {

    STRING("字符串", "String"),
    BOOLEAN("布尔", "Boolean");
    private final String text;
    private final String value;

    ModelTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
