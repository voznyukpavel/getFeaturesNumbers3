package com.lux.generator.model;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.UUID;

public class DataModel {

	private final String uuid;
	private String name = "";
	private String search = "";
	private String projectName = "";
	private String suffix = "";
	private String way = "";
	private String number = "";

	public DataModel(String name, String search, String projectName, String suffix) {
		super();
		this.uuid = UUID.randomUUID().toString();
		this.name = name;
		this.search = search;
		this.projectName = projectName;
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getSubSuffix(int number) {
		StringTokenizer st = new StringTokenizer(suffix, "|");
		String subSuffix = st.nextToken();
		try {
			for (int i = 0; i < number; i++) {
				subSuffix = st.nextToken();
			}
		} catch (NoSuchElementException ex) {
			return "";
		}
		return subSuffix;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public String getSearch() {
		return search;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getWay() {
		return way;
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		return "DataModel [uuid=" + uuid + ", name=" + name + ", search=" + search + ", projectName=" + projectName
				+ ", suffix=" + suffix + ", way=" + way + ", number=" + number + "]";
	}
	
}
