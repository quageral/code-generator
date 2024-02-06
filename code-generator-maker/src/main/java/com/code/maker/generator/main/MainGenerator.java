package com.code.maker.generator.main;

/**
 * 生成器(不输出简化目录 Dist）
 * @author ctc
 * @date 2024/1/23
 */
public class MainGenerator extends GenerateTemplate {

    @Override
    protected String buildDist(String outputPath, String jarPath, String shellOutputFilePath, String sourceCopyDestPath) {
        System.out.println("不要输出Dist目录");
        return "";
    }
}
