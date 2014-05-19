package com.gwb.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.net.Uri;
import android.util.Log;

public class DownloadUtils {

	public DownloadUtils() {
		// TODO Auto-generated constructor stub
	}

	public static void getTempFile(String uriPath) throws Exception{
		URL url;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		InputStream is = null;
		int size =0;
		int total = 0;
		try {			
			url = new URL(Uri.decode(uriPath));
//			url = new URL(new String(uriPath.getBytes("UTF-8"),"UTF-8"));
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			// 获得文件大小
			size = connection.getContentLength();
			System.out.println("size:" + size);
			try {
				is = connection.getInputStream();
				fos = new FileOutputStream(ConstantParams.TEMP_FILE_PATH);
				bis = new BufferedInputStream(is);
			} catch (Exception e) {
				throw e;
			}
			if (is == null) {
//				ConstantParams.TEMP_FILE = null;
			}else {
				byte buffer[] = new byte[1024];
				int len;
				while ((len = bis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					total += len;
				}
				ConstantParams.TEMP_FILE = new File(ConstantParams.TEMP_FILE_PATH);
			}
			
			Log.i("PDF", "ConstantParams.TEMP_FILE " + ConstantParams.TEMP_FILE.getAbsolutePath());
			Log.i("DownloadUtils", "fos:" + fos + ": bis:"+bis+": is:"+is + ": total:" +total);
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (fos!=null) {
					fos.close();
				}
				if (bis!=null) {
					bis.close();
				}
				if (is!=null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
	}

	public static void main(String[] args) {
//		String path = "http://zyy258963.w4.en.tm/pdf/i百悦用户登录后会员页面开发.pdf";
		String path = "http://192.168.1.2:8080/FileUpload/upload/books/books/1/19/交通建筑产品手册初稿-第二章项目工作.pdf";
		try {
			String path1 = new String(path.getBytes("UTF-8"),"UTF-8");
			System.out.println(path1);
			try {
				DownloadUtils.getTempFile(path1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
