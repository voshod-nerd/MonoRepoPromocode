package com.voshodnerd.promocode.issue.utils;

import org.apache.commons.csv.CSVFormat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

class CsvReader {

    companion object {
        const val DEFAULT_CSV_SEPARATOR = ',';
        fun parseCsv(csvInput: InputStream): List<String> {
            val arrayList = mutableListOf<String>()

            try {
                BufferedReader(InputStreamReader(csvInput, "UTF-8")).use { br ->
                    val parser = CSVFormat.Builder.create()
                        .setDelimiter(DEFAULT_CSV_SEPARATOR).build().parse(br)
                    for (record in parser) {
                        arrayList.add(record[0])
                    }
                }
            } catch (e: Exception) {
                println(e)
            }

            val excludeList = listOf(0, 1, 3, arrayList.size - 1)
            val response = arrayList.toTypedArray()

            return response.indices
                .filter { !excludeList.contains(it) }
                .map { response[it] }
                .toList()
        }
    }
}



