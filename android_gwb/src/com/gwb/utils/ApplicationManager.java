package com.gwb.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;

public class ApplicationManager {

	private static List<Activity> activityList = new ArrayList<Activity>();

	public static void remove(Activity activity) {
		activityList.remove(activity);
		Log.i("EXIT", "remove " + activity.toString());
	}

	public static void add(Activity activity) {
		activityList.add(activity);
		Log.i("EXIT", "add" + activity.toString());
	}

	public static void finishProgram() {
		for (Activity activity : activityList) {
			activity.finish();
			Log.i("EXIT", "finish" + activity.toString());
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
