package com.lux.generator.manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lux.generator.model.DataModel;
import com.lux.generator.storage.EntitiesPluginsStorage;
import com.lux.generator.storage.ParseEnum;
import com.lux.generator.util.Connector;

public class NonInstallablessNumberGetter {

    private static final String TIMESTAMP_REGULAR_EXPRESSION = "[0-9]{14}";
    private static final String BUILD_NUMBER_REGULAR_EXPRESSION = "[0-9]{14}-[a-fA-F0-9]{10}";
    private static final String END_OF_PAGE = "</body>";
    private static final String INSTALLABLES = "installables";
    private static final String NON_INSTALLABLES_MARKER = "0";

    private final Logger logger = Logger.getLogger(NonInstallablessNumberGetter.class.getName());

    private String path;
    private String number;

    public NonInstallablessNumberGetter(String path, String artifactory1) {
        this.path = path;
        number = getNumberFromPath();
        replaseInstallablessToTemplate();
        HashMap<String, String> paths = replaseTemplateToNonInstallablesRepos();
        getConnection(paths, artifactory1);
    }

    private void getConnection(HashMap<String, String> paths, String artifactory1) {
        Connector.getInstance();
        Iterator<Map.Entry<String, String>> it = paths.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            try {
                InputStream in = Connector.setConnection((String) pair.getValue(), artifactory1);
                addNumbers(in, (String) pair.getKey());
            } catch (MalformedURLException e) {
                logger.log(Level.SEVERE, ErrorMessagesStringFinals.MALFORMED_ERROR_MESSAGE, e);
            } catch (IOException e) {
                logger.log(Level.SEVERE, ErrorMessagesStringFinals.IO_ERROR_MESSAGE, e);
            }
        }
    }

    private void addNumbers(InputStream setConnection, String name) throws IOException {
        char character;
        String temp = "";
        end_of_page: while (true) {
            String searchedString = "";
            do {
                character = (char) setConnection.read();
                searchedString += character + "";
                if (searchedString.contains(END_OF_PAGE)) {
                    break end_of_page;
                }
            } while (character != '\n');
            if (elderThanInstallables(searchedString)) {
                break;
            }
            if (contentsNumber(searchedString)) {
                temp = getNumbersByRegular(searchedString);
            }

        }
        EntitiesPluginsStorage.addNumberByName(name,temp);
        setConnection.close();
    }
    
    private boolean contentsNumber(String searchedString) {
        Pattern pattern = Pattern.compile(TIMESTAMP_REGULAR_EXPRESSION);
        Matcher matcher1 = pattern.matcher(searchedString);
        if (matcher1.find()) {
            return true;
        }
        return false;
    }
    
    private boolean elderThanInstallables(String searchedString) {
        Pattern pattern = Pattern.compile(TIMESTAMP_REGULAR_EXPRESSION);
        Matcher matcher1 = pattern.matcher(searchedString);
        Matcher matcher2 = pattern.matcher(number);
        if (matcher1.find()&&matcher2.find()) {
            if (matcher1.group(0).compareTo(matcher2.group(0)) > 0) {
                //System.out.println(matcher1.group(0));
                return true;
            }
        }
        return false;
    }

    private String getNumbersByRegular(String searchedString) {
        Pattern pattern = Pattern.compile(BUILD_NUMBER_REGULAR_EXPRESSION);
        Matcher matcher = pattern.matcher(searchedString);
        if (matcher.find()) {
            //System.out.println(matcher.group(0));
            return matcher.group(0);
        }
        return "";
    }

    private HashMap<String, String> replaseTemplateToNonInstallablesRepos() {
        HashMap<String, String> paths = new HashMap<>();
        ArrayList<DataModel> repos = EntitiesPluginsStorage.getBySearch(NON_INSTALLABLES_MARKER);
        int size = repos.size();
        for (int i = 0; i < size; i++) {
            String repoPath = replaseTemplateToNonInstallablesRepo(repos.get(i));
            paths.put(repos.get(i).getName(), repoPath);
        }
        return paths;
    }

    private String replaseTemplateToNonInstallablesRepo(DataModel dataModel) {
        String repoPath = path;
        repoPath = repoPath.replace(ParseEnum.NAME.getParse(), dataModel.getName());
        repoPath = repoPath.replace(ParseEnum.PROJECT_NAME.getParse(), dataModel.getProjectName());
        ArrayList<String> subsuffixes = dataModel.getSubSuffixes();
        for (int i = 0; i < subsuffixes.size(); i++) {
            repoPath = repoPath.replace(ParseEnum.SUFFIX.getParse() + "" + i, subsuffixes.get(i));
        }
        return repoPath;
    }

    private void replaseInstallablessToTemplate() {
        DataModel installables = EntitiesPluginsStorage.getByName(INSTALLABLES);
        path = path.replace(installables.getName(), ParseEnum.NAME.getParse());
        path = path.replace(installables.getProjectName(), ParseEnum.PROJECT_NAME.getParse());
        ArrayList<String> subsuffixes = installables.getSubSuffixes();
        for (int i = 0; i < subsuffixes.size(); i++) {
            path = path.replace(subsuffixes.get(i), ParseEnum.SUFFIX.getParse() + "" + i);
        }
    }

    private String getNumberFromPath() {
        Pattern pattern = Pattern.compile(BUILD_NUMBER_REGULAR_EXPRESSION);
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            String temp[] = path.split(matcher.group(0));
            path = temp[0];
            return matcher.group(0);
        }
        return "";
    }
}
