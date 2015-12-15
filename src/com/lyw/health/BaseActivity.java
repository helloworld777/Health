package com.lyw.health;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.lidroid.xutils.ViewUtils;

public class  BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
	}
	
}
