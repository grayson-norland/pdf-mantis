package com.graysonnorland.pdfmantis.index.algo;

import com.graysonnorland.pdfmantis.index.object.TextIndex;
import com.graysonnorland.pdfmantis.exception.PdfMantisException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextIndexBuilder {

    public List<TextIndex> findTextIndex(List<TextIndex> index, String string) throws PdfMantisException {
        List<TextIndex> results = new ArrayList<>();
        if (!string.contains(" ")) {
            for (TextIndex entry : index) {
                if (entry.getWord().toLowerCase().startsWith(string.toLowerCase())) {
                    results.add(entry);
                }
            }
        } else {
            results = buildMultiWordIndex(index, string);
        }
        if (results.isEmpty()) {
            throw new PdfMantisException("Cannot find any occurrence of String '" + string + "'");
        }
        return results;
    }

    private List<TextIndex> buildMultiWordIndex(List<TextIndex> index, String string) {

        List<String> splitString = buildSplitString(string);
        List<TextIndex> tempIndex = new ArrayList<>();
        List<TextIndex> returnIndex = new ArrayList<>();
        boolean lastWord = false;

        for (Integer page : index.stream()
                .mapToInt(TextIndex::getPageNumber)
                .distinct()
                .toArray()) {

            List<TextIndex> currentPage = index.stream()
                    .filter(i -> i.getPageNumber() == page)
                    .collect(Collectors.toList());

            for (TextIndex entry : currentPage) {

                if (splitString.isEmpty()) {
                    splitString = buildSplitString(string);
                    returnIndex.add(new TextIndexSorter().determineIndex(tempIndex, string));
                    tempIndex.clear();
                    lastWord = false;
                }

                if (splitString.size() == 1) {
                    lastWord = true;
                }

                if (lastWord) {
                    if (entry.getWord().toLowerCase().startsWith(splitString.get(0))) {
                        tempIndex.add(entry);
                        splitString.remove(0);
                    } else {
                        splitString = buildSplitString(string);
                        tempIndex.clear();
                        lastWord = false;
                    }

                } else {
                    if (entry.getWord().equalsIgnoreCase(splitString.get(0))) {
                        tempIndex.add(entry);
                        splitString.remove(0);
                    } else {
                        splitString = buildSplitString(string);
                        tempIndex.clear();
                        lastWord = false;
                    }
                }
            }
        }
        return returnIndex;
    }

    private List<String> buildSplitString(String string) {
        return Arrays.stream(string.split(" "))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
