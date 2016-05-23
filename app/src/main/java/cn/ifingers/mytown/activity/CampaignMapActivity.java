package cn.ifingers.mytown.activity;

import android.os.Bundle;

import com.amap.api.maps2d.model.Marker;

import cn.ifingers.mytown.R;

/**
 * Created by syfing on 2016/5/15.
 */
public class CampaignMapActivity extends BaseMapActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaignmap);
        mContext = this;
        initMapView(R.id.view_campaingmap, savedInstanceState);
        initAMap();
    }

    @Override
    protected void setNearByData(double lat, double lon, int radius) {

    }

    @Override
    protected void drawMarker() {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
