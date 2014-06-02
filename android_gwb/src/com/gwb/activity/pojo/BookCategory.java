package com.gwb.activity.pojo;
import java.io.Serializable;

public class BookCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int categoryId;
	private String categoryName;

	public BookCategory(int categoryId, String categoryName) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}

	public BookCategory() {
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "BookCategory [categoryId=" + categoryId + ", categoryName="
				+ categoryName + "]";
	}
	
	

}
