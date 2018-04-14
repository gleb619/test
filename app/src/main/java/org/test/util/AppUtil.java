package org.test.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;

import org.test.settings.Settings;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by BORIS on 01.12.2015.
 */
public class AppUtil {

    private static final String TAG = AppUtil.class.getName() + ":";


    private final Context context;
    private final Settings projectSettings;

    public AppUtil(Context context, Settings projectSettings) {
        this.context = context;
        this.projectSettings = projectSettings;
    }

    @Nullable
    public String testData() {
        if (!isNetworkEnabled()) {
            return "Network is unreachable";
        } else if (!isInternetConnectionEstableshed2()) {
            return "Internet connection problems";
        } else if (!isWiFiEnabled()) {
            return "Wifi is turned off";
        }

        return null;
    }

    public boolean isNetworkEnabled() {
        boolean result = false;

        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            result = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            Log.e(TAG, "#ERROR: ", e);
        }

        return result;
    }

    public boolean isWiFiEnabled() {
        boolean result = false;

        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            result = mWifi.isConnected() && mWifi.isAvailable();
        } catch (Exception e) {
            Log.e(TAG, "#ERROR: ", e);
        }

        return result;
    }

    public boolean isMobileConnectionEnabled() {
        boolean result = false;

        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            result = !(connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                    && connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getReason().equals("dataDisabled"));
        } catch (Exception e) {
            Log.e(TAG, "#ERROR: ", e);
        }

        return result;
    }

    public boolean isInternetConnectionEstableshed() {
        if (isNetworkEnabled()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(TAG, "#ERROR: ", e);
            }
        }

        return false;
    }

    public boolean isInternetConnectionEstableshed2() {
        if (isNetworkEnabled()) {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                if (reachable) {
                    return reachable;
                }
            } catch (Exception e) {
                Log.e(TAG, "#ERROR: ", e);
            }
        }

        return false;
    }

    public String ip4() {
//        try {
//            return Stream.of(Collections.list(NetworkInterface.getNetworkInterfaces()))
//                    .flatMap(ne -> Stream.of(Collections.list(ne.getInetAddresses())))
//                    .filter(InetAddress::isLoopbackAddress)
//                    .map(InetAddress::getHostAddress)
//                    .map(Object::toString)
//                    .findFirst()
//                    .orElse(null);
//        } catch (Exception e) {
//            Log.e(TAG, "ERROR: ", e);
//        }

        return null;
    }

    public String ip4_2() {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    public String ip4_3() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (isIPv4)
                            return sAddr;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR: ", e);
        } // for now eat exceptions

        return "";
    }

    public String ip4_4() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i(TAG, "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, "ERROR: ", e);
        }

        return null;
    }

    public String ip4_5() {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();

        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
    }

    public String ip4_6() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress().toString();
                        Log.e("IP address", "" + ipAddress);
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return null;
    }

    public String ip4_7() {
        return "localhost";
    }

}
