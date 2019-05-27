package com.xdtech.patent.model;

import java.util.List;

public class NamedData {
	
	public String name;
	
	public List data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	public NamedData(String name, List data) {
		super();
		this.name = name;
		this.data = data;
	}
}
