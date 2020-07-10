package com.lux.generator.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.WeakHashMap;

import org.json.simple.parser.ParseException;

import com.lux.generator.events.ConnectionEvent;
import com.lux.generator.listeners.DataPanelListener;
import com.lux.generator.model.JSONArtifactModel;
import com.lux.generator.storage.JSONEntitiesPluginsStorage;
import com.lux.generator.util.FileManager;

public class DataManager {

    private static ArrayList<DataPanelListener> observers;
    private InputStream inputData;

    public DataManager() {
        observers = new ArrayList<DataPanelListener>();
        JSONEntitiesPluginsStorage.getInstance();
    }

    public void addNumbers(InputStream inputData) throws IOException, ParseException {
        LinkedList<String> searchList = JSONEntitiesPluginsStorage.getSearch();
        NumberParser parser = new NumberParser(searchList);
        WeakHashMap<String, String> result = parser.readFromInStream(inputData);
        JSONEntitiesPluginsStorage.addNumber(result);
        notifyObservers();
    }

    public void createWay(String way) {
        JSONEntitiesPluginsStorage.addWay(way);
        notifyObservers();
    }

    public void addItem(String name, String search, String project_name, String suffix, String order_priority,
            String number, String way) {
        JSONArtifactModel jsonItem = new JSONArtifactModel(name, search, project_name, suffix, order_priority, number,
                way);
        JSONEntitiesPluginsStorage.addJsonEntity(jsonItem);
        notifyObservers();
    }

    public ArrayList<JSONArtifactModel> getData() {
        return JSONEntitiesPluginsStorage.getJsonEntity();
    }

    private void notifyObservers() {
        int size = observers.size();
        for (int i = 0; i < size; i++) {
            DataPanelListener observer = (DataPanelListener) observers.get(i);
            observer.setDataToTable();
        }
    }


    private void notifyObserversSession() {
        int size = observers.size();
        for (int i = 0; i < size; i++) {
            DataPanelListener observer = (DataPanelListener) observers.get(i);
            observer.getInputData(new ConnectionEvent(this, inputData));
        }
    }

    public void registerObserver(DataPanelListener observer) {
        observers.add(observer);
    }

    public void deleteItem(Object[] selection) {
        JSONArtifactModel[] dest = new JSONArtifactModel[selection.length];
        System.arraycopy(selection, 0, dest, 0, selection.length);
        JSONEntitiesPluginsStorage.deleteJSONEntity(dest);
        notifyObservers();
    }

    public void getInputStreamConnection(InputStream inputData) {
        this.inputData = inputData;
        notifyObserversSession();
    }

    public void updateItem(Object selection, String name, String search, String project_name, String suffix,
            String order_priority, String number, String way) {
        JSONEntitiesPluginsStorage.updateJsonEntity((JSONArtifactModel) selection, name, search, project_name, suffix,
                order_priority, number, way);
        notifyObservers();
    }

    public void clean() {
        JSONEntitiesPluginsStorage.clean();
        notifyObservers();
    }

    public void setData(Object[] objects) {
        JSONArtifactModel[] dest = new JSONArtifactModel[objects.length];
        System.arraycopy(objects, 0, dest, 0, objects.length);
        JSONEntitiesPluginsStorage.setData(dest);
    }

    public void saveBatFile(File file, String commandAreaText) throws IOException {
        CommandParser parser = new CommandParser(JSONEntitiesPluginsStorage.getJsonEntity(), commandAreaText);
        String command = parser.getCommand();
        FileManager.saveBat(file, command);
    }
}
