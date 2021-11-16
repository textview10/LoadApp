package com.instahela.deni.mkopo.ui.setting.message;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.bean.setting.msg.MsgResponse;
import com.instahela.deni.mkopo.bean.setting.msg.UnReadMsgResponse;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.ui.home.startload.HomeItemDecoration;
import com.instahela.deni.mkopo.ui.setting.BaseSettingFragment;
import com.instahela.deni.mkopo.ui.setting.SettingActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageFragment extends BaseSettingFragment {

    private static final String TAG = "MessageFragment";

    private RecyclerView rvMessage;

    private final ArrayList<MsgResponse.Msg> mList = new ArrayList<>();
    private MsgAdapter msgAdapter;
    private FrameLayout flEmpty;
    private ProgressBar pbLoading;
    private SmartRefreshLayout refreshLayout;

    private static final int PAGE_SIZE = 20;

    private int mPageIndex = 1;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_message, container, false);
        initializeView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.setting_message);
    }

    private void initializeView(View view) {
        rvMessage = view.findViewById(R.id.rv_setting_message);
        flEmpty = view.findViewById(R.id.fl_empty_page);
        pbLoading = view.findViewById(R.id.pb_message_loading);
        refreshLayout = view.findViewById(R.id.refresh_layout_message);
        rvMessage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        msgAdapter = new MsgAdapter(mList);
        rvMessage.setAdapter(msgAdapter);
        rvMessage.addItemDecoration(new HomeItemDecoration());
        msgAdapter.setOnItemClickListener(new MsgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, MsgResponse.Msg msg) {
                if (getActivity() instanceof SettingActivity){
                    SettingActivity settingActivity = (SettingActivity) getActivity();
                    MessageContentFragment messageContentFragment = new MessageContentFragment();
                    messageContentFragment.setMsg(msg);
                    settingActivity.addFragment(messageContentFragment, "messageContent");
                }
            }

        });
        initErrorPage(view);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                requestMessage(true);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mList.clear();
                if(msgAdapter != null){
                    msgAdapter.notifyDataSetChanged();
                }
                requestMessage(false);
            }
        });
        refreshLayout.autoRefresh(500);
    }


    private void requestMessage(boolean isLoadMore) {
        if (isDetached()){
            return;
        }
        if (!isLoadMore){
            mPageIndex = 1;
        }
        if (flEmpty != null){
            flEmpty.setVisibility(View.GONE);
        }
        hideErrorPage();
        if (pbLoading != null) {
            pbLoading.setVisibility(View.VISIBLE);
        }
        OkGo.<String>post(Api.MESSAGE_LIST_URL).tag(TAG).params("accountId", Constant.mAccountId)
                .params("page", mPageIndex).params("pageSize", PAGE_SIZE)
                .execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                if (pbLoading != null){
                    pbLoading.setVisibility(View.GONE);
                }
                if (refreshLayout != null){
                    if (isLoadMore){
                        refreshLayout.finishLoadMore();
                    } else {
                        refreshLayout.finishRefresh();
                    }
                }
                MsgResponse msgResponse = checkResponseSuccess(response, MsgResponse.class);
                if (msgResponse == null || msgResponse.getItems() == null ){
                    ToastUtils.showShort("request msg failure");
                    if (mList.size() == 0) {
                        showErrorPage();
                    }
                    return;
                }
                mList.addAll(msgResponse.getItems());
                if (mList.size() == 0){
                    if (flEmpty != null){
                        flEmpty.setVisibility(View.VISIBLE);
                    }
                }
                if (mList.size() >= PAGE_SIZE){
                    mPageIndex++;
                    if (refreshLayout != null) {
                        refreshLayout.setEnableLoadMore(true);
                    }
                } else {
                    if (refreshLayout != null) {
                        refreshLayout.setEnableLoadMore(false);
                    }
                }
                if (msgAdapter != null){
                    msgAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (pbLoading != null){
                    pbLoading.setVisibility(View.GONE);
                }
                if (refreshLayout != null){
                    if (isLoadMore){
                        refreshLayout.finishLoadMore();
                    } else {
                        refreshLayout.finishRefresh();
                    }
                }
                if (mList.size() == 0) {
                    showErrorPage();
                }
                Log.e(TAG, "request message  error ...");
            }
        });
    }

    @Override
    protected void clickErrorRetry() {
        mList.clear();
        if(msgAdapter != null){
            msgAdapter.notifyDataSetChanged();
        }
        if (refreshLayout != null) {
            refreshLayout.autoRefresh();
        }
    }

    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelAll();
        super.onDestroy();
    }
}
