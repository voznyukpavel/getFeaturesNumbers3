package com.lux.generator.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lux.generator.model.JSONArtifactModel;

public class JSONEntitiesPluginsStorage {

    private static JSONEntitiesPluginsStorage instance;

    private static ArrayList<JSONArtifactModel> jsonEntity;

    private JSONEntitiesPluginsStorage() {
        jsonEntity = new ArrayList<JSONArtifactModel>();
    }

    public static JSONEntitiesPluginsStorage getInstance() {
        if (instance == null) {
            instance = new JSONEntitiesPluginsStorage();
        }
        return instance;
    }

    public static ArrayList<JSONArtifactModel> getJsonEntity() {
        return jsonEntity;
    }

    public static void setJsonEntity(ArrayList<JSONArtifactModel> jsonEntity) {
        JSONEntitiesPluginsStorage.jsonEntity = jsonEntity;
    }

    public static void addJsonEntity(JSONArtifactModel jsonItem) {
        jsonEntity.add(jsonItem);
    }

    public static void deleteJSONEntity(JSONArtifactModel[] selection) {
        List<JSONArtifactModel> list = Arrays.asList(selection);
        jsonEntity.removeAll(list);
    }

    public static void addNumber(WeakHashMap<String, String> result) {
        int size = jsonEntity.size();
        for (int i = 0; i < size; i++) {
            int result_size = result.size();
            for (int j = 0; j < result_size; j++) {
                String search = jsonEntity.get(i).getSearch();
                if (result.get(search) != null) {
                    jsonEntity.get(i).setNumber(result.get(search));
                    result.remove(search);
                }
            }
        }
    }

    public static void updateJsonEntity(JSONArtifactModel jsonItem, String name, String search, String project_name,
            String suffix, String order_priority, String number, String way) {
        String uuid = jsonItem.getUuid();
        int size = jsonEntity.size();
        for (int i = 0; i < size; i++) {
            if (uuid.equals(jsonEntity.get(i).getUuid())) {
                jsonEntity.set(i,
                        new JSONArtifactModel(name, search, project_name, suffix, order_priority, number, way));
                break;
            }
        }
    }

    public static void addWay(String way) {
        int size = jsonEntity.size();
        for (int i = 0; i < size; i++) {
            String parsedWay;
            JSONArtifactModel model = jsonEntity.get(i);
            parsedWay = way.replaceAll(ParseEnum.NAME.getParse(), jsonEntity.get(i).getName());
            parsedWay = parsedWay.replaceAll(ParseEnum.PROJECT_NAME.getParse(), model.getProjectName());
            String number = model.getNumber();
            parsedWay = parsedWay.replaceAll(ParseEnum.NUMBER.getParse(), number);
            parsedWay = parsedWay.replaceAll(ParseEnum.COMMIT.getParse(),
                    number.substring(number.lastIndexOf("-") + 1));
            parsedWay = parseSuffix(model, parsedWay);
            jsonEntity.get(i).setWay(parsedWay);
        }
    }

    private static String parseSuffix(JSONArtifactModel model, String parsedWay) {
        Pattern pat = Pattern.compile(ParseEnum.SUFFIXREGULAR.getParse());
        Matcher mat = pat.matcher(parsedWay);
        String result = "";
        while (mat.find()) {
            int index = parsedWay.indexOf(mat.group()) + (ParseEnum.SUFFIX.getParse().length());
            int endIndex = index;
            while (parsedWay.charAt(endIndex) != '\"') {
                endIndex++;
            }
            int num = (Integer.parseInt(parsedWay.substring(index, endIndex)));
            result += parsedWay.replace(parsedWay.substring(parsedWay.indexOf(mat.group())), model.getSubSuffix(num));
            parsedWay = parsedWay.substring(endIndex + 1);
        }
        return result += parsedWay;
    }

    public static String getWays() {
        String way = "";
        String lineSeparator = System.lineSeparator();
        for (JSONArtifactModel entity : jsonEntity) {
            way += entity.getWay() + lineSeparator;
        }
        return way;
    }

    public static LinkedList<String> getSearch() {
        LinkedList<String> searchItem = new LinkedList<String>();
        for (JSONArtifactModel entity : jsonEntity) {
            searchItem.add(entity.getSearch());
        }
        return searchItem;
    }

    public static void clean() {
        jsonEntity.clear();
    }

    public static void setData(JSONArtifactModel[] dest) {
        List<JSONArtifactModel> list = Arrays.asList(dest);
        jsonEntity.clear();
        jsonEntity.addAll(list);
    }

}