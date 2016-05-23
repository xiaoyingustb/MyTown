package cn.ifingers.mytown.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;

import cn.ifingers.mytown.R;

/**
 * Created by syfing on 2016/5/15.
 */
public abstract class BaseMapActivity extends Activity implements AMapLocationListener, LocationSource, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener {
    protected MapView mMapView;
    protected AMap mAMap;
    protected AMapLocationClient mClient;
    protected AMapLocationClientOption mOption;
    protected LocationSource.OnLocationChangedListener mLocationListener;
    protected Context mContext;

    protected final String TAG = getClass().getSimpleName();

    protected void initMapView(int mapViewId, Bundle savedInstanceState){
        mMapView = (MapView) findViewById(mapViewId);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mClient.stopLocation();
        mClient.onDestroy();
    }

    protected void initAMap(){
        if(mAMap == null){
            mAMap = mMapView.getMap();
        }

        if(mOption == null){
            mOption = new AMapLocationClientOption();
        }

        if(mClient == null){
            mClient = new AMapLocationClient(mContext.getApplicationContext());
        }

        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mOption.setOnceLocation(true);

        mClient.setLocationOption(mOption);
        mClient.setLocationListener(this);

        mAMap.setLocationSource(this);
        mAMap.setMyLocationEnabled(true);
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnInfoWindowClickListener(this);
    }

    protected abstract void setNearByData(double lat, double lon, int radius);

    protected abstract  void drawMarker();

    /**
     * AMapLocationListener 接口强制实现的方法
     * @param aMapLocation 封装了经纬度的位置对象
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        Log.i(TAG, "onLocationChanged");
        Log.i("onLocationChanged", String.valueOf(mLocationListener));
        Log.i("onLocationChanged", String.valueOf(aMapLocation));
        Log.i("onLocationChanged", String.valueOf(aMapLocation.getErrorCode()));

        if (mLocationListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocationListener.onLocationChanged(aMapLocation);
                double lat = aMapLocation.getLatitude();
                double lon = aMapLocation.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                Log.i(TAG, "onLocationChanged");
                mAMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition(latLng, 16, 0, 0)));
                setNearByData(lat, lon, 1000);
            }
        }
    }

    /**
     * LocationSource 接口强制实现的方法
     * @param onLocationChangedListener 用于启动定位服务的接口对象
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mLocationListener = onLocationChangedListener;
        mClient.startLocation();
        Log.i(TAG, "activated");
    }

    /**
     * LocationSource 接口强制实现的方法
     */
    @Override
    public void deactivate() {

    }

    /**
     * OnMarkerClickListener 接口强制实现的方法
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /**
     * OnInfoWindowClickListener 接口强制实现的方法
     * @param marker
     */
    @Override
    public abstract void onInfoWindowClick(Marker marker);
}
