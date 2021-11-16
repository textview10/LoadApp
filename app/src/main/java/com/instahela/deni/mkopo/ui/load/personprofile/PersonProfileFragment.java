package com.instahela.deni.mkopo.ui.load.personprofile;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.bean.AreaResponseBean;
import com.instahela.deni.mkopo.bean.BaseResponseBean;
import com.instahela.deni.mkopo.bean.personprofile.DetailProfileResponse;
import com.instahela.deni.mkopo.bean.personprofile.RequestProfileResponse;
import com.instahela.deni.mkopo.data.DataManager;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.global.LoanConstant;
import com.instahela.deni.mkopo.ui.MainActivity;
import com.instahela.deni.mkopo.ui.dialog.requestpermission.RequestPermissionDialog;
import com.instahela.deni.mkopo.ui.load.LoanActivity;
import com.instahela.deni.mkopo.widget.EditTextContainer;
import com.instahela.deni.mkopo.widget.GenderCheckBox;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 个人信息填写
 */
public class PersonProfileFragment extends BaseProfileFragment {

    private static final String TAG = "PersonProfile";
    private TextView tvNext;

    private EditTextContainer editFirstName, editMiddleName, editFamilyName, editNationId, editEmail, editStreetAddress;

    private final ArrayList<String> provinceList = new ArrayList<>();
    private final ArrayList<ArrayList<String>> stateList = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<String>>> areaList = new ArrayList<>();
    private View viewAreaPicker;

    private GenderCheckBox genderCheckBox;
    private FrameLayout flTimePicker;

    private String mSelectProvince, mSelectState, mSelectArea;
    private String mSelectDate;
    private TextView tvTimeDetail, tvArea;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_profile, container, false);
        initializeView(view);
        initializeData();
        return view;
    }

    private void initializeView(View view) {
        editFirstName = view.findViewById(R.id.edit_view_person_profile_first_name);
        editMiddleName = view.findViewById(R.id.edit_view_person_profile_middle_name);
        editFamilyName = view.findViewById(R.id.edit_view_person_profile_family_name);
        editNationId = view.findViewById(R.id.edit_view_person_profile_national_id);
        editEmail = view.findViewById(R.id.edit_view_person_profile_email);
        editStreetAddress = view.findViewById(R.id.edit_view_person_profile_street_address);
        viewAreaPicker = view.findViewById(R.id.fl_person_profile_picker);
        genderCheckBox = view.findViewById(R.id.gender_person_profile_check_box);
        flTimePicker = view.findViewById(R.id.fl_person_profile_time_picker);
        tvTimeDetail = view.findViewById(R.id.tv_person_profile_time);
        tvArea = view.findViewById(R.id.tv_person_profile_area_desc);

        tvNext = view.findViewById(R.id.tv_person_profile_next);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                if (getAllInfo(jsonObject)) {
                    commitPersonProfile(jsonObject);
                }
            }
        });
        viewAreaPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAreaPicker();
            }
        });
        flTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String datef = sdf.format(date);
                        mSelectDate = datef;
                        tvTimeDetail.setText(mSelectDate);
                    }
                });
            }
        });
    }

    private void initializeData() {
        if (LoanConstant.mDetailProfile != null) {
            updatePersonProfileUi(LoanConstant.mDetailProfile);
        } else {
            requestAddressPersonProfile();
        }
    }

    private void initAreaPickerData() {
        provinceList.clear();
        stateList.clear();
        areaList.clear();
        AreaResponseBean areaData = DataManager.getInstance().getAreaData();
        for (int i = 0; i < areaData.provinces.size(); i++) {
            AreaResponseBean.Province province = areaData.provinces.get(i);
            provinceList.add(province.provinceName);
            ArrayList<ArrayList<String>> areaStateList = new ArrayList<>();
            if (province != null && province.states != null && province.states.size() > 0) {
                ArrayList<String> stateTempList = new ArrayList<>();
                for (int j = 0; j < province.states.size(); j++) {
                    AreaResponseBean.State state = province.states.get(j);
                    ArrayList<String> areaTempList = new ArrayList<>();
                    if (state != null && state.areas != null && state.areas.size() > 0) {
                        stateTempList.add(state.stateName);
                        for (int k = 0; k < state.areas.size(); k++) {
                            String s = state.areas.get(k);
                            areaTempList.add(s);
                        }
                    }
                    if (areaTempList.size() == 0){
                        areaTempList.add(state.stateName);
                        stateTempList.add(state.stateName);
                    }
                    areaStateList.add(areaTempList);
                }
                if (stateTempList.size() == 0){
                    stateTempList.add(province.provinceName);
                }
                stateList.add(stateTempList);
            }
            if (areaStateList.size() == 0){    //没有state数据，就把父节点的数据加进去
                ArrayList<String> stateTempList = new ArrayList<>();
                stateTempList.add(province.provinceName);
                stateList.add(stateTempList);
                ArrayList<String> areaTempList = new ArrayList<>();
                areaTempList.add(province.provinceName);
                areaStateList.add(areaTempList);
            }
            areaList.add(areaStateList);
        }
    }


    public void showAreaPicker() {
        KeyboardUtils.hideSoftInput(getActivity());
        initAreaPickerData();
        if (provinceList.size() == 0 ||  stateList.size() == 0 ||  areaList.size() == 0){
            DataManager.getInstance().requestInfoByType(DataManager.TYPE_AREA);
            ToastUtils.showShort("area data error , please try again");
            return;
        }
        OptionsPickerBuilder pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (options1 != -1){
                    mSelectProvince = provinceList.get(options1);
                }
                if (options1 != -1 && options2 != -1) {
                    mSelectState = stateList.get(options1).get(options2);
                }
                if (options1 != -1 && options2 != -1 && options3 != -1) {
                    mSelectArea = areaList.get(options1).get(options2).get(options3);
                }
                if (tvArea != null) {
                    tvArea.setText(mSelectProvince + "-" + mSelectState + "-" + mSelectArea);
                }
                Log.e(TAG, " select province = " + mSelectProvince + " select state = " + mSelectState
                        + " select area = " + mSelectArea);
            }
        });
        OptionsPickerView view = pvOptions.setSubmitText("ok")//确定按钮文字
                .setCancelText("cancel")//取消按钮文字
                .setTitleText("city picker")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build();
        view.setPicker(provinceList, stateList, areaList);
        view.setSelectOptions(0, 0, 0);
        view.show();
    }

    private boolean getAllInfo(JSONObject jsonObject) {
        if (editFirstName == null || editFirstName.isEmptyText()) {
            ToastUtils.showShort("first name is null");
            return false;
        }
        if (editMiddleName == null) {
            ToastUtils.showShort("middle name is null");
            return false;
        }
        if (editFamilyName == null || editFamilyName.isEmptyText()) {
            ToastUtils.showShort("family name is null");
            return false;
        }
        if (editNationId == null || editNationId.isEmptyText()) {
            ToastUtils.showShort("nation id is null");
            return false;
        }
        if (editEmail == null || editEmail.isEmptyText()) {
            ToastUtils.showShort("email is null");
            return false;
        }
        if (editStreetAddress == null || editStreetAddress.isEmptyText()) {
            ToastUtils.showShort("home address is null");
            return false;
        }
        if (TextUtils.isEmpty(mSelectState) || TextUtils.isEmpty(mSelectArea)){
            ToastUtils.showShort("home address is null");
            return false;
        }
        if (genderCheckBox == null){
            ToastUtils.showShort("unChoice gender");
            return false;
        }
        if (mSelectDate == null){
            ToastUtils.showShort("birthday is null");
            return false;
        }
        try {
            jsonObject.put("firstName", editFirstName.getText());
            jsonObject.put("middleName", editMiddleName.getText());
            jsonObject.put("lastName", editFamilyName.getText());
            jsonObject.put("bvn", editNationId.getText());
            jsonObject.put("email", editEmail.getText());
            jsonObject.put("homeProvince", mSelectProvince);
            jsonObject.put("homeState", mSelectState);
            jsonObject.put("homeArea", mSelectArea);
            // 1,男, 2女
            jsonObject.put("gender", genderCheckBox.getCurPos());
            jsonObject.put("homeAddress", editStreetAddress.getText());
            jsonObject.put("birthday", mSelectDate);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    //提交个人详情页信息
    private void commitPersonProfile(JSONObject params) {
        OkGo.<String>post(Api.COMMIT_PERSON_PROFILE_URL).tag(TAG).params("accountId", Constant.mAccountId).
                params("mobile", Constant.mPhoneNum).upJson(params).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                RequestProfileResponse requestProfileResponse = checkResponseSuccess(response, RequestProfileResponse.class);
                if (requestProfileResponse == null ){
                    return;
                }
                if (!requestProfileResponse.isBvnChecked()){
                    ToastUtils.showShort("bvn check failure.");
                    return;
                }
                ToastUtils.showShort("person profile edit success");
                toPersonProfileFragment();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e(TAG, "on error ...");
            }
        });
    }

    private void toPersonProfileFragment() {
        if (getActivity() instanceof LoanActivity){
            LoanActivity activity = (LoanActivity) getActivity();
            activity.toAdditionalProfile();
        } else if (getActivity() instanceof MainActivity){
            PermissionUtils.permission( Manifest.permission.READ_CONTACTS
                    ).callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.toAdditionalProfile();
                }

                @Override
                public void onDenied() {
                    ToastUtils.showShort("please allow permission.");
                }
            }).request();
        }

    }

    private void requestAddressPersonProfile() {
        OkGo.<String>post(Api.REQUEST_PERSON_PROFILE_URL).tag(TAG).params("accountId", Constant.mAccountId)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        DetailProfileResponse detailProfileResponse = checkResponseSuccess(response, DetailProfileResponse.class);
                        if (detailProfileResponse == null){
                            return;
                        }
                        updatePersonProfileUi(detailProfileResponse);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, " request Address Person Profile on error ...");
                    }
                });
    }

    //数据回显
    private void updatePersonProfileUi(DetailProfileResponse detailProfileResponse) {
        if (detailProfileResponse == null ){
            return;
        }
        editFirstName.setEditTextAndSelection(detailProfileResponse.getFirstName());
        editMiddleName.setEditTextAndSelection(detailProfileResponse.getMiddleName());
        editFamilyName.setEditTextAndSelection(detailProfileResponse.getLastName());
        editNationId.setEditTextAndSelection(detailProfileResponse.getBvn());
        editEmail.setEditTextAndSelection(detailProfileResponse.getEmail());
        editStreetAddress.setEditTextAndSelection(detailProfileResponse.getHomeAddress());

        mSelectProvince = detailProfileResponse.getHomeProvince();
        mSelectState = detailProfileResponse.getHomeState();
        mSelectArea = detailProfileResponse.getHomeArea();

        mSelectDate = detailProfileResponse.getBirthday();

        tvArea.setText(mSelectProvince + "-" + mSelectState + "-" + mSelectArea);
        tvTimeDetail.setText(mSelectDate);
    }

    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(TAG);
        super.onDestroy();
    }
}
