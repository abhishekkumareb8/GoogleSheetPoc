package com.ty.Poc.Dao;

import java.util.List;

public class Dto {

	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List<List<String>> getDataToBeUpdated() {
		return dataToBeUpdated;
	}
	public void setDataToBeUpdated(List<List<String>> dataToBeUpdated) {
		this.dataToBeUpdated = dataToBeUpdated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	private String name;
	private String address;
	private int age;
	private String sheetName;
	private List<List<String>> dataToBeUpdated;

}
