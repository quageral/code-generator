package com.code.cli.example;

import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * @author ctc
 * @date 2024/1/22
 */


public class Login implements Runnable {
    @Option(names = {"-u", "--user"}, description = "User name")
    String user;

    @Option(names = {"-p", "--password"}, arity = "0..1", description = "Passphrase", interactive = true)
    String password;

    @Option(names = {"-cp", "--checkPassword"}, arity = "0..1", description = "Check Password", interactive = true)
    String checkPassword;

    public void run() {
        System.out.println("password = " + password);
        System.out.println("checkPassword = " + checkPassword);

    }

    public static void main(String[] args) {
        new CommandLine(new Login()).execute("-u", "user123", "-cp", "-p", "345");
    }
}
