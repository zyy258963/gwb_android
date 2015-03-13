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

	// 20. 193.34.134 10 .0.2.2 20 .193.34.133 117. 79.84.185    www.weebo .com.cn

	public static final String URL_BATH = "http://www.weebo.com.cn/GwbProject";
	public static final String URL_BATH_SLASH = "http://www.weebo.com.cn/GwbProject/";

	public static final String URL_LOGIN = "http://www.weebo.com.cn/GwbProject/AndroidLoginAction";
	public static final String URL_DOWNLOAD = "http://www.weebo.com.cn/GwbProject/update/MainActivity.apk";
	
	public static final String URL_UPDATE_SERVER = "http://www.weebo.com.cn/gwb/update/";
	public static final String URL_UPDATE_APKNAME = "MainActivity.apk";
	public static final String URL_UPDATE_VERJSON = "ver.json";
	public static final String URL_UPDATE_SAVENAME = "MainActivity.apk";

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
	// ��峰�����������琛�涓�������琛�

	public static final String URL_GET_CATEGORYS = "http://www.weebo.com.cn/GwbProject/AndroidAction?type=listCategory";
	public static final String URL_GET_CLASS = "http://www.weebo.com.cn/GwbProject/AndroidAction?type=listClass";
	public static final String URL_GET_BOOKS = "http://www.weebo.com.cn/GwbProject/AndroidAction?type=listBook";
	public static final String URL_SEARCH_BOOKS = "http://www.weebo.com.cn/GwbProject/AndroidAction?type=searchBook";
	public static final String URL_LIST_FAVOURITE_BOOKS = "http://www.weebo.com.cn/GwbProject/AndroidAction?type=listFavourite";
	public static final String URL_ADD_FAVOURITE_BOOKS = "http://www.weebo.com.cn/GwbProject/AndroidAction?type=addFavourite";
	public static final String URL_DELETE_FAVOURITE_BOOKS = "http://www.weebo.com.cn/GwbProject/AndroidAction?type=deleteFavourite";
	public static final String URL_LOG_OPEN_DOC = "http://www.weebo.com.cn/GwbProject/AndroidAction?type=log";
	public static final String URL_PDF_DOWN = "";

	// public static String URL_DOWN_PDF_BASE=
	// "http://www.weebo.com.cn/FileUpload/upload/books/18-16/2014-05-04-09-30-37.pdf";
	// public static String URL_DOWN_PDF_BASE=
	// "http://www.weebo.com.cn/FileUpload/upload/";
	public static final String URL_DOWN_PDF_BASE = "http://www.weebo.com.cn/FileUpload/upload/books/";

	// 涓存�舵��浠剁��瀛���句��缃�
	public static final String FILE_STORE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/.gwb/books";
	public static final String TEMP_FILE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/temp.pdf";
	public static File TEMP_FILE = new File(TEMP_FILE_PATH);

	// public static String URL_TEMP_PDF=
	// "http://www.weebo.com.cn/GwbProject/upload/android1.pdf";

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

	public static int SIZE_ROW = 0;
	public static int SIZE_BUTTON_WIDTH = 0;
	public static float SIZE_TOP_TEXT = 0;
	public static float SIZE_MAIN_TEXT = 0;
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;

	public static String FIELD_CATEGORY_LIST = "categoryList";
	public static String FIELD_FAVOURITE_BOOK_SET = "favouriteset";
	
	public static int NUM_BOOK_SAVE_MAX = 1;

	public static String PACKAGE_NAME = "com.artifex.mupdfdemo";
	public static boolean HAS_CHECK_UPDATE = false;
	
	public static int MAX_STORE_BOOK = 1;
	
}
