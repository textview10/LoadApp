package com.instahela.deni.mkopo.ui.load.personprofile;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.bean.additionalprofile.AdditionalProfileBean;
import com.instahela.deni.mkopo.bean.additionalprofile.AdditionalProfileResponse;
import com.instahela.deni.mkopo.bean.ContactBean;
import com.instahela.deni.mkopo.data.DataManager;
import com.instahela.deni.mkopo.event.RefreshEvent;
import com.instahela.deni.mkopo.event.ToStartLoadEvent;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.ui.MainActivity;
import com.instahela.deni.mkopo.ui.dialog.CustomDialog;
import com.instahela.deni.mkopo.ui.dialog.contact.SettingContactAdapter;
import com.instahela.deni.mkopo.ui.dialog.selectdata.SelectDataDialog;
import com.instahela.deni.mkopo.ui.load.LoanActivity;
import com.instahela.deni.mkopo.util.CheckClickFastUtils;
import com.instahela.deni.mkopo.widget.EditSelectContainer;
import com.instahela.deni.mkopo.widget.EditTextContainer;
import com.instahela.deni.mkopo.widget.SelectContainer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 附加设置
 */
public class AdditionalProfileFragment extends BaseProfileFragment {

    private static final String TAG = "AdditionalProfile";

    private EditSelectContainer selectPhone1;
    private SelectContainer selectRelationShip1;
    private EditTextContainer editFullName1;

    private EditSelectContainer selectPhone2;
    private SelectContainer selectRelationShip2;
    private EditTextContainer editFullName2;

    private SelectContainer selectMartial, selectEducation, selectWork, selectSalary, selectPayday, selectLoanHistory;
    private TextView tvSubmit;

    //关系 relationShip
    private final ArrayList<Pair<String, String>> mRelationShipList = new ArrayList<>();
    //受教育程度
    private final ArrayList<Pair<String, String>> mEducationList = new ArrayList<>();
    //婚姻程度
    private final ArrayList<Pair<String, String>> mMaritalList = new ArrayList<>();
    //收入程度
    private final ArrayList<Pair<String, String>> mSalaryList = new ArrayList<>();
    //工作程度
    private final ArrayList<Pair<String, String>> mWorkList = new ArrayList<>();
    //本地联系人
    private final ArrayList<ContactBean> mContactList = new ArrayList<>();

    private final ArrayList<Pair<String, String>> mYesOrNoList = new ArrayList<>();

    private static final int TYPE_RELATIONSHIP_1 = 111;
    private static final int TYPE_RELATIONSHIP_2 = 112;
    private static final int TYPE_EDUCATION = 113;
    private static final int TYPE_MARITAL = 114;
    private static final int TYPE_SALARY = 115;
    private static final int TYPE_WORK = 116;
    private static final int TYPE_SELECT_PHONE_1 = 117;
    private static final int TYPE_SELECT_PHONE_2 = 118;
    private static final int TYPE_SELECT_LOAN_HISTORY = 119;    //有没有贷款历史

    private Pair<String, String> mRelationShip1;
    private Pair<String, String> mRelationShip2;
    private Pair<String, String> mEducation;
    private Pair<String, String> mMarital;
    private Pair<String, String> mSalary;
    private Pair<String, String> mWork;
    private Pair<String, String> mSelectHistory;
    private String mPayday;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addition_profile, container, false);
        CheckClickFastUtils.clickStartLoanMillions = System.currentTimeMillis();
        initializeView(view);
        initializeData();
        initializeListener();
        initializePermission();
        return view;
    }

    private void initializeView(View view) {
        selectPhone1 = view.findViewById(R.id.select_container_additional_phone_1);
        selectRelationShip1 = view.findViewById(R.id.select_container_additional_profile_relationship_1);
        editFullName1 = view.findViewById(R.id.edit_container_additional_profile_full_name_1);

        selectPhone2 = view.findViewById(R.id.select_container_additional_profile_phone2);
        selectRelationShip2 = view.findViewById(R.id.select_container_additional_profile_relationship_2);
        editFullName2 = view.findViewById(R.id.edit_view_additional_profile_full_name_2);

        selectMartial = view.findViewById(R.id.select_container_additional_martial);
        selectEducation = view.findViewById(R.id.select_container_additional_education);
        selectWork = view.findViewById(R.id.select_container_additional_employment);
        selectSalary = view.findViewById(R.id.select_container_additional_salary);
        selectPayday = view.findViewById(R.id.select_container_additional_payday);
        selectLoanHistory = view.findViewById(R.id.select_container_additional_loan_history);

        tvSubmit = view.findViewById(R.id.tv_additional_profile_submit);
    }

    private void initializeData() {
        mYesOrNoList.clear();
        mYesOrNoList.add(new Pair<>("yes", "0"));
        mYesOrNoList.add(new Pair<>("no", "1"));
        requestAdditionalProfileUrl();
    }

    private void initializeListener() {
        selectPhone1.setOnBgClickListener(new EditSelectContainer.OnBgClickListener() {
            @Override
            public void onClick() {
                showContactDialog(TYPE_SELECT_PHONE_1);
            }
        });
        selectPhone2.setOnBgClickListener(new EditSelectContainer.OnBgClickListener() {
            @Override
            public void onClick() {
                showContactDialog(TYPE_SELECT_PHONE_2);
            }
        });
        selectRelationShip1.setOnClickListener(v -> {
            mRelationShipList.clear();
            mRelationShipList.addAll(DataManager.getInstance().getRelationShipList());
            showListDialog(mRelationShipList, TYPE_RELATIONSHIP_1);
        });
        selectRelationShip2.setOnClickListener(v -> {
            mRelationShipList.clear();
            mRelationShipList.addAll(DataManager.getInstance().getRelationShipList());
            showListDialog(mRelationShipList, TYPE_RELATIONSHIP_2);
        });
        selectMartial.setOnClickListener(v -> {
            mMaritalList.clear();
            mMaritalList.addAll(DataManager.getInstance().getMaritalList());
            showListDialog(mMaritalList, TYPE_MARITAL);
        });
        selectEducation.setOnClickListener(v -> {
            mEducationList.clear();
            mEducationList.addAll(DataManager.getInstance().getEducationList());
            showListDialog(mEducationList, TYPE_EDUCATION);
        });
        selectSalary.setOnClickListener(v -> {
            mSalaryList.clear();
            mSalaryList.addAll(DataManager.getInstance().getSalaryList());
            showListDialog(mSalaryList, TYPE_SALARY);
        });
        selectWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWorkList.clear();
                mWorkList.addAll(DataManager.getInstance().getWorkList());
                showListDialog(mWorkList, TYPE_WORK);
            }
        });
        selectPayday.setOnClickListener(v -> {
            showTimePicker(new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String datef = sdf.format(date);
                    mPayday = datef;
                    selectPayday.setText(mPayday);
                }
            });
        });
        selectLoanHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog(mYesOrNoList, TYPE_SELECT_LOAN_HISTORY);
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkClickFast()){
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                if (checkAdditionalProfileParams(jsonObject)) {
                    addAdditionalProfileUrl(jsonObject);
                }
            }
        });
    }


    private void initializePermission() {
        boolean hasReadContactPermission = PermissionUtils.isGranted(Manifest.permission.READ_CONTACTS);
        if (!hasReadContactPermission) {
            PermissionUtils.permission(PermissionConstants.CONTACTS).callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    readContact();
                }

                @Override
                public void onDenied() {

                }
            }).request();
        } else {
            readContact();
        }
    }

    private void readContact() {
        //调用并获取联系人信息
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null) {
                mContactList.clear();
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                    String ringtone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE));
//                    Log.e(TAG, " photo = " + photoUri + "  ringtone = " + ringtone + " look = " + lookupUri);

                    mContactList.add(new ContactBean(id, number, displayName, photoUri, ringtone));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, " exception = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private boolean checkAdditionalProfileParams(JSONObject jsonObject) {
        if (mRelationShip1 == null || TextUtils.isEmpty(mRelationShip1.first)) {
            ToastUtils.showShort("relation ship 1 is null");
            return false;
        }
        if (mRelationShip2 == null || TextUtils.isEmpty(mRelationShip2.first)) {
            ToastUtils.showShort("relation ship 2 is null");
            return false;
        }
        if (mEducation == null || TextUtils.isEmpty(mEducation.first)) {
            ToastUtils.showShort("education is null");
            return false;
        }
        if (mMarital == null || TextUtils.isEmpty(mMarital.first)) {
            ToastUtils.showShort("marital is null");
            return false;
        }
        if (mSalary == null || TextUtils.isEmpty(mSalary.first)) {
            ToastUtils.showShort("salary is null");
            return false;
        }
        if (mWork == null || TextUtils.isEmpty(mWork.first)) {
            ToastUtils.showShort("work is null");
            return false;
        }
        if (selectPhone1 == null || selectPhone1.isEmptyText()) {
            ToastUtils.showShort("select phone1 null");
            return false;
        }
        if (selectPhone2 == null || selectPhone2.isEmptyText()) {
            ToastUtils.showShort("select phone2 null");
            return false;
        }
        if (TextUtils.isEmpty(mPayday)) {
            ToastUtils.showShort("payday null");
            return false;
        }
        if (editFullName1 == null || TextUtils.isEmpty(editFullName1.getText())) {
            ToastUtils.showShort(" phone1 full name is null");
            return false;
        }
        if (editFullName2 == null || TextUtils.isEmpty(editFullName2.getText())) {
            ToastUtils.showShort(" phone2 full name is null");
            return false;
        }

        try {
            jsonObject.put("contact1", editFullName1.getText());    //紧急联系人1姓名
            jsonObject.put("contact1Mobile", selectPhone1.getText());        //紧急联系人1手机号
            jsonObject.put("contact1Relationship", strToInt(mRelationShip1));     //紧急联系人1关系
            jsonObject.put("contact2", editFullName2.getText());        //紧急联系人2姓名
            jsonObject.put("contact2Mobile", selectPhone2.getText());        //紧急联系人2手机号
            jsonObject.put("contact2Relationship", strToInt(mRelationShip2));    //紧急联系人2关系
            jsonObject.put("marital", strToInt(mMarital));    //婚姻状况
            jsonObject.put("work", strToInt(mWork));    //工作情况
            jsonObject.put("salary", strToInt(mSalary));    //工资区间
            jsonObject.put("payday", mPayday);    //发薪日
            jsonObject.put("hasOutstandingLoan", strToInt(mSelectHistory));    //是否有外债
            // TODO
//            jsonObject.put("companyName", "xes");    //单位名称
//            jsonObject.put("companyAddress", "beijing");    //工作地详细地址(手动输入)
            jsonObject.put("education", strToInt(mEducation));    //学历状况
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private int strToInt(Pair<String, String> pair) {
        if (pair == null || TextUtils.isEmpty(pair.second)){
            return -1;
        }
        try {
            return Integer.parseInt(pair.second);
        } catch (Exception e) {

        }
        return -1;
    }

    private void toLoadConfirmation() {
        if (getActivity() instanceof LoanActivity){
            toMainPage();
        } else if (getActivity() instanceof MainActivity){
            EventBus.getDefault().post(new RefreshEvent());
        }
    }

    private void toMainPage(){
        LoanActivity loanActivity = (LoanActivity) getActivity();
        loanActivity.finish();
        EventBus.getDefault().post(new ToStartLoadEvent());
    }

    public void addAdditionalProfileUrl(JSONObject params) {
        OkGo.<String>post(Api.ADD_ADDITIONAL_PROFILE_URL).tag(TAG).params("accountId", Constant.mAccountId).
                params("mobile", Constant.mPhoneNum).upJson(params).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                AdditionalProfileResponse additionalProfileResponse = checkResponseSuccess(response, AdditionalProfileResponse.class);
                if (additionalProfileResponse == null ) {
                    return;
                }
                ToastUtils.showShort("person profile edit success");
                toLoadConfirmation();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e(TAG, "on error ...");
            }
        });
    }

    public void showListDialog(ArrayList<Pair<String, String>> list, int type) {
        SelectDataDialog dialog = new SelectDataDialog(getContext());
        dialog.setList(list, (content, pos) -> {
            switch (type) {
                case TYPE_RELATIONSHIP_1:
                    mRelationShip1 = content;
                    if (selectRelationShip1 != null) {
                        selectRelationShip1.setText(mRelationShip1.first);
                    }
                    break;
                case TYPE_RELATIONSHIP_2:
                    mRelationShip2 = content;
                    if (selectRelationShip2 != null) {
                        selectRelationShip2.setText(mRelationShip2.first);
                    }
                    break;
                case TYPE_EDUCATION:
                    mEducation = content;
                    if (selectEducation != null) {
                        selectEducation.setText(mEducation.first);
                    }
                    break;
                case TYPE_MARITAL:
                    mMarital = content;
                    if (selectMartial != null) {
                        selectMartial.setText(mMarital.first);
                    }
                    break;
                case TYPE_SALARY:
                    mSalary = content;
                    if (selectSalary != null) {
                        selectSalary.setText(mSalary.first);
                    }
                    break;
                case TYPE_WORK:
                    mWork = content;
                    if (selectWork != null) {
                        selectWork.setText(mWork.first);
                    }
                    break;
                case TYPE_SELECT_LOAN_HISTORY:
                    mSelectHistory = content;
                    if (selectLoanHistory != null) {
                        selectLoanHistory.setText(mSelectHistory.first);
                    }
                    break;
            }
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //显示选择联系人的Dialog.
    public void showContactDialog(int type) {
        CustomDialog customDialog = new CustomDialog(getContext());
        customDialog.setView(R.layout.dialog_contact);
        RecyclerView rv = customDialog.findViewById(R.id.rv_dialog_contact);
        TextView tv = customDialog.findViewById(R.id.tv_contact_no_data);
        if (mContactList.size() == 0){
            rv.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        SettingContactAdapter contactAdapter = new SettingContactAdapter(mContactList);
        contactAdapter.setOnItemClickListener(new SettingContactAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                try {
                    switch (type) {
                        case TYPE_SELECT_PHONE_1: {
                            String number = mContactList.get(pos).number;
                            if (selectPhone1 != null) {
                                selectPhone1.setEditTextAndSelection(number);
                            }
                            String contactName = mContactList.get(pos).contactName;
                            if (editFullName1 != null) {
                                editFullName1.setEditTextAndSelection(contactName);
                            }
                            break;
                        }
                        case TYPE_SELECT_PHONE_2: {
                            String number = mContactList.get(pos).number;
                            if (selectPhone2 != null) {
                                selectPhone2.setEditTextAndSelection(number);
                            }
                            String contactName = mContactList.get(pos).contactName;
                            if (editFullName2 != null) {
                                editFullName2.setEditTextAndSelection(contactName);
                            }
                            break;
                        }
                    }
                } catch (Exception e) {

                }
                if (customDialog != null) {
                    customDialog.dismiss();
                }
            }
        });
        rv.setAdapter(contactAdapter);
        customDialog.show();
    }

    private void requestAdditionalProfileUrl() {
        OkGo.<String>post(Api.DETAIL_ADDITIONAL_PROFILE_URL).tag(TAG).params("accountId", Constant.mAccountId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        AdditionalProfileBean profileBean = checkResponseSuccess(response, AdditionalProfileBean.class);
                        if (profileBean == null){
                            ToastUtils.showShort("request additional profile error");
                            return;
                        }
                        if (selectPhone1 != null && !TextUtils.isEmpty(profileBean.getContact1Mobile())) {
                            selectPhone1.setEditTextAndSelection(profileBean.getContact1Mobile());
                        }
                        if (editFullName1 != null && !TextUtils.isEmpty(profileBean.getContact1())){
                            editFullName1.setEditTextAndSelection(profileBean.getContact1());
                        }
                        if (selectRelationShip1 != null && !TextUtils.isEmpty(profileBean.getContact1RelationshipLabel())){
                            mRelationShip1 = new Pair<>(profileBean.getContact1RelationshipLabel(), profileBean.getContact1Relationship());
                            selectRelationShip1.setText(profileBean.getContact1RelationshipLabel());
                        }
                        if (selectPhone2 != null && !TextUtils.isEmpty(profileBean.getContact2Mobile())) {
                            selectPhone2.setEditTextAndSelection(profileBean.getContact2Mobile());
                        }
                        if (editFullName2 != null && !TextUtils.isEmpty(profileBean.getContact2())){
                            editFullName2.setEditTextAndSelection(profileBean.getContact2());
                        }
                        if (selectRelationShip2 != null && !TextUtils.isEmpty(profileBean.getContact2RelationshipLabel())){
                            mRelationShip2 = new Pair<>(profileBean.getContact2RelationshipLabel(), profileBean.getContact2Relationship());
                            selectRelationShip2.setText(profileBean.getContact2RelationshipLabel());
                        }
                        if (selectEducation != null && !TextUtils.isEmpty(profileBean.getEducationLabel())){
                            mEducation = new Pair<>(profileBean.getEducationLabel(), profileBean.getEducation());
                            selectEducation.setText(profileBean.getEducationLabel());
                        }
                        if (selectMartial != null && !TextUtils.isEmpty(profileBean.getMaritalLabel())){
                            mMarital = new Pair<>(profileBean.getMaritalLabel(), profileBean.getMarital());
                            selectMartial.setText(profileBean.getMaritalLabel());
                        }
                        if (selectSalary != null && !TextUtils.isEmpty(profileBean.getSalaryLabel())){
                            mSalary = new Pair<>(profileBean.getSalaryLabel(), profileBean.getSalary());
                            selectSalary.setText(profileBean.getSalaryLabel());
                        }
                        if (selectWork != null && !TextUtils.isEmpty(profileBean.getWorkLabel())){
                            mWork = new Pair<>(profileBean.getWorkLabel(), profileBean.getWork());
                            selectWork.setText(profileBean.getMaritalLabel());
                        }
                        if (selectPayday != null && !TextUtils.isEmpty(profileBean.getPayday())){
                            mPayday = profileBean.getPayday();
                            selectPayday.setText(mPayday);
                        }
                        if (selectLoanHistory != null){
                            int hasOutstandingLoan = profileBean.getHasOutstandingLoan();
                            mSelectHistory = new Pair<>(hasOutstandingLoan == 0 ? "yes" : "no" , String.valueOf(hasOutstandingLoan));
                            selectLoanHistory.setText(mSelectHistory.first);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "on error ...");
                    }
                });
    }

}
