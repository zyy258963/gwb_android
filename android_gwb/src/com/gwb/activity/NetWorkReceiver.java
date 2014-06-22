package com.gwb.activity;

import com.gwb.utils.ApplicationManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetWorkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!mobileInfo.isConnected() && !wifiInfo.isConnected()) {
			AlertDialog alertDialog = new AlertDialog.Builder(context)
					.setTitle("提示").setMessage("您当前未联网，即将退出程序。。").create();
			
			Toast.makeText(context, "mobile:"+mobileInfo.isConnected()+"\n"+"wifi:"+wifiInfo.isConnected()  
                    +"\n",1).show();  
			alertDialog.show();
			
			try {
				new Thread();
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ApplicationManager.finishProgram();
					
		}
	}

}
