package com.example.pc.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class AppConstant {
  public static final int MODE_PARENT = 1;
  public static final int MODE_CHILD = 2;
  public static final String USER_MODE = "mode";

  public static final int SELECT_PIC_KITKAT = 3;
  public static final int SELECT_PIC = 4;

  public static final int CAMERA_RESULTCODE = 5;

  public static final int PARENT_ADDDIYTASK_REQUESTCODE = 6;
  public static final int PARENT_ADDDIYTASK_RESULTCODE = 7;
  public static final int PARENT_ADDSYSTEMTASK_REQUESTCODE = 8;
  public static final int PARENT_ADDSYSTEMTASK_RESULTCODE = 9;



  //孩子端控件漂浮参数
  public static final int START_MOVE = 10;
  public static final int STOP_MOVE = 11;
  public static final int MOVE_INTERVAL_TIME = 100;
  public static final int LEFT_DIRECTION = 1;
  public static final int RIGHT_DIRECTION = -1;
  public static final int UP_DIRECTION = 2;
  public static final int DOWN_DIRECTION = -2;
  public static final int TOP_SPEED = 5;
  public static final int ROTATE_TIME = 3000;

  //网络请求返回code
  public static final int NEW_USER_SUCCESS = 1105;
  public static final int LOGIN_SUCCESS = 1201;
  public static final int LOGOUT_SUCCESS = 1203;
  public static final int SET_DIY_TASK_SUCCESS = 1302;


  public static final String RETURN_MSG = "msg";
  public static final String RETURN_CODE = "code";
  public static final String FROM_USERID = "from_userid";
  public static final String TO_USERID = "to_userid";
  public static final String USERID = "userid";
  public static final String PREFERENCE_NAME = "yulei";
  public static final String CLICKED_SYSTEM_TASK = "clickedSystemTask";

  //和任务相关的关键字
  public static final String TASK_ID = "taskId";
  public static final String TASK_CONTENT = "content";
  public static final String TASK_REGDATE = "regdate";
  public static final String TASK_TO_BE_SUBMIT = "task_to_be_submit";

  //发送广播的关键字
  public static final String BROADCAST_REMOVEVIEW = "broadcast_to_removeview";



  //网络请求URL
  public static final String HOST = "http://byhands.sinaapp.com";
  public static final String NEW_USER_URL= HOST + "/Users/NewUser.php";
  public static final String LOGIN_IN_URL = HOST + "/Users/Login.php";
  public static final String LOGIN_OUT_URL = HOST + "/Users/Logout.php";
  public static final String SET_DIY_TASK_URL = HOST + "/Users/SetDiyTask.php";
  public static final String GET_SYS_TASK_URL = HOST + "/Users/GetSysTask.php";
  public static final String GET_DIY_TASK_URL = HOST + "/Users/GetDiyTask.php";
  public static final String GET_CURRENT_USER_URL = HOST + "/Users/GetCurUser.php";
  public static final String USER_INVITATION_URL = HOST + "/Users/user_invitation.php";
  public static final String GET_INVITATION_URL = HOST + "/Users/get_invitation.php";
  public static final String ADD_FRIEND_URL = HOST + "/Users/AddFriend.php";
  public static final String CHECK_DIY_TASK_URL = HOST + "/Users/CheckDiyTask.php";
  public static final String GET_COMPANY_URL = HOST + "/Users/Company.php";
  public static final String DONATE_URL = HOST + "/Users/Donate.php";
  public static final String FINISH_DIY_TASK_URL = HOST + "/Users/FinishDiyTask.php";
  public static final String GET_CHILDREN_URL = HOST + "/Users/GetChildren.php";
  public static final String GET_PARENT_URL = HOST + "/Users/GetParent.php";
  public static final String GET_USER_INFO_URL = HOST + "/Users/GetUserInfo.php";
  public static final String SUBMIT_DIY_TASK_URL = HOST + "/Users/SubmitDiyTask.php";

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
