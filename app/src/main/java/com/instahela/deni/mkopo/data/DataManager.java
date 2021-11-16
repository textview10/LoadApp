package com.instahela.deni.mkopo.data;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.bean.AreaResponseBean;
import com.instahela.deni.mkopo.bean.BaseResponseBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataManager {
    private static final String TAG = "DataManager";

    private static DataManager mDataManager;

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (mDataManager == null) {
            synchronized (DataManager.class) {
                if (mDataManager == null) {
                    mDataManager = new DataManager();
                }
            }
        }
        return mDataManager;
    }

    public static final String TYPE_AREA = "provinceStateArea";
    private static final String TYPE_RELATION_SHIP = "relationship";
    private static final String TYPE_EDUCATION = "education";
    private static final String TYPE_MARITAL = "marital";
    private static final String TYPE_SALARY = "salary";
    private static final String TYPE_WORK = "work";

    //地区数据
    private final AreaResponseBean areaResponseBean = new AreaResponseBean();
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

    public AreaResponseBean getAreaData() {
        return areaResponseBean;
    }

    public ArrayList<Pair<String, String>> getRelationShipList(){
        return mRelationShipList;
    }

    public ArrayList<Pair<String, String>> getEducationList(){
        return mEducationList;
    }

    public ArrayList<Pair<String, String>> getMaritalList(){
        return mMaritalList;
    }

    public ArrayList<Pair<String, String>> getSalaryList(){
        return mSalaryList;
    }

    public ArrayList<Pair<String, String>> getWorkList(){
        return mWorkList;
    }

    public void requestInfo() {
        requestInfoByType(TYPE_AREA);  //省市
        requestInfoByType(TYPE_RELATION_SHIP);      //关系
        requestInfoByType(TYPE_EDUCATION);      //教育
        requestInfoByType(TYPE_EDUCATION);      //教育
        requestInfoByType(TYPE_MARITAL);      //婚姻
        requestInfoByType(TYPE_SALARY);      //收入
        requestInfoByType(TYPE_WORK);      //工作

        requestTextDetail(TEXT_APPROVING);
    }

    public void requestInfoByType(String type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dictType", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.DICT_DETAIL_URL).tag(TAG).upJson(jsonObject)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        BaseResponseBean responseBean = gson.fromJson(response.body().toString(), BaseResponseBean.class);
                        if (responseBean != null && responseBean.isRequestSuccess()) {
                            try {
                                JSONObject targetObject = new JSONObject(response.body().toString());
                                if (targetObject != null) {
                                    JSONObject bodyObject = targetObject.optJSONObject("body");
                                    if (bodyObject != null) {
                                        JSONObject dictMapObject = bodyObject.optJSONObject("dictMap");
                                        if (dictMapObject != null) {
                                            switch (type) {
                                                case TYPE_AREA:
                                                    ArrayList<AreaResponseBean.Province> provinces = DataParserUtil.parseAreaAndState(dictMapObject);
                                                    if (provinces != null) {
                                                        areaResponseBean.provinces = provinces;
                                                    }
                                                    break;
                                                case TYPE_RELATION_SHIP:
                                                    ArrayList<Pair<String, String>> relationShips = DataParserUtil.parseCommonData(dictMapObject, TYPE_RELATION_SHIP);
                                                    if (relationShips != null){
                                                        mRelationShipList.clear();
                                                        mRelationShipList.addAll(relationShips);
                                                    }
                                                    break;
                                                case TYPE_EDUCATION:
                                                    ArrayList<Pair<String, String>> educationList = DataParserUtil.parseCommonData(dictMapObject, TYPE_EDUCATION);
                                                    if (educationList != null){
                                                        mEducationList.clear();
                                                        mEducationList.addAll(educationList);
                                                    }
                                                    break;
                                                case TYPE_MARITAL:
                                                    ArrayList<Pair<String, String>> maritalList = DataParserUtil.parseCommonData(dictMapObject, TYPE_MARITAL);
                                                    if (maritalList != null){
                                                        mMaritalList.clear();
                                                        mMaritalList.addAll(maritalList);
                                                    }
                                                    break;
                                                case TYPE_SALARY:
                                                    ArrayList<Pair<String, String>> salary = DataParserUtil.parseCommonData(dictMapObject, TYPE_SALARY);
                                                    if (salary != null){
                                                        mSalaryList.clear();
                                                        mSalaryList.addAll(salary);
                                                    }
                                                    break;
                                                case TYPE_WORK:
                                                    ArrayList<Pair<String, String>> work = DataParserUtil.parseCommonData(dictMapObject, TYPE_WORK);
                                                    if (work != null){
                                                        mWorkList.clear();
                                                        mWorkList.addAll(work);
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "requestInfoByType on error ...");
                    }
                });
    }

    //------------------------------文案-------------------------------------

    private static final String TEXT_APPROVING = "approving";     // 批准文案
    private static final String TEXT_OTP = "otp";     // 批准文案
    private static final String TEXT_DEFAULT = "default";     // 默认文案

    private void requestTextDetail(String textType){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("category", textType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.TEXT_DETAIL_URL).tag(TAG).upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("Test", "" + response.body());
                    }
                });
    }
}
