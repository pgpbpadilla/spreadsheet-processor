package org.pgpb.cli;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CliTest {

    @Test
    public void testParse() {
        String [] args = {"-file sample.tsv"};
        Cli cli = new Cli(args);
        cli.parse();
    }
}