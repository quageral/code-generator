package com.code.cli.pattern;

/**
 * @author ctc
 * @date 2024/1/23
 */
public class TurnOffCommand implements Command {
    private Device device;

    public TurnOffCommand(Device device) {
        this.device = device;
    }

    public void execute() {
        device.turnOff();
    }
}
