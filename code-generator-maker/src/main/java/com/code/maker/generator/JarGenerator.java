package com.code.maker.generator;

import java.io.*;

/**
 * @author ctc
 * @date 2024/1/24
 */
public class JarGenerator {
    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        // 调用 Process 类执行 Maven 打包命令
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand = "mvn clean package -DskipTests=true";
        String mavenCommand = winMavenCommand;

        // 拆分命令，否则会被当成一整个参数
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));

        Process process = processBuilder.start();

        // 读取输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 等待命令执行结束
        int exitCode = process.waitFor();
        System.out.println("命令执行结束: " + exitCode);

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("C:\\Users\\frank\\IdeaProjects\\code-generator\\code-generator-maker\\generated\\acm-template-pro-generator");
    }
}
