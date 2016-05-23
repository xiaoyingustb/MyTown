package cn.ifingers.mytown.utils;

import java.security.MessageDigest;

/**
 * Created by syfing on 2016/5/13.
 */
public class MD5Utils {
    public static String getMD5(String str){
        StringBuffer sb = new StringBuffer();
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] byt = digest.digest(str.getBytes());
            for(int i = 0; i < byt.length; i++){
                int result = byt[i] & 0xff;
                String hexForm = Integer.toHexString(result);
                if(hexForm.length() < 1){
                    sb.append("0");
                }
                sb.append(result);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
