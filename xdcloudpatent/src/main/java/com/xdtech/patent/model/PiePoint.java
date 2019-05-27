package com.xdtech.patent.model;

/**
 * 饼图上的数据点
 * 
 * @author coolBoy
 *
 */
public class PiePoint {
	private String name;
	private int y;
	private String color;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getY() {
		return y;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setY(int y) {
		this.y = y;
	}

	public PiePoint() {

	}

	public PiePoint(String name, long y) {
		super();
		this.name = name;
		this.y = (int) y;
	}

	public PiePoint(String name, long y, String color) {
		super();
		this.name = name;
		this.y = (int) y;
		this.color = color;
	}

}
