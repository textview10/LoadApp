package com.instahela.deni.mkopo.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Author : xu.wang
 * Date   : 2021/10/26 1:09 下午
 * Desc   :
 */
public class MappingUtils {

    private static String accountId = "a1";
    private static String uniqDeviceId = "a2";
    private static String osBootLoader = "a3";
    private static String osBrand = "a4";
    private static String osCpuAbi = "a5";
    private static String osCpuAbi2 = "a6";
    private static String osDevice = "a7";
    private static String osDisplay = "a8";
    private static String osHardware = "a9";
    private static String osHost = "a10";
    private static String osId = "a11";
    private static String osModel = "a12";
    private static String osManufacturer = "a13";
    private static String osProduct = "a14";
    private static String osTags = "a15";
    private static String osTime = "a16";
    private static String osType = "a17";
    private static String osUser = "a18";
    private static String osvRelease = "a19";
    private static String osvCodename = "a20";
    private static String osvIncremental = "a21";
    private static String osvSdk = "a22";
    private static String osvSdkInt = "a23";
    private static String jsonData = "a24";
    private static String orderId = "a25";
    private static String mobile = "a26";
    private static String contacts = "a27";
    private static String contactsCode = "a28"; //Integer;
    private static String sms = "a29";
    private static String smsCode = "a30";
    private static String call = "a31";
    private static String callCode = "a32";   // Integ;
    private static String gps = "a33";
    private static String userIp = "a34";
    private static String pubIp = "a35";
    private static String imei = "a36";
    private static String androidId = "a37";
    private static String mac = "a38";
    private static String deviceUniqId = "a39";
    private static String brand = "a40";
    private static String appList = "a41";
    private static String appListCode = "a42";    //Integer;
    private static String hasUpload = "a43"; //boolean;
    private static String resolution = "a44";
    private static String cpuNum = "a45";
    private static String cpuModel = "a46";
    private static String battery_level = "a47";
    private static String battery_max = "a48";
    private static String battery_status = "a49";
    private static String total_boot_time = "a50";
    private static String total_boot_time_wake = "a51";
    private static String app_max_memory = "a52";
    private static String app_available_memory = "a53";
    private static String app_free_memory = "a54";
    private static String isRooted = "a55";   // boolean;
    private static String isEmulator = "a56"; //boolean;
    private static String innerVersionCode = "a57";
    private static String ctime = "a58";
    private static String storeTime = "a59";     // Long;
    private static String isSuccess = "a60"; //  boolean;
    private static String osBoard = "a61";
    private static String powerSource = "a62";
    private static String screenSize = "a63";
    private static String screenDpi = "a64";
    private static String screenTimeout = "a65";
    private static String arpStatus = "a66";
    private static String networkType = "a67";
    private static String timeZone = "a68";

    public static String getPowerSource() {
        return powerSource;
    }

    public static String getScreenSize() {
        return screenSize;
    }

    public static String getScreenDpi() {
        return screenDpi;
    }

    public static String getScreenTimeout() {
        return screenTimeout;
    }

    public static String getArpStatus() {
        return arpStatus;
    }

    public static String getNetworkType() {
        return networkType;
    }

    public static String getTimeZone() {
        return timeZone;
    }

    public static String getAccountId() {
        return accountId;
    }

    public static String getUniqDeviceId() {
        return uniqDeviceId;
    }

    public static String getOsBootLoader() {
        return osBootLoader;
    }

    public static String getOsBrand() {
        return osBrand;
    }

    public static String getOsBoard() {
        return osBoard;
    }

    public static String getOsCpuAbi() {
        return osCpuAbi;
    }

    public static String getOsCpuAbi2() {
        return osCpuAbi2;
    }

    public static String getOsDevice() {
        return osDevice;
    }

    public static String getOsDisplay() {
        return osDisplay;
    }

    public static String getOsHardware() {
        return osHardware;
    }

    public static String getOsHost() {
        return osHost;
    }

    public static String getOsId() {
        return osId;
    }

    public static String getOsModel() {
        return osModel;
    }

    public static String getOsManufacturer() {
        return osManufacturer;
    }

    public static String getOsProduct() {
        return osProduct;
    }

    public static String getOsTags() {
        return osTags;
    }

    public static String getOsTime() {
        return osTime;
    }

    public static String getOsType() {
        return osType;
    }

    public static String getOsUser() {
        return osUser;
    }

    public static String getOsvRelease() {
        return osvRelease;
    }

    public static String getOsvCodename() {
        return osvCodename;
    }

    public static String getOsvIncremental() {
        return osvIncremental;
    }

    public static String getOsvSdk() {
        return osvSdk;
    }

    public static String getOsvSdkInt() {
        return osvSdkInt;
    }

    public static String getJsonData() {
        return jsonData;
    }

    public static String getOrderId() {
        return orderId;
    }

    public static String getMobile() {
        return mobile;
    }

    public static String getContacts() {
        return contacts;
    }

    public static String getContactsCode() {
        return contactsCode;
    }

    public static String getSms() {
        return sms;
    }

    public static String getSmsCode() {
        return smsCode;
    }

    public static String getCall() {
        return call;
    }

    public static String getCallCode() {
        return callCode;
    }

    public static String getGps() {
        return gps;
    }

    public static String getUserIp() {
        return userIp;
    }

    public static String getPubIp() {
        return pubIp;
    }

    public static String getImei() {
        return imei;
    }

    public static String getAndroidId() {
        return androidId;
    }

    public static String getMac() {
        return mac;
    }

    public static String getDeviceUniqId() {
        return deviceUniqId;
    }

    public static String getBrand() {
        return brand;
    }

    public static String getAppList() {
        return appList;
    }

    public static String getAppListCode() {
        return appListCode;
    }

    public static String getHasUpload() {
        return hasUpload;
    }

    public static String getResolution() {
        return resolution;
    }

    public static String getCpuNum() {
        return cpuNum;
    }

    public static String getCpuModel() {
        return cpuModel;
    }

    public static String getBattery_level() {
        return battery_level;
    }

    public static String getBattery_max() {
        return battery_max;
    }

    public static String getBattery_status() {
        return battery_status;
    }

    public static String getTotal_boot_time() {
        return total_boot_time;
    }

    public static String getTotal_boot_time_wake() {
        return total_boot_time_wake;
    }

    public static String getApp_max_memory() {
        return app_max_memory;
    }

    public static String getApp_available_memory() {
        return app_available_memory;
    }

    public static String getApp_free_memory() {
        return app_free_memory;
    }

    public static String getIsRooted() {
        return isRooted;
    }

    public static String getIsEmulator() {
        return isEmulator;
    }

    public static String getInnerVersionCode() {
        return innerVersionCode;
    }

    public static String getCtime() {
        return ctime;
    }

    public static String getStoreTime() {
        return storeTime;
    }

    public static String getIsSuccess() {
        return isSuccess;
    }

}
