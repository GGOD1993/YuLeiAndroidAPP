package com.example.pc.myapplication.activity.child;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ChildSettingActivity extends SwipeBackActivity
        implements HttpService.OnUserInvitationRequestResponseListener,
        HttpService.OnGetInvitationRequestResponseListener,
        HttpService.OnAddFriendRequestResponseListener{

  private RelativeLayout addFriend;

  private SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_child_setting);

    preferences = getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    initViews();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_child_setting, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void initViews() {

    addFriend = (RelativeLayout) findViewById(R.id.addFriend);
    addFriend.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                HttpService.DoGetInvitationRequest(
                        Request.Method.GET,
                        AppConstant.GET_INVITATION_URL + "?userid=" + preferences.getString(AppConstant.FROM_USERID, ""),
                        null,
                        ChildSettingActivity.this);
              }
            });
  }

  /**
   * 设定邀请的对话框
   */
  private void showUserInviteDialog() {
    final LayoutInflater layoutInflater = LayoutInflater.from(ChildSettingActivity.this);
    final View view = layoutInflater.inflate(R.layout.layout_parent_leftmenu_dialog_userinvite, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(ChildSettingActivity.this);
    builder.setTitle("添 加 宝 贝").setView(view);
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

                  map.put(AppConstant.USERID,preferences.getString(AppConstant.FROM_USERID,""));
                  HttpService.DoUserInvitationRequest(
                          Request.Method.POST,
                          AppConstant.USER_INVITATION_URL,
                          map,
                          ChildSettingActivity.this
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
    final LayoutInflater layoutInflater = LayoutInflater.from(ChildSettingActivity.this);
    final View view = layoutInflater.inflate(R.layout.layout_parent_leftmenu_dialog_getinvite, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(ChildSettingActivity.this);
    builder.setTitle("用 户 请 求").setView(view);
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
                map.put(AppConstant.USERID, preferences.getString(AppConstant.FROM_USERID,""));
                HttpService.DoAddFriendRequest(
                        Request.Method.POST,
                        AppConstant.ADD_FRIEND_URL,
                        map,
                        ChildSettingActivity.this
                );
              }
            });
  }

  @Override
  public void OnGetInvitationSuccessResponse(JSONArray jsonArray) {

    if (null != jsonArray) {
      try {
        JSONObject object = (JSONObject) jsonArray.get(0);
        showGetInviteDialog(object.getString("parent"));
      } catch (JSONException e) {
        e.printStackTrace();
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

  private void showToast(String string) {
    Toast.makeText(ChildSettingActivity.this, string, Toast.LENGTH_SHORT).show();
  }
}
