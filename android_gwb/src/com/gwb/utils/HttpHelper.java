package com.gwb.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpHelper {
	
	/**
	 * 从网上获取内容get方式
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getStringFromUrl(String url)
			throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}

	public static String sendGetMessage(String path, String encode) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(path);
			if (url != null) {
				connection = (HttpURLConnection) url.openConnection();
				connection.setReadTimeout(3000);
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				int responseCode = connection.getResponseCode();
				System.out.println("responseCode :"
						+ String.valueOf(responseCode));
				if (responseCode == 200) {
					return changeInputStreamToString(
							connection.getInputStream(), encode);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return "";
	}

	public static String changeInputStreamToString(InputStream inputStream,
			String encode) {
		String rs = "";
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		int len = 0;
		byte[] data = new byte[1024];
		try {
			if (inputStream != null) {
				while ((len = inputStream.read(data)) != -1) {
					arrayOutputStream.write(data, 0, len);
				}
				rs = new String(arrayOutputStream.toByteArray(), encode);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (arrayOutputStream != null) {
				try {
					arrayOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return rs;
	}

	public static String sendPostMessage(String path, Map<String, String> params,
			String encode) {
		// 获得url的输入输出流
		StringBuffer buffer = new StringBuffer();
		try {
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					buffer.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), encode))
							.append("&");
				}
			}
			buffer.deleteCharAt(buffer.length() - 1);
			System.out.println(buffer.toString());

			URL url = new URL(path);
			
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setReadTimeout(3000);
//			httpURLConnection.setReadTimeout(3000);// 设置超时时间
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoInput(true);// 从服务器获得数据
			httpURLConnection.setDoOutput(true);// 向服务器写数据
			// 获得上传信息的字节的大小
			byte data[] = buffer.toString().getBytes();
			// 设置请求体的属性
			httpURLConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			httpURLConnection.setRequestProperty("Content-Length",
					String.valueOf(data.length));

			// 通过输出流来向服务器发送数据
			OutputStream outputStream = (OutputStream) httpURLConnection
					.getOutputStream();
			outputStream.write(data);
			// 获得返回类型编码
			int responseCode = httpURLConnection.getResponseCode();
			Log.i("HttpHelper", "responseCode:" +responseCode);
			if (responseCode == 200) {
				// 通过输入流来获得服务器返回的数据
				return changeInputStreamToString(
						httpURLConnection.getInputStream(), encode);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("clientType", "android");
		params.put("username", "admin");
		params.put("password", "admin");

//		String rs = sendPostMessage(LOGIN_PATH, params, "utf-8");
//		System.out.println("--result-->>" + rs);
		
		String str = sendGetMessage(ConstantParams.URL_GET_CATEGORYS, "utf-8");
		System.out.println(str);
		
	}

}
