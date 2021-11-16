package com.instahela.deni.mkopo.ui.home.startload;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseStatusFragment;
import com.instahela.deni.mkopo.bean.main.ProductBean;
import com.instahela.deni.mkopo.bean.personprofile.DetailProfileResponse;
import com.instahela.deni.mkopo.data.CollectDataManager;
import com.instahela.deni.mkopo.data.LocationMgr;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.global.LoanConstant;
import com.instahela.deni.mkopo.ui.dialog.requestpermission.RequestPermissionDialog;
import com.instahela.deni.mkopo.ui.load.LoanActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StartLoadFragment extends BaseStatusFragment {

    private static final String TAG = "StartLoadFragment";

    private RecyclerView rvLoanMoney, rvPeriod;

    private final ArrayList<ProductBean.Item> mOriginList = new ArrayList<>();
    private final ArrayList<String> mLoadMoneyList = new ArrayList<>();
    private final ArrayList<ProductBean.Item> mPeriodList = new ArrayList<>();

    private LoadMoneyAdapter loadMoneyAdapter;
    private PeriodAdapter periodAdapter;
    private ProgressBar mPb;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initErrorPage(view);
        initializeView(view);
        initializeData();
        requestProductInfo();
        return view;
    }

    private void initializeView(View view) {
        rvLoanMoney = view.findViewById(R.id.rv_main_loan_money);
        rvPeriod = view.findViewById(R.id.rv_main_period);
        mPb = view.findViewById(R.id.pb_start_loading);

        TextView tvApply = view.findViewById(R.id.tv_main_apply);
        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Constant.mLoadAmount) || TextUtils.isEmpty(Constant.prodName) ||
                        TextUtils.isEmpty(Constant.proId)) {
                    ToastUtils.showShort("un select load product.");
                    return;
                }
                boolean hasPermission = PermissionUtils.isGranted(PermissionConstants.LOCATION,
                        PermissionConstants.SMS, PermissionConstants.CALENDAR, PermissionConstants.CONTACTS);
                if (hasPermission) {
                    executeNext();
                } else {
                    requestPermission();
                }
            }
        });
    }

    @Override
    protected void clickErrorRetry() {
        requestProductInfo();
    }

    private void requestPermission() {
        RequestPermissionDialog dialog = new RequestPermissionDialog(getContext());
        dialog.setOnItemClickListener(new RequestPermissionDialog.OnItemClickListener() {
            @Override
            public void onClickAgree() {
                PermissionUtils.permission(Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE).callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        executeNext();
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("please allow permission.");
                    }
                }).request();
            }
        });
        dialog.show();
    }

    private void executeNext() {
        CollectDataManager.getInstance().collectHardware();
        LocationMgr.getInstance().getLocation();
//        // TODO 测试用的
//        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Object>() {
//            @Override
//            public Object doInBackground() throws Throwable {
//                SystemClock.sleep(5000);
//                CollectDataManager.getInstance().collectAuthData(getContext(),"",null);
//                return null;
//            }
//
//            @Override
//            public void onSuccess(Object result) {
//
//            }
//        });
        OkGo.<String>post(Api.REQUEST_PERSON_PROFILE_URL).tag(TAG).params("accountId", Constant.mAccountId)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        DetailProfileResponse detailProfileResponse = checkResponseSuccess(response, DetailProfileResponse.class);

                        LoanConstant.mDetailProfile = detailProfileResponse;
                        toLoanActivity();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toLoanActivity();
                        Log.e(TAG, " request Address Person Profile on error ...");
                    }
                });
    }

    private void toLoanActivity(){
        Intent intent = new Intent(getContext(), LoanActivity.class);
        startActivity(intent);
    }

    private void initializeData() {
        GridLayoutManager loadMoneyLayoutManager = new GridLayoutManager(getContext(), 3);
        rvLoanMoney.setLayoutManager(loadMoneyLayoutManager);
        loadMoneyAdapter = new LoadMoneyAdapter(mLoadMoneyList, -1);
        rvLoanMoney.setAdapter(loadMoneyAdapter);
        loadMoneyAdapter.setOnItemClickListener(new BaseHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String amount = mLoadMoneyList.get(pos);
                notifyPeriodByAmount(amount);
            }

        });
        rvLoanMoney.addItemDecoration(new HomeItemDecoration());

        GridLayoutManager periodLayoutManager = new GridLayoutManager(getContext(), 3);
        rvPeriod.setLayoutManager(periodLayoutManager);
        periodAdapter = new PeriodAdapter(mPeriodList, -1);
        rvPeriod.setAdapter(periodAdapter);
        periodAdapter.setOnItemClickListener(new BaseHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                ProductBean.Item item = mPeriodList.get(pos);
                selectItem(item);
            }
        });
        rvPeriod.addItemDecoration(new HomeItemDecoration());
    }


    private void notifyPeriodByAmount(String amount) {
        mPeriodList.clear();
        for (int i = 0; i < mOriginList.size(); i++) {
            ProductBean.Item item = mOriginList.get(i);
            if (TextUtils.equals(item.getAmount(), amount)) {
                mPeriodList.add(item);
            }
        }
        int selectPos = -1;
        if (mPeriodList.size() > 0){
            selectPos = 0;
            ProductBean.Item item = mPeriodList.get(selectPos);
            selectItem(item);
        }
        if (periodAdapter != null) {
            periodAdapter.setSelectPos(selectPos);
            periodAdapter.notifyDataSetChanged();
        }
    }

    private void requestProductInfo() {
        if (mPb != null) {
            mPb.setVisibility(View.VISIBLE);
        }
        OkGo.<String>post(Api.PRODUCTS_URL).tag(TAG).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                if (mPb != null) {
                    mPb.setVisibility(View.GONE);
                }
                hideErrorPage();
                ProductBean productBean = checkResponseSuccess(response, ProductBean.class);
                if (productBean == null) {
                    showErrorPage();
                    return;
                }
                if(productBean.getProducts() == null || productBean.getProducts().size() == 0){
                    showErrorPage();
                    ToastUtils.showShort("request product error.");
                    return;
                }
                ArrayList<ProductBean.Item> products = productBean.getProducts();
                mOriginList.clear();
                mOriginList.addAll(products);
                initHomeList();
//                Log.e(TAG, "request product info =  " + response.body().toString());
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (mPb != null) {
                    mPb.setVisibility(View.GONE);
                }
                showErrorPage();
                Log.e(TAG, "on error ...");
            }
        });
    }

    private void initHomeList() {
        mLoadMoneyList.clear();
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < mOriginList.size(); i++) {
            ProductBean.Item item = mOriginList.get(i);
            set.add(item.getAmount());
        }
        mLoadMoneyList.addAll(set);
        if (mLoadMoneyList.size() <= 0) {
            return;
        }
        if (loadMoneyAdapter != null) {
            loadMoneyAdapter.setSelectPos(0);
            loadMoneyAdapter.notifyDataSetChanged();
        }
        notifyPeriodByAmount(mLoadMoneyList.get(0));
    }

    private void selectItem(ProductBean.Item item) {
        Constant.mLoadAmount = item.getAmount();
        Constant.prodName = item.getProdName();
        Constant.proId = item.getProdId();
        Constant.mPeriod = item.getPeriod();
    }
}
