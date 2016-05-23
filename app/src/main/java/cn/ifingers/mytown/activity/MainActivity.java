package cn.ifingers.mytown.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import cn.ifingers.mytown.R;
import cn.ifingers.mytown.fragments.FirstPageFragment;
import cn.ifingers.mytown.fragments.MonitorFragment;
import cn.ifingers.mytown.fragments.MyFragment;
import cn.ifingers.mytown.fragments.ShopsFragment;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    private FragmentManager mManager;
    private FragmentTransaction mTransaction;
    private String mLastTag;

    private RadioGroup mGroup;
    private RadioButton mFirstRadio;

    private FirstPageFragment mFPFragment;
    private ShopsFragment mShopsFragment;
    private MonitorFragment mMonitorFragment;
    private MyFragment mMyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //个推sdk初始化
        PushManager.getInstance().initialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        mManager = getSupportFragmentManager();

        mGroup = (RadioGroup) findViewById(R.id.main_bottom_group);
        mGroup.setOnCheckedChangeListener(this);
        mFirstRadio = (RadioButton) mGroup.getChildAt(0);
        mFirstRadio.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onProfileSignIn("syf2013");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onProfileSignOff();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mTransaction = mManager.beginTransaction();
        String tag = String.valueOf(checkedId);
        Fragment fragment = mManager.findFragmentByTag(tag);

        if(fragment == null){
            switch (checkedId){
                case R.id.main_first_bottom:
                    mFPFragment = new FirstPageFragment();
                    mTransaction.add(R.id.main_frame, mFPFragment, tag);
                    break;

                case R.id.main_second_bottom:
                    mShopsFragment = new ShopsFragment();
                    mTransaction.add(R.id.main_frame, mShopsFragment, tag);

                    break;

                case R.id.main_third_bottom:
                    mMonitorFragment = new MonitorFragment();
                    mTransaction.add(R.id.main_frame, mMonitorFragment, tag);
                    break;

                case R.id.main_fourth_bottom:
                    mMyFragment = new MyFragment();
                    mTransaction.add(R.id.main_frame, mMyFragment, tag);

                    break;
            }
        }else{
            mTransaction.show(fragment);
        }

        mTransaction.commit();

        if(!TextUtils.isEmpty(mLastTag)){
            mTransaction = mManager.beginTransaction();
            mTransaction.hide(mManager.findFragmentByTag(mLastTag));
            mTransaction.commit();
        }

        mLastTag = tag;
    }
}