package com.instahela.deni.mkopo.ui.home.loadstatus;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.ui.MainActivity;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.ui.home.LoadStatus;
import com.instahela.deni.mkopo.util.CheckClickFastUtils;

import org.jetbrains.annotations.NotNull;

public class LoadStatusFragment extends BaseFragment {

    private ImageView ivIcon;
    private TextView tvDesc;
    private TextView btnRefresh;

    private String mDesc;
    private @LoadStatus.LoanStatus
    String mStatus;
    private boolean mCanApply;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_processing, container, false);
        initializeView(view);
        updateStateByStatus();
        return view;
    }

    private void initializeView(View view) {
        ivIcon = view.findViewById(R.id.iv_loan_processing_icon);
        tvDesc = view.findViewById(R.id.tv_loan_processing_desc);
        btnRefresh = view.findViewById(R.id.tv_load_processing_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckClickFastUtils.checkClickFast()){
                    return;
                }
                MainActivity mainActivity = (MainActivity) getActivity();
//                switch (mStatus){
//                    case LoadStatus.TYPE_REPAY_SUCCESS:
//                        mainActivity.toStartLoanFragment();
//                        break;
//                    case LoadStatus.TYPE_REPAYING:
//                    case LoadStatus.TYPE_APPLY_FAILURE:
//                    case LoadStatus.TYPE_APPLY_START:
//                    default:
//                        mainActivity.requestLoadDetail();
//                        break;
//                }
                if (mCanApply){
                    mainActivity.toStartLoanFragment();
                } else {
                    mainActivity.requestLoadDetail();
                }
            }
        });
    }

    private void updateStateByStatus() {
        if (!TextUtils.isEmpty(mDesc) && tvDesc != null){
            tvDesc.setText(mDesc);
        }
        //共四个情况,会显示界面
        //贷款被拒绝,. //还款中, //还款成功//已申请,待审核 ;
        //修改Iv状态
        switch (mStatus){
            case LoadStatus.TYPE_APPLY_FAILURE:
                if (ivIcon != null){
                    ivIcon.setImageResource(R.drawable.ic_loan_failure);
                }
                break;
            case LoadStatus.TYPE_APPLY_START:
                if (getActivity() instanceof BaseActivity){
                    ((BaseActivity) getActivity()).setTitle(getResources()
                            .getString(R.string.load_result_title_in_progress));
                }
                if (ivIcon != null){
                    ivIcon.setImageResource(R.drawable.ic_loan_processing);
                }
                break;
            case LoadStatus.TYPE_REPAY_SUCCESS:
            case LoadStatus.TYPE_REPAYING:
            default:
                if (ivIcon != null){
                    ivIcon.setImageResource(R.drawable.ic_loan_processing);
                }
                break;
        }

        //修改Button文字
        if (btnRefresh != null){
            btnRefresh.setText(mCanApply ? getText(R.string.load_processing_success_apply_now)
                    : getText(R.string.load_processing_refresh));
        }
    }

    public void setLoanDetail(String desc, @LoadStatus.LoanStatus String status, boolean canApply) {
        mDesc = desc;
        mStatus = status;
        mCanApply = canApply;
        updateStateByStatus();
    }

}
