package cn.ifingers.mytown.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.ifingers.mytown.utils.ApplicationUtil;

/**
 * Created by syfing on 2016/5/18.
 */
public class BaseFragment extends Fragment {
    public ApplicationUtil mApplication;
    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mApplication = (ApplicationUtil) getActivity().getApplication();
    }
}
