package com.example.pc.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class AppConstant {
    public static final int MODE_PARENT = 1;
    public static final int MODE_CHILD = 2;

    public static final int SELECT_PIC_KITKAT = 3;
    public static final int SELECT_PIC = 4;

    public static final int CAMERA_RESULTCODE = 5;

    public static final int PARENT_ADDDIYTASK_REQUESTCODE = 6;
    public static final int PARENT_ADDDIYTASK_RESULTCODE = 7;
    public static final int PARENT_ADDSYSTEMTASK_REQUESTCODE = 8;
    public static final int PARENT_ADDSYSTEMTASK_RESULTCODE = 9;

    public static final int NEW_USER_SUCCESS = 1105;
    public static final int LOGIN_SUCCESS = 1201;
    public static final int LOGOUT_SUCCESS = 1203;
    public static final int SET_DIY_TASK_SUCCESS = 1302;

    public static String COOKIE = "";

    public static final String FROM_USERID = "from_userid";
    public static final String PREFERENCE_NAME = "yulei";
    public static final String CLICKED_SYSTEM_TASK = "clickedSystemTask";

    public static final String NEW_USER_URL= "http://byhands.sinaapp.com/Users/NewUser.php";
    public static final String LOGIN_IN_URL = "http://byhands.sinaapp.com/Users/Login.php";
    public static final String LOGIN_OUT_URL = "http://byhands.sinaapp.com/Users/Logout.php";

    public static final String SET_DIY_TASK_URL = "http://byhands.sinaapp.com/Users/SetDiyTask.php";

    public static final String GET_SYS_TASK_URL = "http://byhands.sinaapp.com/Users/GetSysTask.php";
    public static final String GET_DIY_TASK_URL = "http://byhands.sinaapp.com/Users/GetDiyTask.php";


    public static final String GET_CURRENT_USER_URL = "http://byhands.sinaapp.com/Users/GetCurUser.php";

    /**
     * 流弊的一个函数
     * 大量减少内存的占用
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }
}
