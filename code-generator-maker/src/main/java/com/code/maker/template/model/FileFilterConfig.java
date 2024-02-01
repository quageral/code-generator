package com.code.maker.template.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author ctc
 * @date 2024/1/31
 */
@Data
@Builder
public class FileFilterConfig {
    /**
     * 过滤范围
     */
    private String range;

    /**
     * 过滤规则
     */
    private String rule;

    /**
     * 过滤值
     */
    private String value;
}
