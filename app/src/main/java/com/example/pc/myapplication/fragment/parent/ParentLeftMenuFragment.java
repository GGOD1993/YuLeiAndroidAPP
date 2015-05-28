package com.example.pc.myapplication.fragment.parent;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.CircularImage;
import com.example.pc.myapplication.activity.MainActivity;
import com.example.pc.myapplication.utils.HttpService;
import com.example.pc.myapplication.utils.RequestQueueController;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ParentLeftMenuFragment extends Fragment
        implements HttpService.OnUserInvitationRequestResponseListener,
        HttpService.OnGetInvitationRequestResponseListener,
        HttpService.OnAddFriendRequestResponseListener,
        HttpService.OnLogoutRequestResponseListener,
        HttpService.OnUpLoadImageRequestResponseListener {

  //ImageLoader
  private ImageLoader imageLoader;

  //存储数据的sharedPreferences
  private SharedPreferences preferences;

  //用户头像框
  private CircularImage circularImage;

  //用户更换的头像
  private Bitmap userImage;

  //用于更改头像
  private com.nostra13.universalimageloader.core.ImageLoader loader;

  //退出登录
  private RelativeLayout relativeLayoutLogout;

  //添加好友
  private RelativeLayout relativeLayoutAddFriends;

  //用户头像的高度
  private static final int USERIMAGE_HEIGHT = 70;

  //用户头像的宽度
  private static final int USERIMAGE_WIDTH = 70;

  //Activity返回码
  public static final int RESULT_OK = -1;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_parent_left_menu, container, false);
    preferences = getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME, 0);
    loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    loader.init(ImageLoaderConfiguration.createDefault(getActivity()));
    imageLoader = new ImageLoader(RequestQueueController.get().getRequestQueue(), new ImageLoader.ImageCache() {
      @Override
      public Bitmap getBitmap(String s) {
        return null;
      }

      @Override
      public void putBitmap(String s, Bitmap bitmap) {
      }
    });
    initView(v);
    return v;
  }

  private void initView(View v) {
    relativeLayoutLogout = (RelativeLayout) v.findViewById(R.id.parentactivity_leftmenu_relativelayout_logout);
    relativeLayoutAddFriends = (RelativeLayout) v.findViewById(R.id.parentactivity_leftmenu_relativelayout_addchild);
    circularImage = (CircularImage) v.findViewById(R.id.parentactivity_leftmenu_circularimage_userimage);

    relativeLayoutLogout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        preferences.edit().putInt(AppConstant.USER_MODE, 0).apply();
        HttpService.DoLogoutRequest(Request.Method.GET, AppConstant.LOGIN_OUT_URL, null, ParentLeftMenuFragment.this);
      }
    });

    relativeLayoutAddFriends.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        HttpService.DoGetInvitationRequest(
                Request.Method.GET,
                AppConstant.GET_INVITATION_URL + "?" + AppConstant.USERID + "=" + preferences.getString(AppConstant.FROM_USERID, ""),
                null,
                ParentLeftMenuFragment.this);
      }
    });

    ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(circularImage, R.mipmap.child_funcfragment_setting, R.mipmap.ic_launcher);
    imageLoader.get(preferences.getString(AppConstant.IMG_URL, ""), imageListener);
    circularImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Activity activity = getActivity();
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
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
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
   * 设定邀请的对话框
   */
  private void showUserInviteDialog() {
    final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    final View view = layoutInflater.inflate(R.layout.layout_parent_leftmenu_dialog_userinvite, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                  HttpService.DoUserInvitationRequest(
                          Request.Method.POST,
                          AppConstant.USER_INVITATION_URL,
                          map,
                          ParentLeftMenuFragment.this
                  );
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
    final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    final View view = layoutInflater.inflate(R.layout.layout_parent_leftmenu_dialog_getinvite, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                HttpService.DoAddFriendRequest(
                        Request.Method.POST,
                        AppConstant.ADD_FRIEND_URL,
                        map,
                        ParentLeftMenuFragment.this
                );
              }
            });
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
              HttpService.DoUpLoadImageRequest(Request.Method.POST, AppConstant.UPLOAD_USER_IMAGE,
                      userImage, preferences.getString(AppConstant.FROM_USERID, ""), ParentLeftMenuFragment.this);
            }
          });
          break;

        case AppConstant.CAMERA_RESULTCODE:
          Bundle bundle = intent.getExtras();
          userImage = (Bitmap) bundle.get(AppConstant.CAMERA_DATA);
          HttpService.DoUpLoadImageRequest(Request.Method.POST, AppConstant.UPLOAD_USER_IMAGE,
                  userImage, preferences.getString(AppConstant.FROM_USERID, ""), ParentLeftMenuFragment.this);
          break;
      }
    }
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
  public void OnUserInvitationSuccessResponse(JSONArray jsonArray) {
    showToast(jsonArray.toString());
  }

  @Override
  public void OnUserInvitationErrorResponse(String errorResult) {
    showToast(errorResult);
  }

  @Override
  public void OnAddFriendSuccessResponse(JSONArray jsonArray) {
    showToast(jsonArray.toString());
  }

  @Override
  public void OnAddFriendErrorResponse(String errorResult) {
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
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
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
  public void OnUpLoadImageSuccessResponse(JSONArray jsonArray) {
    JSONObject codeObject;
    JSONObject msgObject;
    try {
      codeObject = (JSONObject) jsonArray.get(0);
      msgObject = (JSONObject) jsonArray.get(1);
      if (null != codeObject) {
        if (AppConstant.UPLOAD_USER_IMAGE_SUCCESS == codeObject.getInt(AppConstant.RETURN_CODE)) {
          circularImage.setImageBitmap(userImage);
          userImage = null;
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

  private void showToast(String string) {
    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
  }
}
