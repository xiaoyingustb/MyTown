package cn.ifingers.mytown.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.ifingers.mytown.R;
import cn.ifingers.mytown.dbutils.HistorySearchDao;
import cn.ifingers.mytown.entities.SearchHistoryBean;
import cn.ifingers.mytown.view.CustomSearchView;

/**
 * Created by syfing on 2016/5/15.
 */
public class FirstPageFragment extends BaseFragment {

    private Context context;

    private CustomSearchView mSearchView;

    private ArrayAdapter<String> mAutoCompleteAdapter;

    private List<SearchHistoryBean> mHistoryBeanList;
    private List<String> mHisAutoCompleteList;
    private HistorySearchDao dao;
    private int mAutoAccount = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_page, null);
        context = getActivity();
        dao = new HistorySearchDao(context);
        mSearchView = (CustomSearchView) view.findViewById(R.id.first_search_view);
        mSearchView.setSearchViewListener(new CustomSearchListener());
        mHistoryBeanList = new ArrayList<>();
        return view;
    }

    private void initHisAutoCompleteList(String text) {
        mHisAutoCompleteList = new ArrayList<String>();
        int count = 0;
        mHistoryBeanList = dao.queryAll();
        for (int i = 0; i < mHistoryBeanList.size() && count < mAutoAccount; i++) {
            if (mHistoryBeanList.get(i).getTitle().contains(text)) {
                mHisAutoCompleteList.add(mHistoryBeanList.get(i).getTitle());
                count++;
            }
        }
        mAutoCompleteAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, mHisAutoCompleteList);
    }

    private class CustomSearchListener implements CustomSearchView.SearchListener {
        @Override
        public void onRefreshAutoComplete(String text) {
            initHisAutoCompleteList(text);
            mSearchView.setAutoCompleteAdapter(mAutoCompleteAdapter);
        }

        /**
         * 搜索操作(点击‘搜索’或点击自动补全listitem)采取的操作
         * @param text
         */
        @Override
        public void onSearch(String text) {

        }
    }
}