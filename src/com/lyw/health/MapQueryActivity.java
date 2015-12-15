package com.lyw.health;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lyw.health.util.DialogUtil;
import com.lyw.health.util.MapUtil;

@ContentView(R.layout.activity_mapquery)
public class MapQueryActivity extends BaseActivity implements OnGetSuggestionResultListener {
	public static final String TAG = "MapQueryActivity";

	@ViewInject(R.id.btnSearch)
	private Button btnSearch;

	@ViewInject(R.id.bmapView)
	private MapView mMapView;

	private BaiduMap mBaiduMap;

	@ViewInject(R.id.etCity)
	private EditText tvCurrentCity;

	@ViewInject(R.id.tvDetail)
	private TextView tvDetail;

	@ViewInject(R.id.searchkey)
	AutoCompleteTextView searchkey;


	PoiSearch mPoiSearch;

	private ArrayAdapter<String> sugAdapter = null;
	private SuggestionSearch mSuggestionSearch = null;
	/**
	 * 搜索关键字输入窗口
	 */
	@ViewInject(R.id.searchkey)
	private AutoCompleteTextView keyWorldsView = null;
	private String city, key;

	private String detailPlaceUrl;
	
	private MapUtil mapUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
		initWidget();
	}
	private void initData() {
		mapUtil=new MapUtil();
		mapUtil.init(this);
		mapUtil.setPoiListener(poiListener);
	}
	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	protected void initWidget() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		sugAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
		keyWorldsView.setAdapter(sugAdapter);
		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				city = tvCurrentCity.getText().toString();
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(city));
			}
		});
	}

	@OnClick({ R.id.btnSearch, R.id.btnMoreData, R.id.tvDetail })
	public void viewClick(View view) {
		city = tvCurrentCity.getText().toString();
		key = searchkey.getText().toString();
		tvDetail.setVisibility(View.INVISIBLE);
		switch (view.getId()) {
		case R.id.btnSearch:
			mapUtil.searchInBound(key);
			DialogUtil.showWaitDialog(this, "查找", "正在查找中...");
			break;
		case R.id.btnMoreData:
			mapUtil.searchInBoundMore(key);
			DialogUtil.showWaitDialog(this, "查找", "正在查找中...");
			break;
		case R.id.tvDetail:
			Intent intent = new Intent(this, HtmlShowActivity.class);
			intent.putExtra("detailPlaceUrl", detailPlaceUrl);
			startActivity(intent);
			break;
		default:
			break;
		}

	};

	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
		public void onGetPoiResult(PoiResult result) {

			if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
				DialogUtil.showToast(getApplicationContext(), "没有更多数据了..");
				DialogUtil.closeAlertDialog();
				return;
			}
			mBaiduMap.clear();
			com.baidu.mapapi.overlayutil.PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			DialogUtil.closeAlertDialog();
			return;
		}

		public void onGetPoiDetailResult(PoiDetailResult result) {
			if (result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(MapQueryActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
				tvDetail.setVisibility(View.INVISIBLE);
			} else {
				tvDetail.setVisibility(View.VISIBLE);
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(result.getName() + ": " + result.getAddress() + "\n");
				stringBuffer.append("距离 : ");
				stringBuffer.append(mapUtil.distance(result.getLocation()));

				tvDetail.setText(stringBuffer);
			}
			DialogUtil.closeAlertDialog();
		}
	};


	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
			// }
			DialogUtil.showWaitDialog(MapQueryActivity.this, "查找", "获取数据中...");
			return true;
		}
	}
 TextView textView;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapUtil.close();
	}
}
