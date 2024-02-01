package com.code.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.code.maker.meta.Meta;
import com.code.maker.meta.enums.FileGenerateTypeEnum;
import com.code.maker.meta.enums.FileTypeEnum;
import com.code.maker.template.enums.FileFilterRangeEnum;
import com.code.maker.template.enums.FileFilterRuleEnum;
import com.code.maker.template.model.FileFilterConfig;
import com.code.maker.template.model.TemplateMakerFileConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板制作工具
 *
 * @author ctc
 * @date 2024/1/28
 */
public class TemplateMaker {

    public static void main(String[] args) {
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 模板生成器");

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "code-generator-demo-projects/springboot-init";
        String inputFilePath1 = "src/main/java/com/yupi/springbootinit/common";
        String inputFilePath2 = "src/main/java/com/yupi/springbootinit/constant";
        List<String> inputFilePathList = Arrays.asList(inputFilePath1);


        // 模型参数信息（首次）
        // Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        // modelInfo.setFieldName("outputText");
        // modelInfo.setType("String");
        // modelInfo.setDefaultValue("sum = ");

        // 模型参数信息（第二次）
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");

        // 替换变量（首次）
        String searchStr = "BaseResponse";
        // 替换变量（第二次）
        // String searchStr = "MainTemplate";

        // common包下，含 Base 的文件
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath1);
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
                .range(FileFilterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("Base")
                .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFilterConfigList(fileFilterConfigList);

        // 分组配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);


        // controller包
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(inputFilePath2);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1, fileInfoConfig2));

        long id = makeTemplate(meta, originProjectPath, templateMakerFileConfig, modelInfo, searchStr, 1L);
        System.out.println(id);

    }

    public static long makeTemplate(Meta newMeta, String originProjectPath, TemplateMakerFileConfig templateMakerFileConfig, Meta.ModelConfig.ModelInfo modelInfo, String searchStr, Long id) {
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }

        // 复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp"; // 复制模板到临时工作空间，防止每次不同的生成参数污染源文件
        String templatePath = tempDirPath + File.separator + id;
        // 是否首次制作模板
        // 目录不存在，则是首次制作
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }

        // 一、输入信息
        // 输入文件信息
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        // 注意 win 系统需要对路径进行转义
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");
        List<TemplateMakerFileConfig.FileInfoConfig> fileConfigInfoList = templateMakerFileConfig.getFiles();

        // 二、制作文件模板
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(); // 存放所有fileInfo
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileConfigInfoList) {  // 遍历所有输入路径
            String inputFilePath = fileInfoConfig.getPath();
            String absoluteInputFilePath = sourceRootPath + File.separator + inputFilePath;

            // 如果是相对路径，改为绝对路径
            if (!inputFilePath.startsWith(sourceRootPath)) {
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }

            // 获取过滤后的文件列表（不会存在目录）
            List<File> fileList = FileFilter.doFilter(inputFilePath, fileInfoConfig.getFilterConfigList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, file, sourceRootPath);
                newFileInfoList.add(fileInfo);
            }
        }

        // 如果是文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();

            // 新增分组配置
            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setGroupName(groupName);
            // 将所有文件添加到一个分组中
            groupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);

        }


        // 三、生成配置文件meta.json
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";

        // 如果已有 meta 文件，说明不是第一次制作，则在 meta 基础上进行修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;

            // 1. 追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.add(modelInfo);

            // 配置去重
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModels(modelInfoList));
        } else {
            // 1. 构造配置参数
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);

            fileInfoList.addAll(newFileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.add(modelInfo);
        }

        // 2. 输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }

    private static Meta.FileConfig.FileInfo makeFileTemplate(Meta.ModelConfig.ModelInfo modelInfo, String searchStr, File inputFile, String sourceRootPath) {
        // 要挖坑的文件绝对路径（用于制作模板）
        // 注意 win 系统需要对路径进行转义
        String fileInputAbsolutePath = inputFile.getAbsolutePath().replaceAll("\\\\", "/");
        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";
        // 要挖坑的文件（注意一定是相对路径）
        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath + "/", ""); // 将文件的绝对路径转换为相对路径
        String fileOutputPath = fileInputPath + ".ftl";

        // 二、使用字符串替换，生成模板文件
        String fileContent;
        // 如果已有模板文件，则在其基础上再次挖坑
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else { // 否则，从原始文件中读取
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        String replacement = String.format("${%s}", modelInfo.getFieldName()); // 生成需要替换的字符串
        String newFileContent = StrUtil.replace(fileContent, searchStr, replacement); // 在文件中替换

        // 文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());

        if (newFileContent.equals(fileContent)) { // 和原文件一致，没有挖坑，则为静态生成
            // 输出路径 = 输入路径
            fileInfo.setOutputPath(fileInputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        } else { // 否则动态生成模板文件

            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        }

        return fileInfo;
    }


    /**
     * 文件去重
     *
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 同分组内的文件 merge, 不同分组保留

        // 1.有分组的，以组为单位划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList
                .stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));

        // 2.同组内的文件配置合并
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()){
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    .collect(
                            Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r))
                    .values()  // o -> o 为整个fileInfo对象，e -> r 为去重策略，保留新值
                    );

            // 使用新的 group 配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }

        // 3. 将文件分组添加到结果列表
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        // 4. 将未分组的文件添加到结果列表
        List<Meta.FileConfig.FileInfo> noGroupFileInfoList = fileInfoList.stream().filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)
                ).values()));
        return resultList;

    }

    /**
     * 模型去重
     *
     * @param modelInfoList
     * @return
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(modelInfoList.stream().collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)).values());
        return newModelInfoList;
    }
}
