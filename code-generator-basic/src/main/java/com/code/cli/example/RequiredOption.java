package com.code.cli.example;

import picocli.CommandLine.Option;
/**
 * @author ctc
 * @date 2024/1/22
 */
class RequiredOption {

    @Option(names = "-a", required = true)
    String author;

    @Option(names = "-option")
    int[] values;
}
