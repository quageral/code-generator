package com.code.model;

import lombok.Data;

/**
 * @author ctc
 * @date 2024/1/20
 */
@Data
public class MainTemplateConfig {
    /**
     * 字符串填充值
     */
    private String author = "ctc";

    private String outputText = "输出结果：";

    /**
     * 是否循环
     */
    private boolean loop = false;


}
