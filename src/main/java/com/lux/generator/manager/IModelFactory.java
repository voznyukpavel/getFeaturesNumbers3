package com.lux.generator.manager;

public interface IModelFactory<T> {
	T create(String data);
}
