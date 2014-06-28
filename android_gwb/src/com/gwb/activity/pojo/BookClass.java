package com.gwb.activity.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class BookClass implements Serializable {

	private int classId;
	private String className;
//	private String categoryName;
	private int categoryId;

	public BookClass() {
		// TODO Auto-generated constructor stub
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

//	public String getCategoryName() {
//		return categoryName;
//	}
//
//	public void setCategoryName(String categoryName) {
//		this.categoryName = categoryName;
//	}

	@Override
	public String toString() {
		return "BookClass [classId=" + classId + ", className=" + className
				+ ", categoryId=" + categoryId + "]";
	}

}
