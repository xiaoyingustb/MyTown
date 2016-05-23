package cn.ifingers.mytown.activity;

import android.os.Bundle;

import com.amap.api.maps2d.model.Marker;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import cn.ifingers.mytown.R;

/**
 * Created by syfing on 2016/5/15.
 */
public class ShopsMapActivity extends BaseMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopmap);
        mContext = this;
        initMapView(R.id.view_shopmap, savedInstanceState);
        initAMap();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
