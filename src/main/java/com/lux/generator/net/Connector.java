package com.lux.generator.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class Connector {
    
    private static Connector instance;

    private Connector() {

    }

    public static Connector getInstance() {
        if (instance == null) {
            instance = new Connector();
        }
        return instance;
    }

    public static InputStream setConnection(String url, String user, String password)
            throws MalformedURLException, IOException {
        URL server = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) server.openConnection();
        String userpass = user + ":" + password;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
        connection.setRequestProperty("Authorization", basicAuth);
        connection.setConnectTimeout(50000);
        connection.connect();
        InputStream in = connection.getInputStream();
        return in;
    }
    
}



