package com.lux.generator.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lux.generator.storage.EntitiesPluginsStorage;
import com.lux.generator.util.Connector;
import com.lux.generator.util.FileManager;

public class DataManager {

    private final Logger logger = Logger.getLogger(DataManager.class.getName());

    private final String GIT_LAB_TOK = "gitLab_tok";
    private final String ARTIFACTORY_TOK1="artifactory1_tok";
    private final String ARTIFACTORY_TOK2="artifactory2_tok";
    private final String END_OF_PATH = "features/";
    private final String FILE = "file.sh";
    private final String TAG= "tagName";

    private String path;
    private String gitLab;
    private String artifactory1;
    private String artifactory2;
    private String projects;
    private String command;
    private String tagName;

    public DataManager(String[] args) {
        //for(int i=0;i<args.length;i++) {
          //  args[i]=args[i].replace("&#10;", "\r\n");
       // }
        this.path = args[0].trim();
        this.gitLab = args[1].trim();
        this.artifactory1 = args[2].trim();
        this.artifactory2 = args[3].trim();
        this.projects = args[4].trim();
        this.command = args[5].trim();
        this.tagName = args[6].trim();
        //show(args);
        checkPath();
        EntitiesPluginsStorage.getInstance();
        EntitiesPluginsStorage.setData(new DataCreator().create(projects));
        if(!EntitiesPluginsStorage.getBySearch("0").isEmpty()) {
            new NonInstallablessNumberGetter(path,artifactory1);
        }
        command = setTokensToCommand();
        getConnection();
        EntitiesPluginsStorage.addWay(command);
        File file = new File(FILE);
        try {
            saveFile(file, command);
        } catch (IOException e) {
            logger.log(Level.SEVERE, ErrorMessagesStringFinals.IO_ERROR_MESSAGE, e);
        }

    }

    private void show(String[] args) {
        for(int i=0;i<args.length;i++) {
            System.out.println("element: "+i+" value: "+args[i]);
        }
    }

    private void checkPath() {
        if (path.endsWith("/")) {
            path = path + END_OF_PATH;
        } else {
            path = path + "/" + END_OF_PATH;
        }
    }

    private String setTokensToCommand() {
        command = command.replace(ARTIFACTORY_TOK1, artifactory1);
        command = command.replace(ARTIFACTORY_TOK2, artifactory2);
        command = command.replace(TAG, tagName);
        return command = command.replace(GIT_LAB_TOK, gitLab);
    }

    private void getConnection() {
        Connector.getInstance();
        try {
            addNumbers(Connector.setConnection(path, artifactory1));
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, ErrorMessagesStringFinals.MALFORMED_ERROR_MESSAGE, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, ErrorMessagesStringFinals.IO_ERROR_MESSAGE, e);
        }
    }

    private void addNumbers(InputStream inputData) throws IOException {
        LinkedList<String> searchList = EntitiesPluginsStorage.getSearch();
        NumberParser parser = new NumberParser(searchList);
        WeakHashMap<String, String> result = parser.readFromInStream(inputData);
        EntitiesPluginsStorage.addNumber(result);
    }

    private void saveFile(File file, String commandString) throws IOException {
        CommandParser parser = new CommandParser(EntitiesPluginsStorage.getEntity(), commandString);
        String command = parser.getCommand();
        FileManager.saveExecFile(file, command);
    }
}
