package org.pgpb.cli;

import com.google.common.collect.ImmutableList;
import org.apache.commons.cli.*;
import org.pgpb.spreadsheet.Spreadsheet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());
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

                } catch (IOException e) {
                    log.log(Level.SEVERE, e.getLocalizedMessage());
                }

                sheet.toTSVRows().forEach(s -> System.out.println(s));
            } else {
                help();
            }

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse comand line properties", e);
            help();
        }
    }

    private void help() {
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("Main", options);
        System.exit(0);
    }
}