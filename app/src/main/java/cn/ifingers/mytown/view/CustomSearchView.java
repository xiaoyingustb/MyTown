package cn.ifingers.mytown.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.ifingers.mytown.R;
import cn.ifingers.mytown.dbutils.HistorySearchDao;
import cn.ifingers.mytown.entities.SearchHistoryBean;

/**
 * Created by syfing on 2016/5/17.
 */
public class CustomSearchView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private SimpleDateFormat mDateFormat;
    private HistorySearchDao mSearchDao;
    private SearchListener mListener;
    private ArrayAdapter<String> mAutoCompleteAdapter;

    private ImageView mBackImg;
    private EditText mSearchInput;
    private Drawable mRightDrawable;
    private Drawable mLeftDrawable;
    private TextView mSearchText;
    private ListView mSearchListView;

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_searchview, this);
        mSearchDao = new HistorySearchDao(context);
        mDateFormat = new SimpleDateFormat("MM月dd日hh时mm分");
        initViews();
    }

    private void initViews(){
        mBackImg = (ImageView) findViewById(R.id.searchview_back);
        mSearchInput = (EditText) findViewById(R.id.searchview_input);
        mLeftDrawable = mSearchInput.getCompoundDrawables()[0];
        mRightDrawable = mSearchInput.getCompoundDrawables()[2];

        mSearchListView = (ListView) findViewById(R.id.searchview_lv);
        mSearchText = (TextView) findViewById(R.id.searchview_searchtv);
        mBackImg.setOnClickListener(this);
        mSearchInput.setOnClickListener(this);
        mSearchInput.addTextChangedListener(new EditChangedListener());
        mSearchText.setOnClickListener(this);
        mSearchListView.setOnItemClickListener(mOnItemClickListener);
        mSearchInput.setOnEditorActionListener(mOnEditorActionListener);
    }

    public void setSearchListView(ListView listView){
        this.mSearchInput.setOnEditorActionListener(null);
        this.mSearchListView.setOnItemClickListener(null);
        this.mSearchListView = listView;
        this.mSearchListView.setOnItemClickListener(mOnItemClickListener);
        this.mSearchInput.setOnEditorActionListener(mOnEditorActionListener);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String str = mAutoCompleteAdapter.getItem(position);
            mSearchInput.setText(str);
            notifyStartSearching(str);
            SearchHistoryBean bean = new SearchHistoryBean();
            bean.setTime(mDateFormat.format(new Date()));
            bean.setTitle(str);
            mSearchDao.insertAndUpdate(bean);
            mSearchListView.setVisibility(View.GONE);
        }
    };

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mSearchListView.setVisibility(View.GONE);
                notifyStartSearching(mSearchInput.getText().toString());
            }
            return true;
        }
    };

    public void setText(String str) {
        mSearchInput.setText(str);
        notifyStartSearching(str);
        mSearchListView.setVisibility(View.GONE);
    }

    @Override
    public void clearFocus(){
        super.clearFocus();
        mSearchInput.clearFocus();
    }

    public void setSearchViewListener(SearchListener mListener) {
        this.mListener = mListener;
    }

    public void setAutoCompleteAdapter(ArrayAdapter<String> autoRefreshAdapter) {
        this.mAutoCompleteAdapter = autoRefreshAdapter;
        mSearchListView.setAdapter(this.mAutoCompleteAdapter);
    }

    private class EditChangedListener implements TextWatcher {
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void afterTextChanged(Editable s) {
            if (s == null || s.toString().equals("")) {
                mSearchInput.setCompoundDrawables(mLeftDrawable, null, null,
                        null);
                mAutoCompleteAdapter = new ArrayAdapter<String>(mContext,
                        R.layout.layout_dropdown_list_item,
                        new ArrayList<String>());
                mSearchListView.setAdapter(mAutoCompleteAdapter);
                mSearchListView.setVisibility(View.GONE);
            } else {
                mSearchInput.setCompoundDrawables(mLeftDrawable, null,
                        mRightDrawable, null);
                mListener.onRefreshAutoComplete(s.toString());
                mAutoCompleteAdapter.notifyDataSetChanged();
            }
            mSearchInput.invalidate();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchview_back:
                ((Activity) mContext).finish();
                break;
            case R.id.searchview_input:
                mSearchListView.setVisibility(View.VISIBLE);
                break;
            case R.id.searchview_searchtv:
                String str = mSearchInput.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    SearchHistoryBean bean = new SearchHistoryBean();
                    bean.setTime(mDateFormat.format(new Date()));
                    bean.setTitle(str);
                    mSearchDao.insertAndUpdate(bean);
                    notifyStartSearching(str);
                }
                break;
        }
    }

    private void notifyStartSearching(String text) {
        if (mListener != null && mAutoCompleteAdapter != null) {
            mListener.onSearch(text);
        }
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public interface SearchListener {

        // 用于根据输入的文本查找潜在数据，关联适配器，并更新mSearchListView的方法
        void onRefreshAutoComplete(String text);

        // 在点击键盘回车、mSearchListView的OnItemClick方法以及“搜索”TextView点击事件发生后，
        // 执行搜索功能的方法
        void onSearch(String text);
    }
}
