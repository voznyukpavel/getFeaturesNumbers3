package com.lux.generator.app;

import com.lux.generator.manager.DataManager;

public class App {
    public static void main(String[] args) {
    	//args[0]->path
    	//args[1]->Gitlab token
    	//args[2]->Artifactory token
      //args[3]->Artifactory token
    	//args[4]->projects list
    	//args[5]->command
      //args[6]->tag
    	new DataManager(args);
    }
}
