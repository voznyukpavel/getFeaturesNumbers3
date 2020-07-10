package com.lux.generator.manager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lux.generator.model.JSONArtifactModel;

class CommandParser {

    private static final String EMPTY_STRINGS = "[\r\n]+";
    private static final String SEPARATE = "<<<SEPARATE>>>";
    private static final String SEPARATOR_TEXT_EMPTIES = SEPARATE + ".*" + "[\r\n]*";

    private final ArrayList<JSONArtifactModel> jsonEntity;
    private final String commandAreaText;
    private String lineSeparator = System.lineSeparator();
    private Pattern emptyStringPattern = Pattern.compile(EMPTY_STRINGS);

    public CommandParser(ArrayList<JSONArtifactModel> jsonEntity, String commandAreaText) {
        this.jsonEntity = jsonEntity;
        this.commandAreaText = commandAreaText;
    }

    public String getCommand() {
        ArrayList<String> parts = createCommand(jsonEntity);
        parts = splitCommand(commandAreaText, parts);
        return combineCommand(parts);
    }

    private ArrayList<String> createCommand(ArrayList<JSONArtifactModel> arrayList) {
        ArrayList<String> parts = new ArrayList<String>();
        Pattern splitter = Pattern.compile(SEPARATOR_TEXT_EMPTIES);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            String[] partsTemp = splitter.split(arrayList.get(i).getWay());
            int counter = 0;
            for (int y = 0; y < partsTemp.length; y++) {
                Matcher matcher = emptyStringPattern.matcher(partsTemp[y]);
                if (validation(partsTemp[y], matcher)) {
                    String way = "";
                    if (counter < parts.size()) {
                        way = parts.get(counter);
                        way += partsTemp[y].trim();
                        parts.set(counter, way += lineSeparator);
                    } else {
                        way += partsTemp[y].trim();
                        parts.add(way += lineSeparator);
                    }
                    counter++;
                }
            }
        }
        return parts;
    }

    private ArrayList<String> splitCommand(String commandAreaText, ArrayList<String> parts) {
        String[] commandTemp = emptyStringPattern.split(commandAreaText);
        int counter = 0;
        String way = "";
        boolean valid = false;
        for (int i = 0; i < commandTemp.length; i++) {
            Matcher matcher = emptyStringPattern.matcher(commandTemp[i]);
            if (validation(commandTemp[i], matcher)) {
                if(!commandTemp[0].startsWith(SEPARATE)&&i==0) {
                    counter++;
                }
                if (commandTemp[i].startsWith(SEPARATE)) {
                    way = removeSeparator(commandTemp[i], way);
                    valid = true;
                    if (way.isEmpty()) {
                        continue;
                    }
                    if (!way.endsWith(lineSeparator)) {
                        way += lineSeparator;
                    }
                } else if (valid) {
                    parts.set(counter, way += parts.get(counter));
                    counter++;
                    way = "";
                    valid = false;
                }
            }
        }
        if (counter >= parts.size()) {
            parts.add(way);
        }
        return parts;
    }

    private String combineCommand(ArrayList<String> parts) {
        String command = "";
        for (String part : parts) {
            command += part;
        }
        return command;
    }

    private String removeSeparator(String part, String way) {
        int start = part.indexOf(SEPARATE) + SEPARATE.length();
        way += part.substring(start).trim();
        return way;
    }

    private boolean validation(String part, Matcher matcher) {
        return !(matcher.find() && matcher.group().equals(part)) && !part.isEmpty();
    }
}
