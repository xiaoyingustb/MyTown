package cn.ifingers.mytown.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;

/**
 * Created by syfing on 2016/5/16.
 */
public class GeTuiReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extra = intent.getExtras();

        Log.i(TAG, "onReceive");

        switch(extra.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:
                String cid = extra.getString("clientid");
                Log.i(TAG + ": cid", String.valueOf(cid));
                break;

            case PushConsts.GET_MSG_DATA:
                String taskId = extra.getString("taskid");
                String msgId = extra.getString("messageid");

                Log.i(TAG + ": taskId", String.valueOf(taskId));
                Log.i(TAG + ": msgId", String.valueOf(msgId));

                byte[] payload = extra.getByteArray("payload");
                if(payload != null){
                    String data = new String(payload);
                    Log.i(TAG + ": data", String.valueOf(data));
                }
                break;

            default:
                break;
        }
    }
}