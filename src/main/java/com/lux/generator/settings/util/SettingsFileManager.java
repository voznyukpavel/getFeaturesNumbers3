package com.lux.generator.settings.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.lux.generator.settings.model.SettingsModel;

public class SettingsFileManager {

    public static SettingsModel loadSettings(String address) throws IOException, ParseException {
        File settingsFile = new File(address);
        if (settingsFile.exists()) {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(settingsFile);
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObj = (JSONObject) obj;
            String proxyString = (String) jsonObj.get(SettingsConstants.PROXY);
            SettingsModel model = new SettingsModel(
                    (String) jsonObj.get(SettingsConstants.USER), (String) jsonObj.get(SettingsConstants.PASSWORD),
                    (String) jsonObj.get(SettingsConstants.PROXY_HOST),
                    (String) jsonObj.get(SettingsConstants.PROXY_PORT),
                    (String) jsonObj.get(SettingsConstants.PROXY_USER),
                    (String) jsonObj.get(SettingsConstants.PROXY_PASSWORD), Boolean.parseBoolean(proxyString));
            reader.close();
            return model;
        } else {
            return new SettingsModel();
        }
    }

    @SuppressWarnings("unchecked")
    public static void saveSettings(String address, SettingsModel model) throws IOException {
        File settingsFile = new File(address);
        if (!settingsFile.exists()) {
            settingsFile.createNewFile();
        }
        JSONObject settings = new JSONObject();
        settings.put(SettingsConstants.USER, model.getLogin());
        settings.put(SettingsConstants.PASSWORD, model.getPassword());
        settings.put(SettingsConstants.PROXY, Boolean.toString(model.isProxy()));
        settings.put(SettingsConstants.PROXY_HOST, model.getProxyHost());
        settings.put(SettingsConstants.PROXY_PORT, model.getProxyPort());
        settings.put(SettingsConstants.PROXY_USER, model.getProxyUser());
        settings.put(SettingsConstants.PROXY_PASSWORD, model.getProxyPassword());
        FileWriter writer = new FileWriter(settingsFile);
        writer.write(settings.toJSONString());
        writer.flush();
        writer.close();
    }
}
