package com.lyw.health;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lyw.health.adapter.LuAdapter;
import com.lyw.health.adapter.ViewHolder;
import com.lyw.health.util.DialogUtil;
import com.lyw.health.util.LogUtil;
import com.lyw.health.util.MapUtil;
import com.lyw.health.widget.xlistview.XListView;

@ContentView(R.layout.activity_place_search)
public class PlaceSearchActivity extends BaseActivity {
	@ViewInject(R.id.etKey)
	EditText etKey;
	@ViewInject(R.id.ivMap)
	ImageView ivMap;
	@ViewInject(R.id.ivSearch)
	ImageView ivSearch;

	private MapUtil mapUtil;

	@ViewInject(R.id.tvNoDataTips)
	private TextView tvNoDataTips;
	
	@ViewInject(R.id.listview)
	private XListView listview;

	@SuppressWarnings("unused")
	private List<PoiDetailResult> poiDetailResults;
	private List<PoiInfo> poiInfos;
//	private LuAdapter<PoiDetailResult> luAdapter;
	private LuAdapter<PoiInfo> luAdapter;

	
	private Handler handler=new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mapUtil = new MapUtil();
		mapUtil.setPoiListener(poiListener);

		mapUtil.init(this);
		poiDetailResults = new ArrayList<PoiDetailResult>();
		poiInfos = new ArrayList<PoiInfo>();

		luAdapter = new LuAdapter<PoiInfo>(this, poiInfos, R.layout.item_place_detail) {
			@Override
			public void convert(ViewHolder helper, PoiInfo item) {
				// TODO Auto-generated method stub
				helper.setString(R.id.tvPlaceName, item.name);
				helper.setString(R.id.tvPlace, item.address);
				helper.setString(R.id.tvDistance, mapUtil.distance(item.location));
				
				
			}
		};
//		luAdapter = new LuAdapter<PoiDetailResult>(this, poiDetailResults, R.layout.item_place_detail) {
//			@Override
//			public void convert(ViewHolder helper, PoiDetailResult item) {
//				// TODO Auto-generated method stub
//				helper.setString(R.id.tvPlaceName, item.getName());
//				helper.setString(R.id.tvPlace, item.getAddress());
//				helper.setString(R.id.tvDistance, mapUtil.distance(item.getLocation()));
//				
//				switch (item.getFavoriteNum()) {
//				case 1:
//					helper.setBackgroundResource(R.id.ivFavorite1, R.drawable.ic_sub_review_rating_star_on);
//				case 2:
//					helper.setBackgroundResource(R.id.ivFavorite2, R.drawable.ic_sub_review_rating_star_on);
//				case 3:
//					helper.setBackgroundResource(R.id.ivFavorite3, R.drawable.ic_sub_review_rating_star_on);
//				case 4:
//					helper.setBackgroundResource(R.id.ivFavorite4, R.drawable.ic_sub_review_rating_star_on);
//				case 5:
//					helper.setBackgroundResource(R.id.ivFavorite5, R.drawable.ic_sub_review_rating_star_on);
//				default:
//					break;
//				}
//				
//			}
//		};
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(new MyXListViewListener());
		
		listview.setAdapter(luAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LogUtil.d(PlaceSearchActivity.this, "position:"+position);
//				LogUtil.d(PlaceSearchActivity.this, "uid:"+poiInfos.get(position).uid);
				LogUtil.d(PlaceSearchActivity.this, "name:"+poiInfos.get(position-1).name);
				mapUtil.searchPoiDetail(poiInfos.get(position-1).uid);
			}
		});
		checkData();
	}
	
	private void checkData(){
		if(poiInfos.size()==0){
			tvNoDataTips.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		}else{
			tvNoDataTips.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}
	}
	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
		public void onGetPoiResult(PoiResult result) {

			LogUtil.d(PlaceSearchActivity.this, "result:"+result);
			if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
				DialogUtil.showToast(getApplicationContext(), "没有更多数据了..");
				DialogUtil.closeAlertDialog();
				return;
			}
			if(result.getAllPoi()==null){
				DialogUtil.showToast(getApplicationContext(), "没有更多数据了..");
				DialogUtil.closeAlertDialog();
				return;
				
			}
			LogUtil.d(PlaceSearchActivity.this, "temppoiInfos.size():"+result.getAllPoi().size());
			if(refresh){
				poiInfos.addAll(0, result.getAllPoi());
			}else{
				poiInfos.addAll(result.getAllPoi());
			}
			luAdapter.notifyDataSetChanged();
			DialogUtil.closeAlertDialog();
			checkData();
			return;
		}
		public void onGetPoiDetailResult(PoiDetailResult result) {
			LogUtil.d(PlaceSearchActivity.this,"onGetPoiDetailResult ："+result);
			
			if (result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(PlaceSearchActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			} else {
				LogUtil.d(PlaceSearchActivity.this,"name ："+result.getName());
				String detailPlaceUrl=result.getDetailUrl();
				Intent intent = new Intent(PlaceSearchActivity.this, HtmlShowActivity.class);
				intent.putExtra("detailPlaceUrl", detailPlaceUrl);
				startActivity(intent);
			}
		}
	};
	public boolean refresh;
	private String city="深圳";
	@OnClick({ R.id.ivMap, R.id.ivSearch })
	public void viewClick(View view) {
		switch (view.getId()) {
		case R.id.ivMap:
			startActivity(new Intent(this, MapQueryActivity.class));
			break;
		case R.id.ivSearch:
			String key = etKey.getText().toString();
			mapUtil.setLoad_Index(0);
			mapUtil.searchInCity(city, key);
			DialogUtil.showWaitDialog(this, "查找", "正在查找中...");
			break;

		default:
			break;
		}
	}
	class MyXListViewListener implements XListView.IXListViewListener {
		@Override
		public void onRefresh() {
//			foodManager.refreshData(true);
			refresh=true;
			String key = etKey.getText().toString();
//			mapUtil.searchInBoundMore(key);
			mapUtil.searchInCityMore(city, key);
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					listview.setRefreshTime(DateFormat.getDateTimeInstance().format(new Date()));
					listview.stopRefresh();
					
				}
			}, 2000);
			
			
		}

		@Override
		public void onLoadMore() {
			refresh=false;
			String key = etKey.getText().toString();
			mapUtil.searchInCityMore(city,key);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					listview.stopLoadMore();
				}
			}, 2000);
			
		}

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapUtil.close();
	}
}
