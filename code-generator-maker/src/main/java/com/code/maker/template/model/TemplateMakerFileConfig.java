package com.code.maker.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ctc
 * @date 2024/1/31
 */
@Data
public class TemplateMakerFileConfig {
    private List<FileInfoConfig> files;
    @Data
    @NoArgsConstructor
    public static class FileInfoConfig{
        private String path;
        private List<FileFilterConfig> filterConfigList;
    }
}
