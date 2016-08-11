package com.jinchao.population.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jinchao.population.MyApplication;


/**
 * Created by OfferJiShu01 on 2016/8/4.
 */
public class NetWorkManager {
    public interface  NetConnectChangeListener{
        void change(NetWorkInfo netWorkInfo);
    }
    /**
     * 枚举网络状态
     * NET_NO：没有网络
     * NET_2G:2g网络
     * NET_3G：3g网络
     * NET_4G：4g网络
     * NET_WIFI：wifi
     * NET_UNKNOWN：未知网络
     */
    public static  enum NetState{NET_NO,NET_2G,NET_3G,NET_4G,NET_WIFI,NET_UNKNOWN};
    public static  enum SIMIProvider{CUCC,CTCC,CMCC,UNKNOW_PROVIDER,NO_SIM};
    private NetConnectChangeListener netConnectChangeListener;
    static volatile NetWorkManager defaultInstance;
    private Context decontext;
    NetChangeBroadCast mReceiver=new NetChangeBroadCast();
    public static NetWorkManager getInstance(){
        if (defaultInstance == null) {
            synchronized (NetWorkManager.class) {
                if (defaultInstance == null) {
                    defaultInstance = new NetWorkManager();
                }
            }
        }
        return defaultInstance;
    }
    private void setOnNetChangeListener(@Nullable NetConnectChangeListener netChangeListener){
        this.netConnectChangeListener=netChangeListener;
    }

    /**
     * 注册广播
     * @param obj
     */
    public void regist(Object obj){

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); // 添加接收网络连接状态改变的Action
        if (obj instanceof Fragment){
            ((Fragment)obj).getActivity().registerReceiver(mReceiver, mFilter);
        }else{
            ((Context)obj).registerReceiver(mReceiver, mFilter);
        }
        defaultInstance.setOnNetChangeListener((NetConnectChangeListener)obj);

    }
    /**
     * 反注册广播
     * @param obj
     */
    public void unregist(Object obj){
        if (obj instanceof Fragment){
            ((Fragment)obj).getActivity().unregisterReceiver(mReceiver);
        }else{
            ((Context)obj).unregisterReceiver(mReceiver);
        }
    }

    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context){

        NetworkInfo networkInfo =((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo!=null)
            return networkInfo.isAvailable();

        return false;

    }

    /**
     * 获取运营商信息
     * @param context
     * @return
     */
    public static SIMIProvider getProvider(Context context){
        SIMIProvider simiProvider= SIMIProvider.NO_SIM;
        TelephonyManager telephonyManager =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI =telephonyManager.getSubscriberId();
        IMSI = telephonyManager.getSubscriberId();
        if (IMSI == null)
        return SIMIProvider.NO_SIM;
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。其中
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            simiProvider = SIMIProvider.CMCC; //"中国移动";
        } else if (IMSI.startsWith("46001")) {
            simiProvider = SIMIProvider.CUCC;// "中国联通";
        } else if (IMSI.startsWith("46003")) {
            simiProvider = SIMIProvider.CTCC;//"中国电信";
        } else{
            simiProvider = SIMIProvider.UNKNOW_PROVIDER;//"未知运营商";
        }
        return simiProvider;
    }

    /**
     * 获取网络类型
     * @param context
     * @return
     */
    public static NetState checkNetwork(Context context){
        NetState netState= NetState.NET_NO;
        NetworkInfo networkInfo =((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                netState = NetState.NET_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        netState = NetState.NET_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        netState = NetState.NET_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        netState = NetState.NET_4G;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            netState = NetState.NET_3G;
                        } else {
                            netState = NetState.NET_UNKNOWN;
                        }
                        Log.e("NETWORK_UTILS", "Network getSubtype : " + Integer.valueOf(networkType).toString());
                        break;
                }

            }
        }
        return netState;
    }
    private class NetChangeBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetWorkInfo netWorkInfo =new NetWorkInfo(checkNetwork(context),getProvider(context),isNetWorkAvailable(context));
            switch (checkNetwork(context)){
                case NET_2G:
                    netConnectChangeListener.change(netWorkInfo);
                    break;
                case NET_3G:
                    netConnectChangeListener.change(netWorkInfo);
                    break;
                case NET_4G:
                    netConnectChangeListener.change(netWorkInfo);
                    break;
                case NET_WIFI:
                    netConnectChangeListener.change(netWorkInfo);
                    break;
                case NET_NO:
                    netConnectChangeListener.change(netWorkInfo);
                    break;
                case NET_UNKNOWN:
                    netConnectChangeListener.change(netWorkInfo);
                    break;
                default:
                    break;
            }
        }
    }
    public  class NetWorkInfo{
        public NetState netState;
        public SIMIProvider simiProvider;
        public boolean isNetAvailable;

        public NetWorkInfo(NetState netState, SIMIProvider simiProvider, boolean isNetAvailable) {
            this.netState = netState;
            this.isNetAvailable = isNetAvailable;
            this.simiProvider = simiProvider;
        }

    }
}
