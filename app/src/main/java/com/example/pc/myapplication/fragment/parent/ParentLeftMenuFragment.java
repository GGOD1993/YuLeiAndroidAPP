package com.example.pc.myapplication.fragment.parent;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.activity.parent.ParentMoreSettingActivity;
import com.example.pc.myapplication.utils.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ParentLeftMenuFragment extends Fragment
        implements HttpService.OnUserInvitationRequestResponseListener,
                   HttpService.OnGetInvitationRequestResponseListener{

  private SharedPreferences preferences;

  private RelativeLayout parentactivity_leftmenu_relativelayout_moresetting;
  private RelativeLayout parentactivity_leftmenu_relativelayout_addchild;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState)
  {

    View v = inflater.inflate(R.layout.fragment_parent_left_menu, container, false);
    preferences = getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    initView(v);
    return v;
  }

  private void initView(View v) {

    parentactivity_leftmenu_relativelayout_moresetting = (RelativeLayout)
            v.findViewById(R.id.parentactivity_leftmenu_relativelayout_moresetting);
    parentactivity_leftmenu_relativelayout_moresetting
            .setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                startActivity(new Intent(getActivity(), ParentMoreSettingActivity.class));
              }
            });

    parentactivity_leftmenu_relativelayout_addchild = (RelativeLayout)
            v.findViewById(R.id.parentactivity_leftmenu_relativelayout_addchild);
    parentactivity_leftmenu_relativelayout_addchild
            .setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                HttpService.DoGetInvitationRequest(
                        Request.Method.GET,
                        AppConstant.GET_INVITATION_URL + "?userid=" + preferences.getString(AppConstant.FROM_USERID,""),
                        null,
                        ParentLeftMenuFragment.this);

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
                  map.put(AppConstant.USERID, getActivity().
                          getSharedPreferences(AppConstant.PREFERENCE_NAME,0).getString(AppConstant.FROM_USERID,""));
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
  private void showGetInviteDialog(String parent) {
    final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    final View view = layoutInflater.inflate(R.layout.layout_parent_leftmenu_dialog_getinvite, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

  private void showToast(String string) {
    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
  }
}
