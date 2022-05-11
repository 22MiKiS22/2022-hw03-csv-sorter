package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw02.HasLabels;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents every row's labels from csv
 * @author Michael Skor
 */
public class Title implements HasLabels {
    private final Set<String> labels;

    /**
     * Constructor for Title
     * @param labels labels
     */
    public Title(String labels) {
        this.labels = Arrays.stream(labels.split(" ")).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getLabels() {
        return new HashSet<>(labels);
    }
}
