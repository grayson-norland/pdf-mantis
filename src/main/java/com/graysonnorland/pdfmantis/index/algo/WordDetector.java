package com.graysonnorland.pdfmantis.index.algo;

import com.graysonnorland.pdfmantis.index.object.TextIndex;

import java.util.ArrayList;
import java.util.List;

public class WordDetector {

    public List<TextIndex> buildWordIndex(List<TextIndex> index) {

        List<TextIndex> wordsIndex = new ArrayList<>();
        TextIndex lastIndex = new TextIndex();
        String lastWord = null;

        for (TextIndex entry : index) {
            if (lastWord == null) {
                lastWord = entry.getWord();
                lastIndex = entry;
                continue;
            } else {
                if (!entry.getWord().startsWith(lastWord)) {
                    wordsIndex.add(lastIndex);
                }
            }
            lastWord = entry.getWord();
            lastIndex = entry;
        }
        wordsIndex.add(lastIndex);
        return wordsIndex;
    }
}
