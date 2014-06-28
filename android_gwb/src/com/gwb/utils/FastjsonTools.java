package com.gwb.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.preference.PreferenceActivity.Header;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gwb.activity.pojo.BookClass;
import com.gwb.activity.pojo.Books;
import com.gwb.activity.pojo.FavouriteBook;
import com.gwb.activity.pojo.HeaderVo;
import com.gwb.activity.pojo.Users;

public class FastjsonTools {

	public FastjsonTools() {
		// TODO Auto-generated constructor stub
	}

	public static <T> T getPojo(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = JSON.parseObject(jsonString, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public static <T> List<T> getListPojos(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			list = JSON.parseArray(jsonString, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<String> getListString(String jsonString) {
		List<String> list = new ArrayList<String>();
		try {
			list = JSON.parseArray(jsonString, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<Map<String, Object>> getListMap(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = JSON.parseObject(jsonString,
					new TypeReference<List<Map<String, Object>>>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static Map<String, Object> getMap(String jsonString) {
		Map<String, Object> list = new HashMap<String, Object>();
		try {
			list = JSON.parseObject(jsonString,
					new TypeReference<Map<String, Object>>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static HeaderVo getHeader(String jsonString) {
		HeaderVo header = null;
		try {
			Object object = getMap(jsonString).get("header");
			if (object != null && !"".equals(object)) {
				header = getPojo(object.toString(), HeaderVo.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}
	
	@SuppressWarnings("unchecked")
	public static String getContentMsg(String jsonStr) {
		try {
			Object object = getMap(jsonStr).get("content");
			if (object != null && !"".equals(object)) {
				return ((Map<String, String>)object).get("msg");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@SuppressLint("NewApi")
	public static <T> T getContentPojo(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Object object = getMap(jsonString).get("content");
			if (object != null && !"".equals(object)) {
				t = getPojo(object.toString(), cls);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	@SuppressLint("NewApi")
	public static <T> List<T> getContentListPojos(String jsonString,
			Class<T> cls) {
		List<T> list = null;
		try {
			Object object = getMap(jsonString).get("content");
			if (object != null && !"".equals(object)) {
				list = getListPojos(object.toString(), cls);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(list);
		return list;
	}

	public static void main(String[] args) {
		// String jsonString =
		// \"[{\\"categoryDesc\\":\\"测试11111111\\",\\"categoryId\\":1,\\"categoryLevel\\":0,\\"categoryName\\":\\"类别1
		// \\",\\"createTs\\":1393470325000,\\"isAvailable\\":1,\\"superId\\":0}]\";
		// List<BookCategory> list = getListPojos(jsonString,
		// BookCategory.class);
		// System.out.println(list);

		// String jsonStr =
		// "{\"content\":{\"isAvailable\":1,\"macAddress\":\"DA31FB7F-8230-4BA4-95F4-12F1276936E8\",\"password\":\"111111\",\"telephone\":\"15210033642\",\"userId\":10,\"userName\":\"15210033642\"},\"header\":{\"code\":\"1\",\"msg\":\"SUCCESS\"}} ";
		// Map<String, Object> map = getMap(jsonStr);
		// System.out.println(map);
		// System.out.println(getPojo(map.get("content").toString(),
		// Users.class));

		// String str =
		// "{\"content\":[{\"categoryId\":1,\"classId\":1,\"className\":\"规划\"},{\"categoryId\":1,\"classId\":2,\"className\":\"建筑\"},{\"categoryId\":1,\"classId\":3,\"className\":\"结构\"},{\"categoryId\":1,\"classId\":4,\"className\":\"暖通\"},{\"categoryId\":1,\"classId\":5,\"className\":\"其他综合\"}],\"header\":{\"code\":\"1\",\"msg\":\"SUCCESS\"}}";
		// List<BookClass> classList = FastjsonTools.getContentListPojos(str,
		// BookClass.class);
		// System.out.println(classList);

//		String str = "{\"content\":[{\"bookDesc\":\"文档1描述\",\"bookId\":1,\"bookName\":\"文档1\",\"bookUrl\":\"book\",\"categoryId\":1,\"categoryName\":\"专业规范标准\",\"chapterNum\":1,\"classId\":2,\"className\":\"建筑\"},{\"bookDesc\":\"文档1描述\",\"bookId\":1,\"bookName\":\"文档1\",\"bookUrl\":\"book\",\"categoryId\":1,\"categoryName\":\"专业规范标准\",\"chapterNum\":1,\"classId\":2,\"className\":\"建筑\"},{\"bookDesc\":\"文档1描述\",\"bookId\":1,\"bookName\":\"文档1\",\"bookUrl\":\"book\",\"categoryId\":1,\"categoryName\":\"专业规范标准\",\"chapterNum\":1,\"classId\":2,\"className\":\"建筑\"},{\"bookDesc\":\"文档1描述\",\"bookId\":1,\"bookName\":\"文档1\",\"bookUrl\":\"book\",\"categoryId\":1,\"categoryName\":\"专业规范标准\",\"chapterNum\":1,\"classId\":2,\"className\":\"建筑\"},{\"bookDesc\":\"文档1描述\",\"bookId\":1,\"bookName\":\"文档1\",\"bookUrl\":\"book\",\"categoryId\":1,\"categoryName\":\"专业规范标准\",\"chapterNum\":1,\"classId\":2,\"className\":\"建筑\"},{\"bookDesc\":\"文档1描述\",\"bookId\":1,\"bookName\":\"文档1\",\"bookUrl\":\"book\",\"categoryId\":1,\"categoryName\":\"专业规范标准\",\"chapterNum\":1,\"classId\":2,\"className\":\"建筑\"},{\"bookDesc\":\"文档1描述\",\"bookId\":1,\"bookName\":\"文档1\",\"bookUrl\":\"book\",\"categoryId\":1,\"categoryName\":\"专业规范标准\",\"chapterNum\":1,\"classId\":2,\"className\":\"建筑\"},{\"bookDesc\":\"文档2\",\"bookId\":2,\"bookName\":\"文档2\",\"bookUrl\":\"books14-05-04-07-50-20.pdf\",\"categoryId\":1,\"categoryName\":\"专业规范标准\",\"chapterNum\":1,\"classId\":2,\"className\":\"建筑\"},{\"bookDesc\":\"房屋建筑标准\",\"bookId\":13,\"bookName\":\"房屋建筑标准\",\"bookUrl\":\"booksll住宅设计规范GB50096-2011.pdf\",\"categoryId\":1,\"categoryName\":\"专业规范标准\",\"chapterNum\":1,\"classId\":2,\"className\":\"建筑\"}],\"header\":{\"code\":\"1\",\"msg\":\"SUCCESS\"}}";

//		FastjsonTools.getContentListPojos(str, Books.class);
		
		
//		String str = "{\"content\":[{\"bookId\":37,\"bookName\":\"住宅设计规范GB50096-2011\",\"bookUrl\":\"18-18/2014-05-11-06-25-07.pdf\",\"createTs\":1399959460000,\"favouriteId\":19,\"userId\":11,\"userName\":\"admin\"},{\"bookId\":33,\"bookName\":\"民用建筑设计通则-GB50352-2005\",\"bookUrl\":\"18-18/2014-05-10-04-55-02.pdf\",\"createTs\":1399960151000,\"favouriteId\":20,\"userId\":11,\"userName\":\"admin\"}],\"header\":{\"code\":\"1\",\"msg\":\"SUCCESS\"}}"; 
//		FastjsonTools.getContentListPojos(str, FavouriteBook.class);
		
		
		String str1 = "{\"content\":[{\"bookDesc\":\"\",\"bookId\":37,\"bookName\":\"住宅设计规范GB50096-2011\",\"bookUrl\":\"18-18/2014-05-11-06-25-07.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":18,\"className\":\"其它综合\"},{\"bookDesc\":\"\",\"bookId\":26,\"bookName\":\"建筑结构荷载规范GB50009-2012\",\"bookUrl\":\"18-19/2014-05-10-12-03-41.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":19,\"className\":\"结构\"},{\"bookDesc\":\"\",\"bookId\":27,\"bookName\":\"建筑结构荷载规范(条文说明)GB50009-2012\",\"bookUrl\":\"18-19/2014-05-10-12-05-01.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":19,\"className\":\"结构\"},{\"bookDesc\":\"\",\"bookId\":34,\"bookName\":\"砌体结构设计规范GB50003-2011\",\"bookUrl\":\"18-19/2014-05-10-08-01-30.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":19,\"className\":\"结构\"},{\"bookDesc\":\"\",\"bookId\":24,\"bookName\":\"建筑设计防火规范-GB50016-2006\",\"bookUrl\":\"18-20/2014-05-10-06-17-08.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":20,\"className\":\"防火\"},{\"bookDesc\":\"\",\"bookId\":25,\"bookName\":\"建筑设计防火规范(条文说明)-GB50016-2006\",\"bookUrl\":\"18-20/2014-05-10-06-20-07.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":20,\"className\":\"防火\"},{\"bookDesc\":\"\",\"bookId\":28,\"bookName\":\"构筑物抗震设计规范GB50191-2012\",\"bookUrl\":\"18-22/2014-05-10-02-44-20.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":22,\"className\":\"抗震\"},{\"bookDesc\":\"\",\"bookId\":29,\"bookName\":\"构筑物抗震设计规范(条文说明)GB50191-2012\",\"bookUrl\":\"18-22/2014-05-10-02-48-13.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":22,\"className\":\"抗震\"},{\"bookDesc\":\"\",\"bookId\":30,\"bookName\":\"节能建筑评价标准GB T50668-2011\",\"bookUrl\":\"18-24/2014-05-10-03-23-59.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":24,\"className\":\"节能\"},{\"bookDesc\":\"\",\"bookId\":35,\"bookName\":\"地铁设计规范GB50157-2003\",\"bookUrl\":\"18-25/2014-05-10-08-58-48.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":25,\"className\":\"轨道交通\"},{\"bookDesc\":\"\",\"bookId\":36,\"bookName\":\"地铁设计规范(条文说明)GB50157-2003\",\"bookUrl\":\"18-25/2014-05-10-09-00-33.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":25,\"className\":\"轨道交通\"},{\"bookDesc\":\"\",\"bookId\":31,\"bookName\":\"民用建筑供暖通风与空气调节设计规范GB50736-2012\",\"bookUrl\":\"18-29/2014-05-10-04-12-01.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":29,\"className\":\"暖通\"},{\"bookDesc\":\"\",\"bookId\":32,\"bookName\":\"民用建筑供暖通风与空气调节设计规范(条文说明)GB50736-2012\",\"bookUrl\":\"18-29/2014-05-10-04-14-49.pdf\",\"categoryId\":18,\"categoryName\":\"标准规范汇编\",\"chapterNum\":1,\"classId\":29,\"className\":\"暖通\"}],\"header\":{\"code\":\"1\",\"msg\":\"SUCCESS\"}}";
		FastjsonTools.getContentListPojos(str1, Books.class);
	}

	
}