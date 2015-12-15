package com.lyw.health.app;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class HealthApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(this);
	}
}
