package com.lux.generator.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.WeakHashMap;

import com.lux.generator.model.DataModel;
import com.lux.generator.storage.EntitiesPluginsStorage;
import com.lux.generator.util.Connector;
import com.lux.generator.util.FileManager;

public class DataManager {

	private final String GIT_LAB_TOK= "gitLab_tok";
	private final String FILE="file.bat";
	private String path;
	private String gitLab;
	private String artifactory;
	private String projects;
	private String command;

	public DataManager(String[] args) {
		this.path = args[0].trim();
		this.gitLab = args[1].trim();
		this.artifactory = args[2].trim();
		this.projects = args[3].trim();
		this.command = args[4].trim();
		EntitiesPluginsStorage.getInstance();
		EntitiesPluginsStorage.setData(new DataCreator().create(projects));
		command=setGitLabToCommand();
		getConnection();
		EntitiesPluginsStorage.addWay(command);
		File file= new File(FILE);
		try {
			saveBatFile(file, command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String setGitLabToCommand() {
		return command.replace(GIT_LAB_TOK, gitLab);
		
	}

	private void getConnection() {
		Connector.getInstance();
		try {
			addNumbers(Connector.setConnection(path, artifactory));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addNumbers(InputStream inputData) throws IOException {
		LinkedList<String> searchList = EntitiesPluginsStorage.getSearch();
		NumberParser parser = new NumberParser(searchList);
		WeakHashMap<String, String> result = parser.readFromInStream(inputData);
		EntitiesPluginsStorage.addNumber(result);
	}

	private void saveBatFile(File file, String commandString) throws IOException {
		CommandParser parser = new CommandParser(EntitiesPluginsStorage.getJsonEntity(), commandString);
		String command = parser.getCommand();
		FileManager.saveBat(file, command);
	}
}
