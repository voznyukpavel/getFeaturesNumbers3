package com.lux.generator.listeners;

import java.io.IOException;
import java.net.MalformedURLException;

public interface SettingsListener {
    public void getSettings(String Address) throws MalformedURLException, IOException;
}
