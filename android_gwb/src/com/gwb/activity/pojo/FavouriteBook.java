package com.gwb.activity.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class FavouriteBook implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int favouriteId;
	private int userId;
	private int bookId;
	private String userName;
	private String bookName;
	private String bookUrl;
	private Date createTs;

	public FavouriteBook() {
		// TODO Auto-generated constructor stub
	}

	public FavouriteBook(int favoriteId, int userId, int bookId,
			String userName, String bookName, Timestamp createTs) {
		super();
		this.favouriteId = favoriteId;
		this.userId = userId;
		this.bookId = bookId;
		this.userName = userName;
		this.bookName = bookName;
		this.createTs = createTs;
	}

	public int getFavouriteId() {
		return favouriteId;
	}

	public void setFavouriteId(int favouriteId) {
		this.favouriteId = favouriteId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public String getBookUrl() {
		return bookUrl;
	}

	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}

	@Override
	public String toString() {
		return "FavoriteBook [favoriteId=" + favouriteId + ", userId=" + userId
				+ ", bookId=" + bookId + ", userName=" + userName
				+ ", bookName=" + bookName + ", createTs=" + createTs + "]";
	}

}
