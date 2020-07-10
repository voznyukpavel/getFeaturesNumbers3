package com.lux.generator.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class NumberParser {
    private static final String FILE_EXTENSION = ".jar";
    private static final String END_OF_PAGE = "</body>";
    private static final String EXPRESSION = "[^-]" + "[_a-zA-Z0-9.-]*" + Pattern.quote(FILE_EXTENSION);
    private static final int MIN_LENGTH=2;

    private LinkedList<String> search;
    private WeakHashMap<String, String> result;

    NumberParser(LinkedList<String> search) {
        this.search = search;
        result = new WeakHashMap<String, String>();
    }

    WeakHashMap<String, String> readFromInStream(InputStream in) throws IOException {
        char character;
        end_of_page: 
            while (true) {
            String searchedString = "";
            do {
                character = (char) in.read();
                searchedString += character + "";
                if (searchedString.contains(END_OF_PAGE)) {
                    break end_of_page;
                }
            } while (character != '\n');
            getMatches(searchedString);
        }
        in.close();
        return result;
    }

    private void getMatches(String text) {
        for (int i = 0; i < search.size(); i++) {
            String searchedString = search.get(i).trim();
            if (searchedString.length() > MIN_LENGTH) {
                Pattern pattern = Pattern.compile(Pattern.quote(searchedString) + EXPRESSION);
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    String[] parts = text.split(FILE_EXTENSION);
                    String numb = parts[0].substring(parts[0].lastIndexOf(".") + 1);
                    result.put(searchedString, numb);
                    search.remove(i);
                }
            }
        }
    }
}
