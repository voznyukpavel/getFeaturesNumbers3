package com.lux.generator.settings.model;

public class SettingsModel {
	private String login;
	private String password;
	private String proxyHost;
	private String proxyPort;
	private String proxyUser;
	private String proxyPassword;
	private boolean isProxy;

	
	public SettingsModel() {
	    this.login = "";
	    this.password = "";
	    this.proxyHost ="";
	    this.proxyPort = "";
	    this.proxyUser = "";
	    this.proxyPassword = "";
	    this.isProxy = false;
    }

    public SettingsModel(String login, String password, String proxyHost, String proxyPort,
			String proxyUser, String proxy_password, boolean isProxy) {
		this.login = login;
		this.password = password;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.proxyUser = proxyUser;
		this.proxyPassword = proxy_password;
		this.isProxy = isProxy;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public boolean isProxy() {
		return isProxy;
	}
}
