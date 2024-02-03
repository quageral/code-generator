package com.code.maker.template.model;

import lombok.Data;

/**
 * @author ctc
 * @date 2024/2/3
 */
@Data
public class TemplateMakerOutputConfig {
    // 从未分组文件中移除组内的同名文件
    private boolean removeGroupFilesFromRoot = true;
}
