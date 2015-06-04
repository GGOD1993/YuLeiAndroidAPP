package com.example.pc.myapplication.activity.child;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.ChildSeedInfoView;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.activity.MainActivity;
import com.example.pc.myapplication.adapter.ChildViewpagerAdapter;
import com.example.pc.myapplication.fragment.child.ChildCharityInfoFragment;
import com.example.pc.myapplication.fragment.child.ChildHistoryFragment;
import com.example.pc.myapplication.fragment.child.ChildTaskFragment;
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
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChildMainActivity extends FragmentActivity
        implements ChildTaskFragment.onChildTaskFragmentInteractionListener,
        ChildHistoryFragment.OnChildHistoryFragmentInteractionListener,
        ChildCharityInfoFragment.OnChildFuncFragmentInteractionListener,
        HttpService.OnUpLoadImageRequestResponseListener,
        HttpService.OnGetInvitationRequestResponseListener,
        HttpService.OnUserInvitationRequestResponseListener,
        HttpService.OnAddFriendRequestResponseListener,
        HttpService.OnLogoutRequestResponseListener,
        OnMenuItemClickListener,
        OnMenuItemLongClickListener{

  //再按一次返回桌面
  private Long exitTime;

  //SharedPreferences
  private SharedPreferences preferences;

  //viewpager
  private ViewPager viewPager;

  //viewpager适配器
  private ChildViewpagerAdapter mAdapter;

  //viewpager指示器
  private UnderlinePageIndicator viewPagerIndicator;

  //布局的Header
  private RelativeLayout relativeLayoutHeader;
  //Header上的TextView
  private TextView textViewHeader;
  //Header上的菜单栏
  private ImageButton imageButtonHeader;
  //Header上的CircularImage
  private CircularImage imageViewHeader;
  //存储用户更换的头像
  private Bitmap userImage;
  //ImageLoader
  private ImageLoader imageLoader;
  //用于更改头像
  private com.nostra13.universalimageloader.core.ImageLoader loader;

  //fragment列表
  private ArrayList<Fragment> fragmentList;

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
  public void onChildHistoryFragmentInteraction() {
  }

  @Override
  public void onChildTaskFragmentInteraction() {
  }

  @Override
  public void onChildFuncFragmentInteraction() {
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_child_main);
    exitTime = 0L;
    fragmentList = new ArrayList<>();
    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
    loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    loader.init(ImageLoaderConfiguration.createDefault(ChildMainActivity.this));
    imageLoader = new ImageLoader(RequestQueueController.get().getRequestQueue(), new ImageLoader.ImageCache() {
      @Override
      public Bitmap getBitmap(String s) {
        return null;
      }

      @Override
      public void putBitmap(String s, Bitmap bitmap) {
      }
    });
    initViews();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (RESULT_OK == resultCode) {
      switch (requestCode) {
        case AppConstant.SELECT_PIC:
        case AppConstant.SELECT_PIC_KITKAT:
          ImageSize targetSize = new ImageSize(USERIMAGE_WIDTH, USERIMAGE_HEIGHT);
          loader.loadImage(intent.getData().toString(), targetSize, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
              userImage = loadedImage;
              HttpService.DoUpLoadImageRequest(
                      userImage, preferences.getString(AppConstant.FROM_USERID, ""), ChildMainActivity.this);
            }
          });
          break;

        case AppConstant.CAMERA_RESULTCODE:
          Bundle bundle = intent.getExtras();
          userImage = (Bitmap) bundle.get(AppConstant.CAMERA_DATA);
          HttpService.DoUpLoadImageRequest(userImage, preferences.getString(AppConstant.FROM_USERID, ""), ChildMainActivity.this);
          break;
      }
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (KeyEvent.KEYCODE_BACK == keyCode
            && event.getAction() == KeyEvent.ACTION_DOWN) {
      if ((System.currentTimeMillis() - exitTime) > 2000) {
        showToast("亲~再点一次返回桌面");
        exitTime = System.currentTimeMillis();
      } else {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  private void initViews() {
    viewPager = (ViewPager) findViewById(R.id.child_mainactivity_viewpager);
    viewPagerIndicator = (UnderlinePageIndicator) findViewById(R.id.child_mainactivity_indicator);
    relativeLayoutHeader = (RelativeLayout) findViewById(R.id.child_mainactivity_header);
    textViewHeader = (TextView) relativeLayoutHeader.findViewById(R.id.child_mainactivity_header_textview);
    imageViewHeader = (CircularImage) relativeLayoutHeader.findViewById(R.id.child_mainactivity_header_circularimage);
    imageButtonHeader = ((ImageButton) relativeLayoutHeader.findViewById(R.id.child_mainactivity_header_imagebutton));

    initMenu();

    ChildSeedInfoView childSeedInfoView = new ChildSeedInfoView(ChildMainActivity.this);
    rapidFloatingActionButton = ((RapidFloatingActionButton) findViewById(R.id.child_mainactivity_rapidfloatingactionbutton));
    rapidFloatingActionLayout = ((RapidFloatingActionLayout) findViewById(R.id.child_mainactivity_rapidfloatingactionlayout));
    rapidFloatingActionHelper = new RapidFloatingActionHelper(ChildMainActivity.this, rapidFloatingActionLayout, rapidFloatingActionButton, childSeedInfoView).build();

    rapidFloatingActionLayout.setIsContentAboveLayout(false);
    rapidFloatingActionLayout.setDisableContentDefaultAnimation(true);

    ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageViewHeader, R.mipmap.child_funcfragment_setting, R.mipmap.ic_launcher);
    imageLoader.get(preferences.getString(AppConstant.IMG_URL, ""), imageListener);
    fragmentList.add(ChildTaskFragment.newInstance());
    fragmentList.add(ChildHistoryFragment.newInstance());
    fragmentList.add(ChildCharityInfoFragment.newInstance());
    mAdapter = new ChildViewpagerAdapter(getSupportFragmentManager(), ChildMainActivity.this, fragmentList);
    viewPager.setAdapter(mAdapter);
    viewPagerIndicator.setViewPager(viewPager, 0);
    viewPagerIndicator.setSelectedColor(getResources().getColor(R.color.skyblue));

    imageButtonHeader.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mMenuDialogFragment.show(getSupportFragmentManager(), "ContextMenuDialogFragment");
      }
    });

    imageViewHeader.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Activity activity = ChildMainActivity.this;
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

    MenuObject addFr4 = new MenuObject("Favorite");
    BitmapDrawable bd4 = new BitmapDrawable(getResources(),
            BitmapFactory.decodeResource(getResources(), R.mipmap.icn_4));
    addFr4.setDrawable(bd4);
    addFr4.setMenuTextAppearanceStyle(R.style.TextViewStyle);

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
    menuObjects.add(addFr4);
    menuObjects.add(menuLogout);

    MenuParams menuParams = new MenuParams();
    menuParams.setActionBarSize(200);
    menuParams.setMenuObjects(menuObjects);
    menuParams.setClosableOutside(true);
    // set other settings to meet your needs
    mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
  }

  @Override
  public void onMenuItemClick(View view, int i) {
    Log.e("dada", "click");
    switch (i) {
      case AppConstant.MENU_CLOSE:
        break;
      case AppConstant.MENU_ADD_FRIEND:
        HttpService.DoGetInvitationRequest(
                AppConstant.GET_INVITATION_URL + "?" + AppConstant.USERID + "=" + preferences.getString(AppConstant.FROM_USERID, ""),
                null,
                ChildMainActivity.this);
        break;
      case AppConstant.MENU_FORUM:
        showToast("该版块正在努力开发中...");

        break;
      case AppConstant.MENU_AGREE:
        showToast("该版块正在努力开发中...");
        break;
      case AppConstant.MENU_FAVORITE:
        showToast("该版块正在努力开发中...");
        break;
      case AppConstant.MENU_LOGOUT:
        preferences.edit().putInt(AppConstant.USER_MODE, 0).apply();
        HttpService.DoLogoutRequest(null, ChildMainActivity.this);
        break;
    }
  }

  @Override
  public void onMenuItemLongClick(View view, int i) {
    
  }

  /**
   * 设定邀请的对话框
   */
  private void showUserInviteDialog() {
    final LayoutInflater layoutInflater = LayoutInflater.from(ChildMainActivity.this);
    final View view = layoutInflater.inflate(R.layout.layout_parent_leftmenu_dialog_userinvite, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(ChildMainActivity.this);
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
                  HttpService.DoUserInvitationRequest(map, ChildMainActivity.this);
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
    final LayoutInflater layoutInflater = LayoutInflater.from(ChildMainActivity.this);
    final View view = layoutInflater.inflate(R.layout.layout_parent_leftmenu_dialog_getinvite, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(ChildMainActivity.this);
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
                HttpService.DoAddFriendRequest(map, ChildMainActivity.this);
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
  public void OnUpLoadImageSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject;
    JSONObject msgObject;
    try {
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
        if (AppConstant.UPLOAD_USER_IMAGE_SUCCESS == codeObject.getInt(AppConstant.RETURN_CODE)) {
          imageViewHeader.setImageBitmap(userImage);

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
        showToast(msgObject.getString(AppConstant.RETURN_MSG));
        startActivity(new Intent(ChildMainActivity.this, MainActivity.class));
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

  private void showToast(String string) {
    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
  }
}
