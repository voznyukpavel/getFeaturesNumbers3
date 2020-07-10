package com.lux.generator.manager;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.lux.generator.model.DataModel;

public class DataCreator implements IModelFactory<ArrayList<DataModel> >{

	@Override
	public ArrayList<DataModel> create(String data) {
		ArrayList<DataModel> projects = new ArrayList<>();
		ModelFactory mf= new ModelFactory();
		StringTokenizer st = new StringTokenizer(data, System.lineSeparator());
		while (st.hasMoreTokens()) {
			projects.add(mf.create(st.nextToken()));
		}
		return projects;
	}
}
