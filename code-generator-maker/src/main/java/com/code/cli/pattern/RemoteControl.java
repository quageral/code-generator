package com.code.cli.pattern;

/**
 * @author ctc
 * @date 2024/1/23
 */
public class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}
