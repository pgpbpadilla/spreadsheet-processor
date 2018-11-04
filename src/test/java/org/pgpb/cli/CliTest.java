package org.pgpb.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CliTest {

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeMethod
    public void setUp() {
        Logger cliLogger = (Logger)LoggerFactory.getLogger(Cli.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        cliLogger.addAppender(listAppender);
    }

    @Test
    public void testParse() {
        String [] args = {"-f",  "sample.tsv"};
        Cli cli = new Cli(args);

        cli.parse();

        List<Level> errors = listAppender.list.stream()
            .map(ILoggingEvent::getLevel)
            .filter(level -> level.equals(Level.ERROR))
            .collect(Collectors.toList());
        assertThat(errors).isEmpty();
    }

    @Test
    public void testParseNoFileName() {
        String [] args = {"-f"};
        Cli cli = new Cli(args);

        cli.parse();

        List<Level> errors = listAppender.list.stream()
            .map(ILoggingEvent::getLevel)
            .filter(level -> level.equals(Level.ERROR))
            .collect(Collectors.toList());
        assertThat(errors).isNotEmpty();
    }
}