package cn.ifingers.mytown.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.ifingers.mytown.R;
import cn.ifingers.mytown.adapters.HistoryDataAdapter;
import cn.ifingers.mytown.dbutils.HistorySearchDao;
import cn.ifingers.mytown.entities.SearchHistoryBean;
import cn.ifingers.mytown.view.CustomSearchView;
import cn.ifingers.mytown.view.FlowLayout;

/**
 * Created by syfing on 2016/5/18.
 */
public class SearchActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context mContext;
    private InputMethodManager mInputManager;

    private CustomSearchView mSearchView;
    private ArrayAdapter<String> mAutoCompleteAdapter;
    private FlowLayout mFlowLayout;
    private FrameLayout mFrameLayout;

    private ListView mAllHistoryLv;
    private HistoryDataAdapter mAdapter;

    private ListView mDropDownLv;

    private List<SearchHistoryBean> mAllHistoryBeanList;
    private List<SearchHistoryBean> mHistoryBeanList;
    private List<String> mHisAutoCompleteList;
    private HistorySearchDao mSearchDao;
    private int mAutoAccount = 4;

    private int[] mHotWordBgs = { R.drawable.text_red_bg,
            R.drawable.text_green_bg, R.drawable.text_blue_bg };

    private String[] mHotWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mSearchDao = new HistorySearchDao(mContext);
        mSearchView = (CustomSearchView) findViewById(R.id.search_view);
        initFrameLayout();
        mDropDownLv = (ListView) findViewById(R.id.outer_history_search_lv);
        mSearchView.setSearchListView(mDropDownLv);

        mFlowLayout = (FlowLayout) findViewById(R.id.view_hot_search);
        mAllHistoryLv = (ListView) findViewById(R.id.history_all_lv);

        mHotWords = getResources().getStringArray(R.array.hot_search);

        mAllHistoryBeanList = mSearchDao.queryAll();
        mAdapter = new HistoryDataAdapter(mAllHistoryBeanList, mContext);
        mAllHistoryLv.setAdapter(mAdapter);
        mAllHistoryLv.setOnItemClickListener(this);

        mSearchView.setSearchViewListener(new CustomSearchListener());
        initFlowLayout();
    }

    private void initFrameLayout(){
        mFrameLayout = (FrameLayout) findViewById(R.id.search_frame);
        mFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFrameLayout.setFocusable(true);
                mFrameLayout.setFocusableInTouchMode(true);
                mFrameLayout.requestFocus();
                if(mInputManager.isActive()){
                    mInputManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void initFlowLayout(){
        Random random = new Random();
        for(int i = 0; i < mHotWords.length; i++){
            final TextView tv = new TextView(mContext);
            tv.setText(mHotWords[i]);
            tv.setBackgroundResource(mHotWordBgs[random.nextInt(mHotWordBgs.length)]);
            MarginLayoutParams marginLayoutParam = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
            marginLayoutParam.leftMargin = 10;
            marginLayoutParam.rightMargin = 10;
            marginLayoutParam.topMargin = 10;
            marginLayoutParam.bottomMargin = 10;
            tv.setLayoutParams(marginLayoutParam);
            tv.setPadding(10, 10, 10, 10);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchView.setText(tv.getText().toString());
                }
            });
            mFlowLayout.addView(tv);
        }
    }

    private void initHisAutoCompleteList(String text) {
        mHisAutoCompleteList = new ArrayList<>();
        int count = 0;
        mHistoryBeanList = mSearchDao.queryAll();
        for (int i = 0; i < mHistoryBeanList.size() && count < mAutoAccount; i++) {
            if (mHistoryBeanList.get(i).getTitle().contains(text)) {
                mHisAutoCompleteList.add(mHistoryBeanList.get(i).getTitle());
                count++;
            }
        }
        mAutoCompleteAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, mHisAutoCompleteList);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSearchView.setText(mAllHistoryBeanList.get(position).getTitle());
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
            mFrameLayout.setFocusable(true);
            mFrameLayout.setFocusableInTouchMode(true);
            mFrameLayout.requestFocus();
            if(mInputManager.isActive()){
                mInputManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
        }
    }
}
