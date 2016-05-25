package cn.ifingers.mytown.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.ifingers.mytown.R;
import cn.ifingers.mytown.activity.MonitorDetailActivity;

/**
 * Created by syfing on 2016/5/15.
 */
public class MonitorFragment extends BaseFragment {
    private Button mMonitorBtn;
    private Activity mParentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_monitor, null);
        mMonitorBtn = (Button) view.findViewById(R.id.monitor_indicate_btn);
        mMonitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mParentActivity, MonitorDetailActivity.class);
                mParentActivity.startActivity(intent);
            }
        });
        return view;
    }
}