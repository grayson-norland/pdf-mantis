package com.graysonnorland.pdfmantis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StreamReader {

    public Map<String, String> getStringMapFromStream(InputStream inputStream) throws IOException {

        Map<String, String> mapFileContents = new LinkedHashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            String[] parts = line.split("\\|");
            String key = parts[0].trim();
            String value = parts[1].trim();
            mapFileContents.put(key, value);
        }

        bufferedReader.close();
        return mapFileContents;
    }

    public List<String> getStringListFromStream(InputStream inputStream) throws IOException {

        List<String> listFileContents = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            listFileContents.add(line);
        }

        bufferedReader.close();
        return listFileContents;
    }
}
