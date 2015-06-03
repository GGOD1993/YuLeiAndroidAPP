package com.example.pc.myapplication.fragment.parent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.AppConstant;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ViewStyle.SpaceItemDecoration;
import com.example.pc.myapplication.activity.parent.ParentAddDiyTaskActivity;
import com.example.pc.myapplication.adapter.RecyclerViewItemClickListener;

import org.json.JSONArray;

public class ParentSendWishFragment extends Fragment implements
        RecyclerViewItemClickListener {

  private RecyclerView recyclerView;

  //activity端实现的接口,用于和activity通信
  private OnMsgFragmentInteractionListener mListener;

  //SharedPreference
  private SharedPreferences preferences;

  public static ParentSendWishFragment newInstance() {
    ParentSendWishFragment fragment = new ParentSendWishFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public ParentSendWishFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View w = inflater.inflate(R.layout.fragment_parent_send_wish, container, false);
    preferences = getActivity().getSharedPreferences(AppConstant.PREFERENCE_NAME,0);
    initView(w);
    return w;
  }
  private void initView(View w) {
    recyclerView = ((RecyclerView) w.findViewById(R.id.parent_sendwishfragment_recyclerview_tasktype));

    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(new ScaleRecyclerViewAdapter());
    recyclerView.addItemDecoration(new SpaceItemDecoration(150));

    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int count = recyclerView.getChildCount();
        int preViewIndex, nowViewIndex, nextViewIndex;
        View preView, nowView, nextView;
        if (count % 2 == 0) {
          nowViewIndex = count / 2;
        } else {
          nowViewIndex = (count + 1) / 2;
        }
        preViewIndex = nowViewIndex - 1;
        nextViewIndex = nowViewIndex + 1;

        preView = recyclerView.getChildAt(preViewIndex - 1);
        nowView = recyclerView.getChildAt(nowViewIndex - 1);
        nextView = recyclerView.getChildAt(nextViewIndex - 1);

        preView.setScaleY(1);
        preView.setScaleX(1);
        nextView.setScaleY(1);
        nextView.setScaleX(1);
        nowView.setScaleX(1 + 0.2f);
        nowView.setScaleY(1 + 0.2f);

      }
    });
  }

  @Override
  public void onItemClick(View view, int position) {
    switch (position) {
      case AppConstant.TASK_TYPE_SELF:
        Intent intent = new Intent(getActivity(), ParentAddDiyTaskActivity.class);
        getActivity().startActivityForResult(intent, AppConstant.PARENT_ADDDIYTASK_REQUESTCODE);
        break;
      default:
        showToast("该版块还在努力开发中...orz");
    }
  }

  class ScaleRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView imageView;
    public TextView textView;
    private RecyclerViewItemClickListener mListener;
    @Override
    public void onClick(View v) {
      if (mListener != null) {
        mListener.onItemClick(v, getPosition());
      }
    }
    public ScaleRecyclerViewHolder(View itemView, RecyclerViewItemClickListener listener) {
      super(itemView);
      mListener = listener;
      imageView = (ImageView) itemView.findViewById(R.id.layout_parent_recyclerview_tasktype_item_imageview);
      textView = (TextView) itemView.findViewById(R.id.layout_parent_recyclerviedw_tasktype_item_textview);
      itemView.setOnClickListener(this);
    }
  }
  class ScaleRecyclerViewAdapter extends RecyclerView.Adapter<ScaleRecyclerViewHolder> {
    public ScaleRecyclerViewAdapter() {
      super();
    }
    @Override
    public int getItemCount() {
      return AppConstant.ParentTaskTypeImg.length;
    }
    @Override
    public ScaleRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View v = View.inflate(parent.getContext(), R.layout.layout_parent_recyclerview_tasktype_item, null);
      ScaleRecyclerViewHolder viewHolder = new ScaleRecyclerViewHolder(v, ParentSendWishFragment.this);
      return viewHolder;
    }
    @Override
    public void onBindViewHolder(ScaleRecyclerViewHolder holder, int position) {
      holder.imageView.setImageResource(AppConstant.ParentTaskTypeImg[position]);
      holder.textView.setText(AppConstant.ParentTaskTypeStr[position]);
    }
  }
  private void showToast(String string) {
    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
  }
  public interface OnMsgFragmentInteractionListener {
    public void onMsgFragmentInteraction(JSONArray jsonArray);
  }
}
