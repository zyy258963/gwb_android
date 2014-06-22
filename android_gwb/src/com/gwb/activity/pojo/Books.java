package com.gwb.activity.pojo;
import java.io.Serializable;
import java.sql.Timestamp;

public class Books implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int bookId;
	private String bookName;
	private String bookDesc;
	private int categoryId;
	private String categoryName;
	private int classId;
	private String className;
	private int chapterNum;
	private String bookUrl;

	public Books() {
		// TODO Auto-generated constructor stub
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookDesc() {
		return bookDesc;
	}

	public void setBookDesc(String bookDesc) {
		this.bookDesc = bookDesc;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public int getChapterNum() {
		return chapterNum;
	}

	public void setChapterNum(int chapterNum) {
		this.chapterNum = chapterNum;
	}

	public String getBookUrl() {
		return bookUrl;
	}

	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}

	@Override
	public String toString() {
		return "Books [bookId=" + bookId + ", bookName=" + bookName
				+ ", bookDesc=" + bookDesc + ", categoryId=" + categoryId
				+ ", classId=" + classId + ", chapterNum=" + chapterNum + "]";
	}

}
