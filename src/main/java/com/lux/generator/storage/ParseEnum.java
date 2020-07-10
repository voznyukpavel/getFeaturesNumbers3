package com.lux.generator.storage;

enum ParseEnum {
    NAME("\"name\""), 
    PROJECT_NAME("\"project_name\""), 
    SUFFIX("\"suffix"),
    SUFFIXREGULAR("\"suffix"+ "[0-9]+" + "\""), 
    NUMBER("\"number\""), 
    COMMIT("\"commit\"");

    private final String parse;

    ParseEnum(String parse) {
        this.parse = parse;
    }

    String getParse() {
        return parse;
    }
}
