package cn.ifingers.mytown.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by syfing on 2016/5/17.
 */
public abstract class TBaseAdapter<T> extends BaseAdapter {
    private List<T> mDatas;
    protected Context context;

    public TBaseAdapter(List<T> mDatas, Context context){
        this.mDatas = mDatas;
        this.context = context;
    }

    public List<T> getData(){
        return mDatas;
    }

    public void setData(List<T> mDatas){
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
