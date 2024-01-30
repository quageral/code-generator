package com.code.maker.generator.file;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author ctc
 * @date 2024/1/20
 */
public class FileGenerator {

    public static void doGenerate(Object model) throws TemplateException, IOException {
        String inputRootPath = "";
        String outputRootPath = "";

        String inputPath;
        String outputPath;

        inputPath = new File(inputRootPath,"").getAbsolutePath();
        outputPath = new File(outputRootPath,"").getAbsolutePath();
        DynamicFileGenerator.doGenerate(inputPath, outputPath, model);

        inputPath = new File(inputRootPath,".gitignore").getAbsolutePath();
        outputPath = new File(outputRootPath,".gitignore").getAbsolutePath();
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);

        inputPath = new File(inputRootPath,"README.md").getAbsolutePath();
        outputPath = new File(outputRootPath,"README.md").getAbsolutePath();
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
    }
}
