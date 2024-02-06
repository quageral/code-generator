package com.code.maker;

// import com.code.maker.cli.CommandExecutor;

import com.code.maker.generator.main.GenerateTemplate;
import com.code.maker.generator.main.MainGenerator;
import com.code.maker.generator.main.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author ctc
 * @date 2024/1/19
 */// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
//        GenerateTemplate generateTemplate = new MainGenerator();
        GenerateTemplate generateTemplate = new ZipGenerator();
        generateTemplate.doGenerate();
    }
}
