package com.lyw.health;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lyw.health.util.DialogUtil;
import com.lyw.health.util.LogUtil;

@ContentView(R.layout.activity_html_show)
public class HtmlShowActivity extends BaseActivity {
	@ViewInject(R.id.webView)
	WebView webView;
	private int progress=0;
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String detailPlaceUrl = getIntent().getStringExtra("detailPlaceUrl");
		LogUtil.d(this,"detailPlaceUrl--------------"+detailPlaceUrl);
		
		webView.getSettings().setDomStorageEnabled(true);     
		webView.getSettings().setAppCacheMaxSize(1024*1024*8);    
	    String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();    
	    webView.getSettings().setAppCachePath(appCachePath);    
	    webView.getSettings().setAllowFileAccess(true);    
	    webView.getSettings().setAppCacheEnabled(true);   
	    
	    
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);// 设置使支持缩放
		webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
            	LogUtil.d(HtmlShowActivity.this,"newProgress:"+newProgress);
                if (newProgress == 100) {
                    // 网页加载完成
                	if((newProgress-progress)>50){
                		;
                	}else{
                		DialogUtil.closeAlertDialog();
                		progress=0;
                	}
                }else{
                	
                	progress=newProgress;
                }

            }
        });
		webView.loadUrl(detailPlaceUrl);
		DialogUtil.showWaitDialog(HtmlShowActivity.this, "加载数据", "数据加载中....");
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				LogUtil.d(HtmlShowActivity.this, "shouldOverrideUrlLoading");
				DialogUtil.showWaitDialog(HtmlShowActivity.this, "加载数据", "数据加载中....");
				return true;
			}
			@Override   //转向错误时的处理  
            public void onReceivedError(WebView view, int errorCode,  
                    String description, String failingUrl) {  
               DialogUtil.showToast(HtmlShowActivity.this, "加载出错");  
            }  
		});
		
	}
	public void viewClick(View view){
		
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();// 返回上一页面
				return true;
			} else {
//				System.exit(0);// 退出程序
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
