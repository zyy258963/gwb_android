package com.gwb.activity.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BookChapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int chapterId;
	private String chapterName;
	private String chapterDesc;
	private int chapterRank;
	private String chapterUrl;
	private int bookId;
	private Timestamp createTs;
	private int isAvailable;

	public BookChapter() {
		// TODO Auto-generated constructor stub
	}

	public BookChapter(int chapterId, String chapterName, String chapterDesc,
			int chapterRank, String chapterUrl, int bookId, Timestamp createTs,
			int isAvailable) {
		super();
		this.chapterId = chapterId;
		this.chapterName = chapterName;
		this.chapterDesc = chapterDesc;
		this.chapterRank = chapterRank;
		this.chapterUrl = chapterUrl;
		this.bookId = bookId;
		this.createTs = createTs;
		this.isAvailable = isAvailable;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getChapterDesc() {
		return chapterDesc;
	}

	public void setChapterDesc(String chapterDesc) {
		this.chapterDesc = chapterDesc;
	}

	public int getChapterRank() {
		return chapterRank;
	}

	public void setChapterRank(int chapterRank) {
		this.chapterRank = chapterRank;
	}

	public String getChapterUrl() {
		return chapterUrl;
	}

	public void setChapterUrl(String chapterUrl) {
		this.chapterUrl = chapterUrl;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public Timestamp getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}

	public int getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(int isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "BookChapter [chapterId=" + chapterId + ", chapterName="
				+ chapterName + ", chapterDesc=" + chapterDesc
				+ ", chapterRank=" + chapterRank + ", chapterUrl=" + chapterUrl
				+ ", bookId=" + bookId + ", createTs=" + createTs
				+ ", isAvailable=" + isAvailable + "]";
	}

	

}
