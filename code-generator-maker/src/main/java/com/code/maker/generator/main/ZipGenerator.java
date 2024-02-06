package com.code.maker.generator.main;

/**
 * 生成代码生成器压缩包
 *
 * @author ctc
 * @date 2024/2/19
 */

public class ZipGenerator extends GenerateTemplate {

    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        String distPath = super.buildDist(outputPath, sourceCopyDestPath, jarPath, shellOutputFilePath);
        return super.buildZip(distPath);
    }
}
