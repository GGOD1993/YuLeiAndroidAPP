package com.example.pc.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AppConstant {
    public static final int MODE_PARENT = 1;
    public static final int MODE_CHILD = 2;
    public static final String USER_MODE = "mode";
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 0;

    public static final int SELECT_PIC_KITKAT = 2;
    public static final int SELECT_PIC = 4;

    public static final int CAMERA_RESULTCODE = 5;
    public static final String CAMERA_DATA = "data";

    public static final int PARENT_ADDDIYTASK_REQUESTCODE = 6;
    public static final int PARENT_ADDDIYTASK_RESULTCODE = 7;

    //网络请求返回code
    public static final int NEW_USER_SUCCESS = 1105;
    public static final int USERNAME_PASSWORD_WRONG = 1200;
    public static final int LOGIN_SUCCESS = 1201;
    public static final int LOGIN_FAILED = 1202;
    public static final int LOGOUT_SUCCESS = 1203;
    public static final int LOGOUT_FAILED = 1204;
    public static final int SET_DIY_TASK_SUCCESS = 1302;
    public static final int SUBMIT_DIY_TASK_SUCCESS = 1306;
    public static final int FINISH_TASK_SUCCESS = 1310;
    public static final int SIGNUP_SUCCESS = 1105;
    public static final int UPLOAD_USER_IMAGE_SUCCESS = 1501;
    public static final int UPLOAD_USER_IMAGE_FAILED = 1502;
    public static final int EVERYDAY_SIGN_IN_SUCCESS = 1601;
    public static final int EVERYDAY_SIGN_IN_FAILED = 1602;

    public static final String AUTO_SIGNIN = "autosignin";
    public static final String MEMORY_PASSWORD = "memorypassword";
    public static final String MONEY = "money";
    public static final String DONATE_MONEY = "donate_money";
    public static final String COMPANY = "company";
    public static final String COMPANY_IMG = "company_img";
    public static final String COMPANY_CONTACT = "company_contact";
    public static final String SEEDS = "seeds";
    public static final String EVERYDAY_TASK = "everytaskdonetime";
    public static final String EVERYDAY_TASK_SUCCESS = "签到成功，奖励50金币O(∩_∩)O~~~";
    public static final String EVERYDAY_TASK_FAILD = "您今天已经签到过咯~~~~(>_<)~~~~ ";
    public static final String RETURN_MSG = "msg";
    public static final String RETURN_CODE = "code";
    public static final String FROM_USERID = "from_userid";
    public static final String TO_USERID = "to_userid";
    public static final String USERNAME = "username";
    public static final String USERID = "userid";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String NICKNAME = "nickname";
    public static final String AWARD = "award";
    public static final String GENDER = "gender";
    public static final String CHILD = "child";
    public static final String PARENT = "parent";
    public static final String USER_IMAGE = "user_image";
    public static final String IMG_URL = "imgurl";
    public static final String AUTO_SIGNIN_USERNAME = "auto_signin_username";
    public static final String AUTO_SIGNIN_PASSWORD = "auto_signin_password";

    //和任务相关的关键字
    public static final int SEND_TASK_TYPE = 1;
    public static final int RECIVE_TASK_TYPE = 2;
    public static final int STATUS_NEW = 0;
    public static final int STATUS_SUBMITTED = 1;
    public static final int STATUS_FINISHED = 2;
    public static final String STATUS_NEW_STRING = "未提交";
    public static final String STATUS_SUBMITTED_STRING = "已提交";
    public static final String STATUS_FINISHED_STRING = "已审核";
    public static final String TASK_STATUS = "taskstatus";
    public static final String TASK_ID = "taskId";
    public static final String TASK_CONTENT = "content";
    public static final String TASK_AWARD = "award";
    public static final String NEW_TASK = "newTask";

    //发送广播的关键字
    public static final String BROADCAST_CHANGE_VIEW_BG = "broadcast_change_view_bg";
    public static final String BROADCAST_MOVE_TO_WISH_BAG = "broadcast_to_wish_bag";

    //preference关键字
    public static final String PREFERENCE_NAME = "yulei";
    public static final String PREFERENCE_LINKED_CHILDREN = "preference_linked_children";
    public static final String PREFERENCE_LINKED_PARENT = "preference_linked_parent";

    //网络请求URL
    public static final String HOST = "http://byhands.sinaapp.com";
    public static final String IMG_HOST = "http://byhands-usersimg.stor.sinaapp.com/";
    public static final String NEW_USER_URL = HOST + "/Users/NewUser.php";
    public static final String LOGIN_IN_URL = HOST + "/Users/Login.php";
    public static final String LOGIN_OUT_URL = HOST + "/Users/Logout.php";
    public static final String SET_DIY_TASK_URL = HOST + "/Users/SetDiyTask.php";
    public static final String GET_SYS_TASK_URL = HOST + "/Users/GetSysTask.php";
    public static final String GET_DIY_TASK_URL = HOST + "/Users/GetDiyTask.php";
    public static final String GET_SEND_DIY_TASK_URL = HOST + "/Users/GetSendDiyTask.php";
    public static final String GET_CURRENT_USER_URL = HOST + "/Users/GetCurUser.php";
    public static final String USER_INVITATION_URL = HOST + "/Users/user_invitation.php";
    public static final String GET_INVITATION_URL = HOST + "/Users/get_invitation.php";
    public static final String ADD_FRIEND_URL = HOST + "/Users/AddFriend.php";
    public static final String GET_CHARITY_URL = HOST + "/Users/GetCharity.php";
    public static final String FINISH_DIY_TASK_URL = HOST + "/Users/FinishDiyTask.php";
    public static final String GET_CHILDREN_URL = HOST + "/Users/GetChildren.php";
    public static final String GET_USER_INFO_URL = HOST + "/Users/GetUserInfo.php";
    public static final String SUBMIT_DIY_TASK_URL = HOST + "/Users/SubmitDiyTask.php";
    public static final String UPLOAD_USER_IMAGE = HOST + "/Users/UploadUserImage.php";
    public static final String GET_TASK_BY_STATUS = HOST + "/Users/GetTaskByStatus.php";
    public static final String EVERYDAY_SIGN_IN_URL = HOST + "/Users/EveryDaySignIn.php";
    public static final String GET_SEED_INFO_URL = HOST + "/Users/GetSeedInfo.php";
    public static final String GET_DONATE_DATA_URL = HOST + "/Users/GetDonateData.php";

    public static final int[] ParentTaskTypeImg = {
            R.mipmap.img_parent_tasktype_animal,
            R.mipmap.img_parent_tasktype_self,
            R.mipmap.img_parent_tasktype_book,
            R.mipmap.img_parent_tasktype_random,
            R.mipmap.img_parent_tasktype_music};
    public static final String[] ParentTaskTypeStr = {"动 物", "自定义", "书 籍", "随 机", "音 乐"};
    public static final int TASK_TYPE_ANIMAL = 0;
    public static final int TASK_TYPE_SELF = 1;
    public static final int TASK_TYPE_BOOK = 2;
    public static final int TASK_TYPE_RANDOM = 3;
    public static final int TASK_TYPE_MUSIC = 4;

    //菜单选项
    public static final int MENU_CLOSE = 0;
    public static final int MENU_ADD_FRIEND = 1;
    public static final int MENU_FORUM = 2;
    public static final int MENU_AGREE = 3;
    public static final int MENU_LOGOUT = 4;

    //慈善机构信息关键字
    public static final String CHARITY_NAME = "charity_name";
    public static final String CHARITY_IMG_URL = "charity_img_url";
    public static final String CHARITY_BIREF = "charity_brief";
    public static final String CHARITY_CONTACT = "charity_contact";
    public static final String CHARITY_ADDRESS = "charity_address";

    /**
     * 流弊的一个函数
     * 大量减少内存的占用
     *
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
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * Bitmap转字节数组的方法
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
