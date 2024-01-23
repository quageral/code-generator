package com.code;

import com.code.cli.CommandExecutor;

/**
 * @author ctc
 * @date 2024/1/19
 */// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // args = new String[]{"generate", "-l", "-a", "-o"};
       // args = new String[]{"config"};
       // args = new String[]{"list"};
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
