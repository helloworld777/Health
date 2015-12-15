package com.lyw.health.util;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;

public class MapUtil {
	public void setLoad_Index(int load_Index) {
		this.load_Index = load_Index;
	}

	public void setPoiListener(OnGetPoiSearchResultListener poiListener) {
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
	}

	private LocationClient mLocClient;
	private LatLng currentLatLng;
	
	private PoiSearch mPoiSearch;
	private int load_Index=0;
	public MapUtil(){
		mPoiSearch=PoiSearch.newInstance();
	}
	
	public void init(Context context) {
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(new MyLocationListenner());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);//
		option.setCoorType("bd09ll"); //
		option.setScanSpan(1000);

		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	public void searchInCity(String city,String key){
		mPoiSearch.searchInCity((new
				 PoiCitySearchOption()).city(city).keyword(key).pageNum(load_Index));
	}
	public void searchInCityMore(String city,String key){
		load_Index++;
		searchInCity(city,key);
	}
	public void searchInBound(String key){
		LatLngBounds latLngBounds=new LatLngBounds.Builder().include(currentLatLng).build();
		mPoiSearch.searchInBound(new PoiBoundSearchOption().bound(latLngBounds).keyword(key).pageNum(load_Index));
	}
	public void searchInBoundMore(String key){
		load_Index++;
		searchInBound(key);
	}
	public void searchPoiDetail(String uid){
		mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(uid));
	}
	public String distance(LatLng l){
		return resolveDistance(DistanceUtil.getDistance(currentLatLng, l));
	}
	private String resolveDistance(double b) {
		int mi = (int) b;
		String result = null;
		if (mi < 500) {
			result = "< 500 m";
		} else {
			int li = mi / 1000 == 0 ? 1 : mi / 1000;

			result = li + " km";

		}
		return result;
	}
	public LatLng getCurrentLatLng() {
		return currentLatLng;
	}

	public void setCurrentLatLng(LatLng currentLatLng) {
		this.currentLatLng = currentLatLng;
	}

	public PoiSearch getmPoiSearch() {
		return mPoiSearch;
	}

	public void close(){
		mLocClient.stop();
		mPoiSearch.destroy();
	}
	public class MyLocationListenner implements BDLocationListener {

		boolean isFirstLoc = true;

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null )
				return;
			if (isFirstLoc) {
				isFirstLoc = false;
				setCurrentLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
}
