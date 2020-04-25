package com.hyjf.batch.statistics.operationresport;

public class AgeByRangeDesc {
	public String name;
	public String value;

	public String color;

	public AgeByRangeDesc(String name, String value, String color) {
		this.name = name;
		this.value = value;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	
}
