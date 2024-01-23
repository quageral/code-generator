package com.code.cli.pattern;

/**
 * @author ctc
 * @date 2024/1/23
 */
public class TurnOnCommand implements Command {
    private Device device;

    public TurnOnCommand(Device device) {
        this.device = device;
    }

    public void execute() {
        device.turnOn();
    }
}
