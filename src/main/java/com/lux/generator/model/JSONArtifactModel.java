package com.lux.generator.model;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.UUID;

public class JSONArtifactModel {

	private final String uuid;
	private String name = "";
	private String search = "";
	private String projectName = "";
	private String suffix = "";
	private String way = "";
	private int orderPriority = 0;
	private String number = "";

	public JSONArtifactModel(String name, String search, String projectName, String suffix, String orderPriority,
			String number, String way) {
		super();
		this.uuid = UUID.randomUUID().toString();
		this.name = name;
		this.search = search;
		this.projectName = projectName;
		this.suffix = suffix;
		this.orderPriority = getIntPriority(orderPriority);
		this.way = way;
		this.number = number;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getSubSuffix(int number) {
		StringTokenizer st = new StringTokenizer(suffix, " ");
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

	private int getIntPriority(String orderPriority) {
		try {
			return Integer.parseInt(orderPriority);
		} catch (NumberFormatException ex) {
			return 0;
		}
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

	public String getOrderPriority() {
		return String.valueOf(orderPriority);
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		return "JSONArtifactModel [uuid=" + uuid + ", name=" + name + ", search=" + search + ", project_name="
				+ projectName + ", suffix=" + suffix + ", way=" + way + ", order_priority=" + orderPriority
				+ ", number=" + number + "]";
	}
}
