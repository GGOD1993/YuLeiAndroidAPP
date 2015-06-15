package com.example.pc.myapplication.activity.parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.Infos.DiyTaskInfo;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.SeedInfoView;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.activity.MainActivity;
import com.example.pc.myapplication.adapter.ParentViewPagerAdapter;
import com.example.pc.myapplication.fragment.parent.ParentCharityInfoFragment;
import com.example.pc.myapplication.fragment.parent.ParentSendWishFragment;
import com.example.pc.myapplication.fragment.parent.ParentWishListFragment;
import com.example.pc.myapplication.utils.HttpService;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.UnderlinePageIndicator;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParentMainActivity extends FragmentActivity implements
        ParentWishListFragment.OnBabyFragmentInteractionListener,
        ParentSendWishFragment.OnMsgFragmentInteractionListener,
        ParentCharityInfoFragment.OnDynamicFragmentInteractionListener,
        HttpService.OnUpLoadImageRequestResponseListener,
        HttpService.OnSetDiyTaskRequestResponseListener,
        HttpService.OnGetInvitationRequestResponseListener,
        HttpService.OnUserInvitationRequestResponseListener,
        HttpService.OnAddFriendRequestResponseListener,
        HttpService.OnLogoutRequestResponseListener,
        HttpService.OnEveryDaySignInRequestResponseListener,
        OnMenuItemClickListener {

    //记录连续按两次退出
    private long exitTime;

    //SharedPreferences
    private SharedPreferences preferences;

    //侧滑控件
    private ViewPager viewPager;
    //ViewPager适配器
    private ParentViewPagerAdapter adapter;

    //viewpager指示器
    private UnderlinePageIndicator viewPagerIndicator;

    //布局的Header
    private RelativeLayout relativeLayoutHeader;
    //菜单栏按钮
    private ImageView imageViewMenu;
    //头像框
    private CircularImage circularImage;
    //记录签到的当前时间
    private String nowTime;
    //每日签到按钮
    private ImageView imageViewEverydayTask;
    //存储用户更换的头像
    private Bitmap userImage;
    //ImageLoader
    private ImageLoader imageLoader;
    //用于更改头像
    private com.nostra13.universalimageloader.core.ImageLoader loader;

    //新增任务引用
    private DiyTaskInfo newTask;

    //存放ViewPager上显示的fragment
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    //弹出式菜单
    ContextMenuDialogFragment mMenuDialogFragment;

    //fab相关的布局
    private RapidFloatingActionLayout rapidFloatingActionLayout;
    private RapidFloatingActionButton rapidFloatingActionButton;
    private RapidFloatingActionHelper rapidFloatingActionHelper;

    //用户头像的高度
    private static final int USERIMAGE_HEIGHT = 35;
    //用户头像的宽度
    private static final int USERIMAGE_WIDTH = 35;

    @Override
    public void onDynamicFragmentInteraction() {
    }

    @Override
    public void onBabyFragmentInteraction() {
    }

    @Override
    public void onMsgFragmentInteraction(JSONArray jsonArray) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);
        exitTime = 0;
        Time t = new Time();
        t.setToNow();
        int lastmonth = t.month + 1;
        nowTime = t.year + "年" + lastmonth + "月" + t.monthDay + "日";
        preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
        loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        loader.init(ImageLoaderConfiguration.createDefault(ParentMainActivity.this));
        imageLoader = new ImageLoader(RequestQueueController.get().getRequestQueue(), new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
            }
        });
        initView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case AppConstant.SELECT_PIC:
                    case AppConstant.SELECT_PIC_KITKAT:
                        ImageSize targetSize = new ImageSize(USERIMAGE_WIDTH, USERIMAGE_HEIGHT);
                        loader.loadImage(intent.getData().toString(), targetSize, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                userImage = loadedImage;
                                HttpService.DoUpLoadImageRequest(userImage, preferences.getString(AppConstant.FROM_USERID, ""), ParentMainActivity.this);
                            }
                        });
                        break;
                    case AppConstant.CAMERA_RESULTCODE:
                        Bundle bundle = intent.getExtras();
                        userImage = (Bitmap) bundle.get(AppConstant.CAMERA_DATA);
                        HttpService.DoUpLoadImageRequest(userImage, preferences.getString(AppConstant.FROM_USERID, ""), ParentMainActivity.this);
                        break;
                }
                break;
            case AppConstant.PARENT_ADDDIYTASK_RESULTCODE:
                if (intent != null) {
                    newTask = intent.getParcelableExtra(AppConstant.NEW_TASK);
                    addNewTask(newTask);
                }
                break;
        }
    }

    /**
     * 添加新任务
     *
     * @param newTask
     */
    private void addNewTask(DiyTaskInfo newTask) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstant.FROM_USERID, preferences.getString(AppConstant.FROM_USERID, ""));
        map.put(AppConstant.TASK_CONTENT, newTask.getTaskContent());
        map.put(AppConstant.TO_USERID, newTask.getToUserId());
        map.put(AppConstant.AWARD, newTask.getAward());
        HttpService.DoSetDiyTaskRequest(map, ParentMainActivity.this);
    }

    private void initView() {
        viewPagerIndicator = (UnderlinePageIndicator) findViewById(R.id.parent_mainactivity_indicator);
        viewPager = (ViewPager) findViewById(R.id.parent_mainactivity_viewpager);
        relativeLayoutHeader = ((RelativeLayout) findViewById(R.id.parent_mainactivity_header));
        imageViewMenu = ((ImageView) relativeLayoutHeader.findViewById(R.id.parent_mainactivity_header_imageview_menu));
        circularImage = ((CircularImage) relativeLayoutHeader.findViewById(R.id.parent_mainactivity_header_circularimage));
        imageViewEverydayTask = ((ImageView) relativeLayoutHeader.findViewById(R.id.parent_mainactivity_imageview_everydaytask));
        adapter = new ParentViewPagerAdapter(ParentMainActivity.this, getSupportFragmentManager(), fragmentList);

        initMenu();

        SeedInfoView seedInfoView = new SeedInfoView(ParentMainActivity.this);
        rapidFloatingActionButton = ((RapidFloatingActionButton) findViewById(R.id.parent_mainactivity_rapidfloatingactionbutton));
        rapidFloatingActionLayout = ((RapidFloatingActionLayout) findViewById(R.id.parent_mainactivity_rapidfloatingactionlayout));
        rapidFloatingActionHelper = new RapidFloatingActionHelper(ParentMainActivity.this, rapidFloatingActionLayout, rapidFloatingActionButton, seedInfoView).build();

        rapidFloatingActionLayout.setIsContentAboveLayout(false);
        rapidFloatingActionLayout.setDisableContentDefaultAnimation(true);

        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(circularImage, R.mipmap.child_funcfragment_setting, R.mipmap.ic_launcher);
        imageLoader.get(preferences.getString(AppConstant.IMG_URL, ""), imageListener);
        fragmentList.add(ParentSendWishFragment.newInstance());
        fragmentList.add(ParentWishListFragment.newInstance());
        fragmentList.add(ParentCharityInfoFragment.newInstance());
        viewPager.setAdapter(adapter);
        viewPagerIndicator.setViewPager(viewPager, 0);
        viewPagerIndicator.setSelectedColor(getResources().getColor(R.color.skyblue));
        if (nowTime.equals(preferences.getString(AppConstant.EVERYDAY_TASK, ""))) {
            imageViewEverydayTask.setImageResource(R.mipmap.parentactivityeveryday_task_done);
        } else imageViewEverydayTask.setImageResource(R.mipmap.parentactivityeveryday_task_normal);

        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDialogFragment.show(getSupportFragmentManager(), "ContextMenuDialogFragment");
            }
        });

        circularImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity activity = ParentMainActivity.this;
                LayoutInflater layoutInflater = LayoutInflater.from(activity);
                View viewAddEmplyee = layoutInflater.inflate(R.layout.layout_signup_imagechooser, null);
                new AlertDialog.Builder(activity).setTitle("更 换 头 像").setView(viewAddEmplyee).show();
                viewAddEmplyee.findViewById(R.id.signup_imagechooser_textview_shot).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, AppConstant.CAMERA_RESULTCODE);
                            }
                        }
                );
                viewAddEmplyee.findViewById(R.id.signup_imagechooser_textview_album).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //打开手机自带的图库，选择图片后将URI返回到onActivityResult
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
                                    startActivityForResult(intent, AppConstant.SELECT_PIC_KITKAT);
                                else startActivityForResult(intent, AppConstant.SELECT_PIC);
                            }
                        }
                );
            }
        });

        imageViewEverydayTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String everyDayTaskDoneTime = preferences.getString(AppConstant.EVERYDAY_TASK, "");
                if (!nowTime.equals(everyDayTaskDoneTime)) {
                    HttpService.DoEveryDaySignInRequest(ParentMainActivity.this);
                } else
                    showToast(AppConstant.EVERYDAY_TASK_FAILD);
            }
        });
    }

    /**
     * 初始化菜单栏
     */
    private void initMenu() {
        MenuObject menuClose = new MenuObject("Close Menu");
        BitmapDrawable closeBd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.icn_close));
        menuClose.setDrawable(closeBd);
        menuClose.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject menuAddFr = new MenuObject("Add Friend");
        BitmapDrawable addFrBd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.icn_add_friend));
        menuAddFr.setDrawable(addFrBd);
        menuAddFr.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject addFr1 = new MenuObject("Forum");
        BitmapDrawable bd1 = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.icn_1));
        addFr1.setDrawable(bd1);
        addFr1.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject addFr2 = new MenuObject("Agree");
        BitmapDrawable bd2 = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.icn_2));
        addFr2.setDrawable(bd2);
        addFr2.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject menuLogout = new MenuObject("Logout");
        BitmapDrawable logoutBd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.icn_4));
        menuLogout.setDrawable(logoutBd);
        menuLogout.setMenuTextAppearanceStyle(R.style.TextViewStyle);


        List<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(menuClose);
        menuObjects.add(menuAddFr);
        menuObjects.add(addFr1);
        menuObjects.add(addFr2);
        menuObjects.add(menuLogout);

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize(200);
        menuParams.setMenuObjects(menuObjects);
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    @Override
    public void onMenuItemClick(View view, int i) {
        switch (i) {
            case AppConstant.MENU_CLOSE:
                break;
            case AppConstant.MENU_ADD_FRIEND:
                HttpService.DoGetInvitationRequest(
                        AppConstant.GET_INVITATION_URL + "?" + AppConstant.USERID + "=" + preferences.getString(AppConstant.FROM_USERID, ""),
                        null,
                        ParentMainActivity.this);
                break;
            case AppConstant.MENU_FORUM:
                showToast("该版块正在努力开发中...");
                break;
            case AppConstant.MENU_AGREE:
                showToast("该版块正在努力开发中...");
                break;
            case AppConstant.MENU_LOGOUT:
                HttpService.DoLogoutRequest(null, ParentMainActivity.this);
                break;
        }
    }

    /**
     * 设定邀请的对话框
     */
    private void showUserInviteDialog() {
        final LayoutInflater layoutInflater = LayoutInflater.from(ParentMainActivity.this);
        final View view = layoutInflater.inflate(R.layout.layout_parent_leftmenu_dialog_userinvite, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ParentMainActivity.this);
        builder.setView(view);
        ((TextView) view.findViewById(R.id.parent_leftmenu_dialog_userinvite_textview_header)).setText("添 加 好 友");
        final AlertDialog dialog = builder.create();
        dialog.show();
        view.findViewById(R.id.parent_leftmenu_imagebutton_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        view.findViewById(R.id.parent_leftmenu_imagebutton_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) view.findViewById(R.id.parent_leftmenu_edittext_tousername);
                        if (0 != editText.getText().length()) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(AppConstant.TO_USERID, editText.getText().toString());
                            map.put(AppConstant.USERID, preferences.getString(AppConstant.FROM_USERID, ""));
                            HttpService.DoUserInvitationRequest(map, ParentMainActivity.this);
                        } else {
                            showToast("请输入正确的用户名");
                        }
                    }
                });
    }

    /**
     * 获取邀请的对话框
     */
    private void showGetInviteDialog(final String parent) {
        final LayoutInflater layoutInflater = LayoutInflater.from(ParentMainActivity.this);
        final View view = layoutInflater.inflate(R.layout.layout_parent_leftmenu_dialog_getinvite, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ParentMainActivity.this);
        builder.setView(view);
        ((TextView) view.findViewById(R.id.parent_leftmenu_dialog_textview_header)).setText("好 友 请 求");
        ((TextView) view.findViewById(R.id.parent_leftmenu_dialog_textview_inviteinfo))
                .setText("用户名为: \"" + parent + "\" 的用户请求和您绑定");
        final AlertDialog dialog = builder.create();
        dialog.show();
        view.findViewById(R.id.parent_leftmenu_dialog_imagebutton_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showUserInviteDialog();
                    }
                });

        view.findViewById(R.id.parent_leftmenu_dialog_imagebutton_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(AppConstant.TO_USERID, parent);
                        map.put(AppConstant.USERID, preferences.getString(AppConstant.FROM_USERID, ""));
                        HttpService.DoAddFriendRequest(map, ParentMainActivity.this);
                    }
                });
    }


    @Override
    public void OnAddFriendSuccessResponse(JSONArray jsonArray) {
        JSONObject codeObject;
        JSONObject msgObject;
        try {
            codeObject = (JSONObject) jsonArray.get(0);
            msgObject = (JSONObject) jsonArray.get(1);
            if (null != codeObject) {
                showToast(msgObject.getString(AppConstant.RETURN_MSG));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnAddFriendErrorResponse(String errorResult) {
        showToast(errorResult);
    }

    @Override
    public void OnUserInvitationSuccessResponse(JSONArray jsonArray) {
        JSONObject codeObject;
        JSONObject msgObject;
        try {
            codeObject = (JSONObject) jsonArray.get(0);
            msgObject = (JSONObject) jsonArray.get(1);
            if (null != codeObject) {
                showToast(msgObject.getString(AppConstant.RETURN_MSG));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnUserInvitationErrorResponse(String errorResult) {
        showToast(errorResult);
    }

    @Override
    public void OnGetInvitationSuccessResponse(JSONArray jsonArray) {
        if (null != jsonArray) {
            try {
                JSONObject object = (JSONObject) jsonArray.get(0);
                showGetInviteDialog(object.getString("parent"));
            } catch (JSONException e) {
                e.printStackTrace();
                showUserInviteDialog();
            }
        } else {
            showUserInviteDialog();
        }

    }

    @Override
    public void OnGetInvitationErrorResponse(String errorResult) {
        showToast(errorResult);
        showUserInviteDialog();
    }

    @Override
    public void OnSetDiyTaskSuccessResponse(JSONArray jsonArray) {
        JSONObject codeObject;
        JSONObject msgObject;
        try {
            codeObject = (JSONObject) jsonArray.get(0);
            msgObject = (JSONObject) jsonArray.get(1);
            if (null != codeObject) {
                if (AppConstant.SET_DIY_TASK_SUCCESS == codeObject.getInt(AppConstant.RETURN_CODE)) {
                    ParentWishListFragment fragment = (ParentWishListFragment) fragmentList.get(1);
                    fragment.addTaskToList(newTask);
                }
            }
            if (null != msgObject) showToast(msgObject.getString(AppConstant.RETURN_MSG));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnSetDiyTaskErrorResponse(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void OnUpLoadImageSuccessResponse(JSONArray jsonArray) {
        JSONObject codeObject;
        JSONObject msgObject;
        try {
            codeObject = (JSONObject) jsonArray.get(0);
            msgObject = (JSONObject) jsonArray.get(1);
            if (null != codeObject) {
                if (AppConstant.UPLOAD_USER_IMAGE_SUCCESS == codeObject.getInt(AppConstant.RETURN_CODE)) {
                    circularImage.setImageBitmap(userImage);

                }
            }
            if (null != msgObject) {
                showToast(msgObject.getString(AppConstant.RETURN_MSG));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnUpLoadImageErrorResponse(String errorResult) {
        showToast(errorResult);
    }

    @Override
    public void OnLogoutSuccessResponse(JSONArray successJsonArray) {
        try {
            JSONObject codeObject = (JSONObject) successJsonArray.get(0);
            JSONObject msgObject = (JSONObject) successJsonArray.get(1);
            int code = codeObject.getInt(AppConstant.RETURN_CODE);
            if (AppConstant.LOGOUT_SUCCESS == code) {
                preferences.edit().putInt(AppConstant.USER_MODE, 0).apply();
                showToast(msgObject.getString(AppConstant.RETURN_MSG));
                startActivity(new Intent(ParentMainActivity.this, MainActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnLogoutErrorResponse(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void OnEveryDaySignInSuccessResponse(JSONArray jsonArray) {
        try {
            JSONObject codeObject = (JSONObject) jsonArray.get(0);
            JSONObject msgObject = (JSONObject) jsonArray.get(1);
            int code = codeObject.getInt(AppConstant.RETURN_CODE);
            if (AppConstant.EVERYDAY_SIGN_IN_SUCCESS == code) {
                imageViewEverydayTask.setImageResource(R.mipmap.parentactivityeveryday_task_done);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(AppConstant.EVERYDAY_TASK, true).apply();
                editor.putString(AppConstant.EVERYDAY_TASK, nowTime).apply();
                showToast(msgObject.getString(AppConstant.RETURN_MSG));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnEveryDaySignInFailedResponse(String errorResult) {
        showToast(errorResult);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("亲~再点一次返回桌面");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
//                Intent i = new Intent(Intent.ACTION_MAIN);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addCategory(Intent.CATEGORY_HOME);
//                startActivity(i);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
}
