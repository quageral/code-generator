package com.code.generator;

import com.code.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

import static com.code.generator.StaticGenerator.copyFilesByRecursive;

/**
 * @author ctc
 * @date 2024/1/20
 */
public class MainGenerator {

    public static void main(String[] args) throws TemplateException, IOException {
        // 静态代码生成
        // 获取整个项目的根路径
        String projectPath = System.getProperty("user.dir");

        // 输入路径：ACM 示例代码模板目录
        String inputPath =  new File(projectPath , "code-generator-demo-projects" + File.separator + "acm-template").getAbsolutePath();
        // 输出路径：直接输出到项目的根目录
        String outputPath = projectPath;
        copyFilesByRecursive(inputPath, outputPath);

        // 动态代码生成

        String DynamicInputPath = projectPath + File.separator + "code-generator-basic/src/main/resources/templates/MainTemplate.java.ftl";
        String DynamicOutputPath = projectPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        // 创建数据模型
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("ctc");
        mainTemplateConfig.setLoop(true);
        mainTemplateConfig.setOutputText("输出结果：");
        DynamicGenerator.doGenerate(DynamicInputPath, DynamicOutputPath, mainTemplateConfig);
    }

    public static void doGenerate(MainTemplateConfig mainTemplateConfig) throws TemplateException, IOException {
        // 静态代码生成
        // 获取整个项目的根路径
        String projectPath = System.getProperty("user.dir");

        // 输入路径：ACM 示例代码模板目录
        File parentFile = new File(projectPath).getParentFile();
        String inputPath = new File(parentFile, "code-generator-demo-projects/acm-template").getAbsoluteFile().getAbsolutePath();
        // String inputPath =  new File(projectPath , "code-generator-demo-projects" + File.separator + "acm-template").getAbsolutePath();
        // 输出路径：直接输出到项目的根目录
        String outputPath = projectPath;
        copyFilesByRecursive(inputPath, outputPath);

        // 动态代码生成

        // String DynamicInputPath = projectPath + File.separator + "code-generator-basic/src/main/resources/templates/MainTemplate.java.ftl";
        String DynamicInputPath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String DynamicOutputPath = projectPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        // 创建数据模型
        DynamicGenerator.doGenerate(DynamicInputPath, DynamicOutputPath, mainTemplateConfig);
    }
}
