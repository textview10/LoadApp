package com.instahela.deni.mkopo.ui.home.loadresult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.bean.main.LoanDetailResponse;
import com.instahela.deni.mkopo.bean.main.RepayBean;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.ui.MainActivity;
import com.instahela.deni.mkopo.ui.home.LoadStatus;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LoadResultFragment extends BaseFragment {

    private static final String TAG = "LoadResultFragment";

    private final ArrayList<LoanDetailResponse.Stage> mList = new ArrayList<>();
    private LoadResultAdapter mAdapter;
    private TextView tvRepaymentAll;

    private  @LoadStatus.LoanStatus String mStatus;
    private String mOrderId;
    private final LoanDetailResponse.Stage allStage = new LoanDetailResponse.Stage();
    private TextView tvTotal, tvDate;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_result, container, false);
        initializeView(view);
        initializeData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).setTitle(getResources().getString(
                    R.string.load_result_title_repayment));
        }
    }

    private void initializeView(View view) {
        tvTotal = view.findViewById(R.id.tv_load_result_total);
        tvDate = view.findViewById(R.id.tv_load_result_date);
        tvRepaymentAll = view.findViewById(R.id.tv_load_result_repayment);

        RecyclerView rvLoadResult = view.findViewById(R.id.rv_load_result_detail);
        rvLoadResult.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new LoadResultAdapter(mList);
        rvLoadResult.setAdapter(mAdapter);
        updateData();

        mAdapter.setOnItemClickListener(new LoadResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LoanDetailResponse.Stage stage, int pos, boolean isLast) {
                String stageNo = stage.getStageNo();
                double amount = stage.getTotalAmount();
                if (!checkClickFast()){
                    requestRepayUrl(stageNo, amount);
                }
            }
        });
    }

    private void initializeData() {
        tvRepaymentAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkClickFast()) {
                    requestRepayUrl("0", allStage.getTotalAmount());
                }
            }
        });
    }

    private void updateData() {
        if (!CollectionUtils.isEmpty(mList)){
            String allTime = "";
            long allFee = 0;
            long allInterest = 0;
            long allAmount = 0;
            long allTotalAmount = 0;
            for (int i = 0; i < mList.size(); i++){
                LoanDetailResponse.Stage stage = mList.get(i);
                if (i == 0) {
                    allTime = stage.getDisburseTime();
                }
                allFee += stage.getFee();
                allInterest += stage.getInterest();
                allAmount += stage.getAmount();
                allTotalAmount += stage.getTotalAmount();
            }
            allStage.setDisburseTime(allTime);
            allStage.setFee(allFee);
            allStage.setInterest(allInterest);
            allStage.setAmount(allAmount);
            allStage.setTotalAmount(allTotalAmount);
            allStage.setStageNo("0");
            if (tvTotal != null){
                tvTotal.setText("N: " + allTotalAmount);
            }
            if (tvDate != null){
                tvDate.setText(String.valueOf(allTime));
            }
        }
    }

    public void setLoanDetail(ArrayList<LoanDetailResponse.Stage> list, @LoadStatus.LoanStatus String status,
                                String order){
        this.mStatus = status;
        this.mOrderId = order;
        mList.clear();
        if (list != null) {
            mList.addAll(list);
        }
        if (mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        updateData();
    }

    private void requestRepayUrl(String stageNo, double amount){
        OkGo.<String>post(Api.LOAN_REPAY_URL).
                params("accountId", Constant.mAccountId).
                params("orderId", mOrderId).
                params("amount", amount).
                params("stageNo", stageNo).
                tag(TAG).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e(TAG, response.body());
                RepayBean repayBean = checkResponseSuccess(response, RepayBean.class);
                if (repayBean == null){
                    return;
                }
                if (getActivity() instanceof MainActivity){
                    ((MainActivity) getActivity()).requestLoadDetail();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtils.showShort("network error");
            }
        });

    }
}
