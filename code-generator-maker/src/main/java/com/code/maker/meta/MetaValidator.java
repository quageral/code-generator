package com.code.maker.meta;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.code.maker.meta.enums.FileGenerateTypeEnum;
import com.code.maker.meta.enums.FileTypeEnum;
import com.code.maker.meta.enums.ModelTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 元信息校验
 *
 * @author ctc
 * @date 2024/1/25
 */

public class MetaValidator {

    public static void doValidAndFill(Meta meta) {
        // 校验和填充根参数（一级参数）
        validAndFillMetaRoot(meta);
        // 校验和填充 fileConfig
        validAndFillFileConfig(meta);
        // 校验和填充 modelConfig
        validAndFillModelConfig(meta);
    }

    private static void validAndFillModelConfig(Meta meta) {
        // modelConfig 校验和默认值
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        List<Meta.ModelConfig.ModelInfo> modelInfoList = modelConfig.getModels();
        if (CollUtil.isEmpty(modelInfoList)) {
            return;
        }
        for (Meta.ModelConfig.ModelInfo modelInfo : modelInfoList) {
            // groupKey 不为空，是分组不需要校验
            String groupKey = modelInfo.getGroupKey();
            if (StrUtil.isNotEmpty(groupKey)) {
                // 生成中间参数
                List<Meta.ModelConfig.ModelInfo> subModelInfoList = modelInfo.getModels();
                String allArgsStr = subModelInfoList.stream()
                        .map(subModelInfo -> String.format("\"--%s\"", subModelInfo.getFieldName()))
                        .collect(Collectors.joining(", "));
                modelInfo.setAllArgsStr(allArgsStr);
                continue;
            }

            // 输出路径默认值
            String fieldName = modelInfo.getFieldName();
            if (StrUtil.isBlank(fieldName)) {
                throw new MetaException("未填写 fieldName");
            }

            String modelInfoType = modelInfo.getType();
            if (StrUtil.isEmpty(modelInfoType)) {
                modelInfo.setType(ModelTypeEnum.STRING.getValue());
            }
        }
    }

    private static void validAndFillFileConfig(Meta meta) {
        // fileConfig 校验和默认值
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        // sourceRootPath 必填
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("未填写 sourceRootPath");
        }

        // inputRootPath: .source + sourceRootPath 的最后一个层级路径
        String inputRootPath = fileConfig.getInputRootPath();

        if (StrUtil.isEmpty(inputRootPath)) {
            String defaultInputRootPath = ".source/"  + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
            fileConfig.setInputRootPath(defaultInputRootPath);
        }

        // outputRootPath 默认为当前路径下的 generated
        String outputRootPath = fileConfig.getOutputRootPath();
        if (StrUtil.isEmpty(outputRootPath)) {
            outputRootPath = "generated";
            fileConfig.setOutputRootPath(outputRootPath);
        }

        // type 默认为 dir
        String fileConfigType = fileConfig.getType();
        if (StrUtil.isEmpty(fileConfigType)) {
            fileConfigType = FileTypeEnum.DIR.getValue();
            fileConfig.setType(fileConfigType);
        }

        // files 校验和默认值
        List<Meta.FileConfig.FileInfo> fileInfoList = fileConfig.getFiles();
        if (CollUtil.isEmpty(fileInfoList)) {
            return;
        }
        for (Meta.FileConfig.FileInfo fileInfo : fileInfoList) {
            String type = fileInfo.getType();
            // group 类型不需要校验
            if (FileTypeEnum.GROUP.getValue().equals(type)) {
                continue;
            }
            // inputPath: 必填
            String inputPath = fileInfo.getInputPath();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("未填写 inputPath");
            }
            // outputPath: 默认等于 inputPath
            String outputPath = fileInfo.getOutputPath();
            if (StrUtil.isEmpty(outputPath)) {
                fileInfo.setOutputPath(inputPath);
            }

            // type: 默认为 inputPath 有的文件后缀则为 file，否则为 dir
            if (StrUtil.isBlank(type)) {
                //     无文件后缀
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    fileInfo.setType(FileTypeEnum.DIR.getValue());
                } else {
                    fileInfo.setType(FileTypeEnum.FILE.getValue());
                }
            }

            // generateType: 如果文件结尾不为ftl，generateType 默认为 static, 否则为 dynamic
            String generateType = fileInfo.getGenerateType();
            if (StrUtil.isBlank(generateType)) {
                if (inputPath.endsWith(".ftl")) {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
                } else {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
                }
            }

        }
    }

    private static void validAndFillMetaRoot(Meta meta) {
        // 基础信息校验和默认值
        String name = StrUtil.blankToDefault(meta.getName(), "my-generator");
        String description = StrUtil.blankToDefault(meta.getDescription(), "我的模板代码生成器");
        String author = StrUtil.blankToDefault(meta.getAuthor(), "ctc");
        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.code");
        String version = StrUtil.blankToDefault(meta.getVersion(), "1.0");
        String createTime = StrUtil.blankToDefault(meta.getCreateTime(), DateUtil.now());

        meta.setName(name);
        meta.setDescription(description);
        meta.setAuthor(author);
        meta.setBasePackage(basePackage);
        meta.setVersion(version);
        meta.setCreateTime(createTime);
    }
}

























