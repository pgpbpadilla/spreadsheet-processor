package org.pgpb.cli;

import com.google.common.collect.ImmutableList;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pgpb.spreadsheet.Spreadsheet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Cli {
    private static final Logger LOGGER = LoggerFactory.getLogger(Cli.class);
    private String[] args = null;
    private Options options = new Options();

    public Cli(String[] args) {

        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("f", "file", true, "The path to the input file.");

    }

    public void parse() {
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                help();
            }

            if (cmd.hasOption("file")) {
                String filePath = cmd.getOptionValue("f");
                Spreadsheet sheet = null;

                try(Stream<String> contents = Files.lines(Paths.get(filePath))){

                    ImmutableList<String> lines =
                        contents.collect(ImmutableList.toImmutableList());
                    sheet = new Spreadsheet(lines);
                    sheet.toTSVRows().forEach(s -> System.out.println(s));
                } catch (IOException e) {
                    LOGGER.error("Could not read input file: " + filePath);
                }
            } else {
                help();
            }

        } catch (ParseException e) {
            LOGGER.error("Failed to parse comand line properties");
            help();
        }
    }

    private void help() {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("Main", options);
    }
}