package com.xdtech.patent.model;

import java.util.List;

/**
 * 定义数据抽象
 * 
 * @author coolBoy
 *
 */
public class GraphModel {

	private Object data;

	private List<String> categories;
	
	private List<String> name;

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
}
