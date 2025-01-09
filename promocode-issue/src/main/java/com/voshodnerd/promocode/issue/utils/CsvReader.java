package com.voshodnerd.promocode.issue.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CsvReader {
    private static final char DEFAULT_CSV_SEPARATOR = ',';

    public static List<String> parseCsv(InputStream csvInput) {
        List<String> arrayList = new ArrayList<>();
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(csvInput, "UTF-8"));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(DEFAULT_CSV_SEPARATOR).withHeader().parse(br);
        ) {
            for (CSVRecord record : parser) {
                arrayList.add(record.get(0));

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        List<Integer> excludeList = new ArrayList<>(List.of(0, 1, 3, arrayList.size() - 1));
        var response = arrayList.toArray();
        // Create an AtomicInteger for index
        return IntStream.range(0, arrayList.toArray().length)
                .filter(i -> !excludeList.contains(i))
                .mapToObj(i -> response[i])
                .map(x -> (String) x)
                .collect(Collectors.toList());
    }
}



