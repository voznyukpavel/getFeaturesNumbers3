package com.lux.generator.settings.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import com.lux.generator.listeners.SettingsListener;
import com.lux.generator.settings.model.SettingsModel;
import com.lux.generator.settings.util.SettingsFileManager;

public class SettingGetter {
	private ArrayList<SettingsListener> observers;
	private SettingsModel model;
	private String address;

	public SettingGetter() {
		observers = new ArrayList<SettingsListener>();
	}

	public void connection(String address) throws MalformedURLException, IOException {
		this.address=address;
		notifyObserversLoaded();
	}

	private void notifyObserversLoaded() throws MalformedURLException, IOException {
		int size = observers.size();
		for (int i = 0; i < size; i++) {
			SettingsListener observer = (SettingsListener) observers.get(i);
			observer.getSettings(address);
		}
	}

	public void registerObserver(SettingsListener observer) {
		observers.add(observer);
	}

	public void saveSettings(String address, String user, String password, String proxyHost,
			String proxyPort, String proxyUser, String proxyPassword, boolean proxy) throws IOException {
		
		model = new SettingsModel(user, password, proxyHost, proxyPort, proxyUser, proxyPassword, proxy);
		SettingsFileManager.saveSettings(address, model);
	}

	public void loadSettings(String address) throws IOException, ParseException {
		model = SettingsFileManager.loadSettings(address);
	}

	public String getLogin() {
		return model.getLogin();
	}

	public String getPassword() {
		return model.getPassword();
	}

	public String getProxyHost() {
		return model.getProxyHost();
	}

	public String getProxyPassword() {
		return model.getProxyPassword();
	}

	public String getProxyPort() {
		return model.getProxyPort();
	}

	public String getProxyUser() {
		return model.getProxyUser();
	}

	public boolean isProxy() {
		return model.isProxy();
	}
}
