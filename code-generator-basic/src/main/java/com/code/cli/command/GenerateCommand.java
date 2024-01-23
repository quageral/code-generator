package com.code.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.code.generator.MainGenerator;
import com.code.model.MainTemplateConfig;
import freemarker.template.TemplateException;
import lombok.Data;
import picocli.CommandLine.*;

import java.io.IOException;

/**
 * @author ctc
 * @date 2024/1/23
 */
@Command(name = "generate", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Runnable{
    /**
     * 作者注释
     */
    @Option(names= {"-a", "--author"}, description = "作者注释", arity = "0..1", interactive = true, echo=true)
    private String author = "ctc";

    /**
     * 输出信息
     */
    @Option(names= {"-o", "--outputText"}, description = "输出信息", arity = "0..1", interactive = true, echo=true)
    private String outputText = "输出结果：";

    /**
     * 是否循环
     */
    @Option(names= {"-l", "--loop"}, description = "是否循环", arity = "0..1", interactive = true, echo=true)
    private boolean loop = false;

    @Override
    public void run() {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        BeanUtil.copyProperties(this, mainTemplateConfig);
        System.out.println("配置信息：" + mainTemplateConfig);
        try {
            MainGenerator.doGenerate(mainTemplateConfig);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
