package com.lux.generator.manager;

import java.util.StringTokenizer;

import com.lux.generator.model.DataModel;

public class ModelFactory implements IModelFactory<DataModel> {

	@Override
	public DataModel create(String data) {
		StringTokenizer st = new StringTokenizer(data, " ");
		String name = "";
		String search = "";
		String project = "";
		String suffix = "";
		while (st.hasMoreTokens()) {
			name = st.nextToken();
			search = st.nextToken();
			project = st.nextToken();
			suffix = st.nextToken();
		}
		return new DataModel(name, search, project, suffix);
	}

}
