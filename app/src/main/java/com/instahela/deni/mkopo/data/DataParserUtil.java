package com.instahela.deni.mkopo.data;

import android.text.TextUtils;
import android.util.Pair;

import com.instahela.deni.mkopo.BuildConfig;
import com.instahela.deni.mkopo.bean.AreaResponseBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataParserUtil {

    /**
     *
     */
    public static ArrayList<Pair<String, String>> parseCommonData(JSONObject jsonObject, String key) {
        try {
            if (jsonObject == null) {
                return null;
            }
            JSONArray array = jsonObject.optJSONArray(key);
            if (array == null) {
                return null;
            }
            ArrayList<Pair<String, String>> list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.optJSONObject(i);
                if (item == null){
                    continue;
                }
                String tempKey = item.optString("key");
                String value = item.optString("val");
                if (!TextUtils.isEmpty(value)){
                    list.add(new Pair<>(value, tempKey));
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            if (BuildConfig.DEBUG){
                throw e;
            }
        }
        return null;
    }

    // province / state /area
    public static ArrayList<AreaResponseBean.Province> parseAreaAndState(JSONObject jsonObject) {
        try {
            HashMap<String, String> provincesMap = new HashMap<>(); //省一级的数据
            HashMap<String, HashMap<String, String>> stateMap = new HashMap<>();    //州一级的数据
            HashMap<String, HashMap<String, String>> areaMap = new HashMap<>();    //区一级的数据

            //解析省
            JSONArray provinceArray = jsonObject.optJSONArray("province");
            if (provinceArray != null) {
                for (int i = 0; i < provinceArray.length(); i++) {
                    JSONObject tempProvince = provinceArray.getJSONObject(i);
                    String provinceKey = tempProvince.optString("key");
                    String provinceVal = tempProvince.optString("val");
                    provincesMap.put(provinceKey, provinceVal);
                }
            }
            //解析州
            JSONObject stateObject = jsonObject.optJSONObject("state");
            if (stateObject != null) {
                Iterator<String> iterator = stateObject.keys();
                while (iterator.hasNext()) {
                    String provinceStateKey = iterator.next();
                    JSONArray stateArray = stateObject.optJSONArray(provinceStateKey);
                    HashMap<String, String> perStateMap = new HashMap<>();
                    if (stateArray != null) {
                        for (int i = 0; i < stateArray.length(); i++) {
                            JSONObject stateJsonObject = stateArray.getJSONObject(i);
                            String stateKey = stateJsonObject.optString("key");
                            String stateVal = stateJsonObject.optString("val");
                            perStateMap.put(stateKey, stateVal);
                        }
                    }
                    stateMap.put(provinceStateKey, perStateMap);
                }
            }
            //解析区
            JSONObject areaObject = jsonObject.optJSONObject("area");
            if (areaObject != null) {
                Iterator<String> iterator = areaObject.keys();
                while (iterator.hasNext()) {
                    String stateAreaKey = iterator.next();
                    JSONArray areaArray = areaObject.optJSONArray(stateAreaKey);
                    HashMap<String, String> perAreaMap = new HashMap<>();
                    if (areaArray != null) {
                        for (int i = 0; i < areaArray.length(); i++) {
                            JSONObject stateJsonObject = areaArray.getJSONObject(i);
                            String stateKey = stateJsonObject.optString("key");
                            String stateVal = stateJsonObject.optString("val");
                            perAreaMap.put(stateKey, stateVal);
                        }
                    }
                    areaMap.put(stateAreaKey, perAreaMap);
                }
            }

            ArrayList<AreaResponseBean.Province> provinces = new ArrayList<>();

            Iterator<Map.Entry<String, String>> provinceIterator = provincesMap.entrySet().iterator();
            while (provinceIterator.hasNext()) {
                Map.Entry<String, String> entry = provinceIterator.next();
                AreaResponseBean.Province province = new AreaResponseBean.Province();
                province.provinceName = entry.getValue();
                HashMap<String, String> stateHashMap = areaMap.get(entry.getKey());
                if (stateHashMap != null) {
                    province.states = new ArrayList<>();
                    Iterator<Map.Entry<String, String>> stateIterator = stateHashMap.entrySet().iterator();
                    while (stateIterator.hasNext()) {
                        Map.Entry<String, String> stateEntry = stateIterator.next();
                        AreaResponseBean.State state = new AreaResponseBean.State();
                        state.stateName = stateEntry.getValue();
                        HashMap<String, String> areaHashMap = areaMap.get(stateEntry.getKey());
                        if (areaHashMap != null) {
                            state.areas = new ArrayList<>();
                            Iterator<Map.Entry<String, String>> areaIterator = areaHashMap.entrySet().iterator();
                            while (areaIterator.hasNext()) {
                                Map.Entry<String, String> areaEntry = areaIterator.next();
                                state.areas.add(areaEntry.getValue());
                            }
                        }
                        province.states.add(state);
                    }
                }
                provinces.add(province);
            }
            return provinces;
        } catch (Exception e) {
            e.printStackTrace();
            if (BuildConfig.DEBUG) {
                try {
                    throw e;
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }
        return null;
    }
}
