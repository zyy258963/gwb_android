package com.gwb.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.os.Environment;

import com.gwb.activity.pojo.BookChapter;

public class ConstantParams {

	// 20. 193.34.134 10 .0.2.2 20 .193.34.133 117. 79.84.185

	public static final String URL_BATH = "http://192.168.1.2:8080/GwbProject";
	public static final String URL_BATH_SLASH = "http://192.168.1.2:8080/GwbProject/";

	public static final String URL_LOGIN = "http://192.168.1.2:8080/GwbProject/AndroidLoginAction";

	public static int CURRENT_CATEGORY_ID = 0;
	public static int CURRENT_CLASS_ID = 0;
	public static int CURRENT_BOOK_ID = 0;
	public static String CURRENT_CATEGORY_NAME = "";
	public static String CURRENT_CLASS_NAME = "";
	public static String CURRENT_BOOK_NAME = "";
	public static int CURRENT_USER_ID = 0;
	public static String CURRENT_USER_NAME = "";
	public static String CURRENT_PASSWORD = "";
	public static String CURRENT_MACADDRESS = "";
	public static String CURRENT_TELEPHONE = "";
	public static int CURRENT_FAVOURITE_BOOK_SIZE = 0;

	
	public static final String SHARED_PREFERENCE_NAME = "config";
	// 获得所有的行业的列表

	public static final String URL_GET_CATEGORYS = "http://192.168.1.2:8080/GwbProject/AndroidAction?type=listCategory";
	public static final String URL_GET_CLASS = "http://192.168.1.2:8080/GwbProject/AndroidAction?type=listClass";
	public static final String URL_GET_BOOKS = "http://192.168.1.2:8080/GwbProject/AndroidAction?type=listBook";
	public static final String URL_SEARCH_BOOKS = "http://192.168.1.2:8080/GwbProject/AndroidAction?type=searchBook";
	public static final String URL_LIST_FAVOURITE_BOOKS = "http://192.168.1.2:8080/GwbProject/AndroidAction?type=listFavourite";
	public static final String URL_ADD_FAVOURITE_BOOKS = "http://192.168.1.2:8080/GwbProject/AndroidAction?type=addFavourite";
	public static final String URL_DELETE_FAVOURITE_BOOKS = "http://192.168.1.2:8080/GwbProject/AndroidAction?type=deleteFavourite";
	public static final String URL_PDF_DOWN = "";

	// public static String URL_DOWN_PDF_BASE=
	// "http://192.168.1.2:8080/FileUpload/upload/books/18-16/2014-05-04-09-30-37.pdf";
	// public static String URL_DOWN_PDF_BASE=
	// "http://192.168.1.2:8080/FileUpload/upload/";
	public static final String URL_DOWN_PDF_BASE = "http://192.168.1.2:8080/FileUpload/upload/books/";

	// 临时文件的存放位置
	public static final String TEMP_FILE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/temp.pdf";
	public static File TEMP_FILE = new File(TEMP_FILE_PATH);

	// public static String URL_TEMP_PDF=
	// "http://192.168.1.2:8080/GwbProject/upload/android1.pdf";

	public static String FIELD_CHAPTER_LIST = "currentChapterList";
	public static List<BookChapter> CURRENT_CHAPTER_LIST = new ArrayList<BookChapter>();

	// public static final String URL_GET_CATEGORY = "servlet/CategoryAction?"

	public static final String FIELD_CATEGORY_ID = "categoryId";
	public static final String FIELD_CLASS_ID = "classId";
	public static final String FIELD_BOOK_ID = "bookId";
	public static final String FIELD_FAVOURITE_ID = "favouriteId";
	public static final String FIELD_CHAPTER_URL = "chapterUrl";
	public static final String FIELD_KEYWORDS = "keywords";
	public static final String FIELD_USER_ID = "userId";
	public static final String FIELD_MAC_ADDRESS = "macAddress";
	public static final String FIELD_TELEPHONE = "telephone";

	public static final String COLUMN_BOOK_ID = "bookId";
	public static final String COLUMN_BOOK_NAME = "bookName";

	public static int SIZE_MENU_BTN_HEIGHT = 0;
	public static int SIZE_MENU_EDIT_TEXT = 0;
	public static int SIZE_MENU_BTN_TEXT = 0;
	public static int SIZE_MAJOR_BTN_WIDTH = 0;
	public static int SIZE_MAJOR_TEXT_VIEW = 0;
	public static int SIZE_MAJOR_TEXT_VIEW_HEIGHT = 0;

}
