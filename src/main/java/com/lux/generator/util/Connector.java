package com.lux.generator.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connector {

	private static Connector instance;
    private static final String ARTIFACTORY_API_HEADER="X-JFrog-Art-Api";
    
	private Connector() {

	}

	public static Connector getInstance() {
		if (instance == null) {
			instance = new Connector();
		}
		return instance;
	}

	public static InputStream setConnection(String url, String password)
			throws MalformedURLException, IOException {
		URL server = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) server.openConnection();
		connection.setRequestProperty(ARTIFACTORY_API_HEADER, password);
		connection.connect();
		InputStream in = connection.getInputStream();
		return in;
	}
}
