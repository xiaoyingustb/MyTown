package cn.ifingers.mytown.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.ifingers.mytown.entities.CampaignInfo;
import cn.ifingers.mytown.utils.TBaseAdapter;

/**
 * Created by syfing on 2016/5/15.
 */
public class CampaignLvAdapter extends TBaseAdapter {

    public CampaignLvAdapter(List<CampaignInfo> mDatas, Context context) {
        super(mDatas, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
