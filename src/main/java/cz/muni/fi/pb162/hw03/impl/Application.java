package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw02.LabelMatcher;
import cz.muni.fi.pb162.hw02.impl.LabelMatcherImpl;
import cz.muni.fi.pb162.hw03.cmd.CommandLine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Application Runtime
 */
final class Application {

    private final ApplicationOptions options;

    Application(ApplicationOptions options, CommandLine cli) {
        Objects.requireNonNull(options);
        Objects.requireNonNull(cli);

        this.options = options;
    }

    void run() throws IOException {
        BufferedReader dataReader = Files.newBufferedReader(options.getInput(), options.getCharset());

        Map<String, LabelMatcher> matchers = new HashMap<>();
        BufferedReader filterReader = Files.newBufferedReader(options.getFilters(), options.getCharset());
        filterReader.readLine();
        String filter;
        while ((filter = filterReader.readLine()) != null) {
            matchers.put(filter.split(options.getDelimiter())[0],
                    new LabelMatcherImpl(filter.split(options.getDelimiter())[1].trim()));
        }
        filterReader.close();
        boolean[] firstLines = new boolean[matchers.size()];
        BufferedWriter[] writers = new BufferedWriter[matchers.size()];

        String line;
        String dataHeader = dataReader.readLine();
        int labelsIndex = Arrays.asList(dataHeader.split(options.getDelimiter())).indexOf(options.getLabelColumn());
        while ((line = dataReader.readLine()) != null) {
            String labels = line.split(options.getDelimiter())[labelsIndex];
            int currentIndex = 0;
            for (String matcher : matchers.keySet()) {
                if (matchers.get(matcher).matches(new Title(labels))) {
                    if (firstLines[currentIndex]) {
                        writers[currentIndex].write(String.format("%s\n", line));
                    } else {
                        firstLines[currentIndex] = true;
                        writers[currentIndex] = new BufferedWriter(new FileWriter(options.getOutput()
                                .resolve(String.format("%s.csv", matcher)).toString(), options.getCharset()));
                        writers[currentIndex].write(String.format("%s\n", dataHeader));
                        writers[currentIndex].write(String.format("%s\n", line));
                    }
                }
                currentIndex++;
            }
        }
        dataReader.close();
        for (BufferedWriter writer : writers) {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
