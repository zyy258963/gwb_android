//package com.gwb.utils;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.util.DisplayMetrics;
//import android.util.Log;
//
//public class DensityUtil {
//
//	private static final String TAG = DensityUtil.class.getSimpleName();
//
//	// 当前屏幕的densityDpi
//	private static float dmDensityDpi = 0.0f;
//	private static DisplayMetrics dm;
//	private static float scale = 0.0f;
//	private static float default_dmDensityDpi = 320.0f;
//	private static int default_width = 720;
//	private static int default_height= 1280;
//
//	/**
//	 * 
////	 * 根据构造函数获得当前手机的屏幕系数
//	 * 
//	 * */
//	@SuppressLint("NewApi")
//	public DensityUtil(Context context) {
//		// 获取当前屏幕
//		dm = new DisplayMetrics();
//		dm = context.getApplicationContext().getResources().getDisplayMetrics();
////		default_width = dm.widthPixels;
////		default_height = dm.heightPixels;
//		// 设置DensityDpi
//		setDmDensityDpi(dm.densityDpi);
//	  
//		// 密度因子
////		scale = getDmDensityDpi() / 160 * ( dm.densityDpi / default_dmDensityDpi);
//		scale = getDmDensityDpi() / 160;
//		Log.i(TAG, toString()+"  dm.densityDpi: "+dm.densityDpi + " default_dmDensityDpi: "+default_dmDensityDpi + " \n scale:" +scale);
//	}
//
//	/**
//	 * 当前屏幕的density因子
//	 * 
//	 * @param DmDensity
//	 * @retrun DmDensity Getter
//	 * */
//	public static float getDmDensityDpi() {
//		return dmDensityDpi;
//	}
//
//	/**
//	 * 当前屏幕的density因子
//	 * 
//	 * @param DmDensity
//	 * @retrun DmDensity Setter
//	 * */
//	public static void setDmDensityDpi(float dmDensityDpi) {
//		DensityUtil.dmDensityDpi = dmDensityDpi;
//	}
//
//	/**
//	 * 密度转换像素
//	 * */
//	public int dip2px(float dipValue) {
//		return (int) (dipValue * scale + 0.5f);
//	}
//
//	/**
//	 * 像素转换密度
//	 * */
//	public int px2dip(float pxValue) {
//		return (int) (pxValue / scale + 0.5f);
//	}
//
//	@Override
//	public String toString() {
//		return " dmDensityDpi:" + dmDensityDpi;
//	}
//
//}
