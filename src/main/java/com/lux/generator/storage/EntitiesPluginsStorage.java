package com.lux.generator.storage;

import java.util.ArrayList;
import java.util.Arrays; 
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lux.generator.model.DataModel;

public class EntitiesPluginsStorage {

    private static EntitiesPluginsStorage instance;
    private static ArrayList<DataModel> entity;

    private EntitiesPluginsStorage() {
        entity = new ArrayList<DataModel>();
    }

    public static EntitiesPluginsStorage getInstance() {
        if (instance == null) {
            instance = new EntitiesPluginsStorage();
        }
        return instance;
    }

    public static ArrayList<DataModel> getJsonEntity() {
        return entity;
    }

    public static void setJsonEntity(ArrayList<DataModel> jsonEntity) {
        EntitiesPluginsStorage.entity = jsonEntity;
    }

    public static void addJsonEntity(DataModel jsonItem) {
        entity.add(jsonItem);
    }

    public static void deleteJSONEntity(DataModel[] selection) {
        List<DataModel> list = Arrays.asList(selection);
        entity.removeAll(list);
    }

    public static void addNumber(WeakHashMap<String, String> result) {
        int size = entity.size();
        for (int i = 0; i < size; i++) {
            int result_size = result.size();
            for (int j = 0; j < result_size; j++) {
                String search = entity.get(i).getSearch();
                if (result.get(search) != null) {
                    entity.get(i).setNumber(result.get(search));
                    result.remove(search);
                }
            }
        }
    }

    public static void updateJsonEntity(DataModel jsonItem, String name, String search, String project_name,
            String suffix, String order_priority, String number, String way) {
        String uuid = jsonItem.getUuid();
        int size = entity.size();
        for (int i = 0; i < size; i++) {
            if (uuid.equals(entity.get(i).getUuid())) {
                entity.set(i,
                        new DataModel(name, search, project_name, suffix));
                break;
            }
        }
    }

    public static void addWay(String way) {
        int size = entity.size();
        for (int i = 0; i < size; i++) {
            String parsedWay;
            DataModel model = entity.get(i);
            parsedWay = way.replaceAll(ParseEnum.NAME.getParse(), entity.get(i).getName());
            parsedWay = parsedWay.replaceAll(ParseEnum.PROJECT_NAME.getParse(), model.getProjectName());
            String number = model.getNumber();
            parsedWay = parsedWay.replaceAll(ParseEnum.NUMBER.getParse(), number);
            parsedWay = parsedWay.replaceAll(ParseEnum.COMMIT.getParse(),
                    number.substring(number.lastIndexOf("-") + 1));
            parsedWay = parseSuffix(model, parsedWay);
            entity.get(i).setWay(parsedWay);
        }
    }

    private static String parseSuffix(DataModel model, String parsedWay) {
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
        for (DataModel entity : entity) {
            way += entity.getWay() + lineSeparator;
        }
        return way;
    }

    public static LinkedList<String> getSearch() {
        LinkedList<String> searchItem = new LinkedList<String>();
        for (DataModel entity : entity) {
            searchItem.add(entity.getSearch());
        }
        return searchItem;
    }

    public static void clean() {
        entity.clear();
    }

    public static void setData(DataModel[] dest) {
        List<DataModel> list = Arrays.asList(dest);
        clean();
        entity.addAll(list);
    }
    
    public static void setData(ArrayList<DataModel> dest) {
        List<DataModel> list =dest;
        clean();
        entity.addAll(list);
    }

}