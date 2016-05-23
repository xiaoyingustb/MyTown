package cn.ifingers.mytown.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

import cn.ifingers.mytown.R;
import cn.ifingers.mytown.adapters.ShopLvAdapter;
import cn.ifingers.mytown.entities.GoodsInfo;
import cn.ifingers.mytown.entities.ResponseObject;

/**
 * Created by syfing on 2016/5/15.
 */
public class ShopsFragment extends BaseFragment {

    private PullToRefreshListView mListView;
    private List<GoodsInfo> mShopDatas;
    private ShopLvAdapter mShopsAdapter;

    private int page = 0,size = 12;

    private static final String TAG = "ShopsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shops, null);
        mListView = (PullToRefreshListView) view.findViewById(R.id.shops_pulllv);
        mListView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {

            }
        });
        setEvents();
        return view;
    }

    private void listViewRefresh() {
        //首次来自动刷新数据
        new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                //触发刷新数据的操作
                mListView.setRefreshing();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 200);

    }

    private void setEvents() {

        //设置数据的加载模式
        mListView.setMode(PullToRefreshBase.Mode.BOTH);

        //设置加载的过程中允许滚动
        mListView.setScrollingWhileRefreshingEnabled(true);

        //刷新数据
		listViewRefresh();

        //只有头部和尾部才会触发的监听事件
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            //上拉加载和下拉刷新的回调函数
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载数据
                setDatas(refreshView.getScrollY()>0);
            }
        });

        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });
    }

    private void setDatas( final boolean isDown) {//requst.getParameter();


        if(isDown){
            //查询下一页的信息
            page++;
        }else{
            //刷新数据
            page = 1;
        }
        //传递查询的参数信息
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addQueryStringParameter("size", String.valueOf(size));
//        params.addQueryStringParameter("catId", "2858");

        new HttpUtils().send(HttpRequest.HttpMethod.GET, mApplication.GOODS_PATH, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                //通知数据已经加载完成
                mListView.onRefreshComplete();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                //通知数据已经加载完成
                mListView.onRefreshComplete();
                Log.i(TAG, arg0.result);
                ResponseObject<List<GoodsInfo>> obj =
                        new Gson().fromJson(arg0.result, new TypeToken<ResponseObject<List<GoodsInfo>>>() {
                        }.getType());

                if (isDown) {
                    //加载数据
                    mShopDatas.addAll(obj.getDatas());
                    mShopsAdapter.notifyDataSetChanged();
                } else {
                    //刷新数据
                    mShopDatas = obj.getDatas();
                    Log.i(TAG, String.valueOf(obj));
                    Log.i(TAG, String.valueOf(mShopDatas));
                    mShopsAdapter = new ShopLvAdapter(mShopDatas, mContext);
                    mListView.setAdapter(mShopsAdapter);
                }

                //判断是否到达最后一页
                if (obj.getPage() == obj.getCount()) {
                    //只能刷新数据
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }

                //如果是刷新的话，支持加载数据
                if (!isDown) {
                    //支持上拉加载和下拉刷新
                    mListView.setMode(PullToRefreshBase.Mode.BOTH);
                }
            }
        });
    }
}