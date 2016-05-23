package cn.ifingers.mytown.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by syfing on 2016/5/17.
 */
public class Preferences {
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_PREFERENCE_NAME = "townpreference";

    private Preferences(){}
    private Preferences(Context context){this.mContext = context;}

    private static Preferences mPreferences;
    private Context mContext;

    public static Preferences getInstance(Context mContext){
       synchronized (Preferences.class){
           if(mPreferences == null){
               synchronized ((Preferences.class)){
                   mPreferences = new Preferences(mContext);
               }
           }
       }
       return mPreferences;
    }

    public void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public void saveUserToken(String token) {
        saveString(KEY_USER_PASSWORD, token);
    }

    public String getUserToken() {
        return getString(KEY_USER_PASSWORD);
    }

    private void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    private SharedPreferences getSharedPreferences(){
        return mContext.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }
}
