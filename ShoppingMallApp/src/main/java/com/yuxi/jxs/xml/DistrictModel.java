package com.yuxi.jxs.xml;

public class DistrictModel {
	private String name;
	private String id;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String zipcode) {
		super();
		this.name = name;
		this.id = zipcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String zipcode) {
		this.id = zipcode;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", id=" + id + "]";
	}

}
