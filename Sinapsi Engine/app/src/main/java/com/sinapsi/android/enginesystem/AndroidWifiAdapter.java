package com.sinapsi.android.enginesystem;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.system.PlatformDependantObjectProvider;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.modules.common.WifiAdapter;
import com.sinapsi.engine.parameters.ConnectionStatusChoices;
import com.sinapsi.engine.parameters.SwitchStatusChoices;
import com.sinapsi.engine.annotations.AdapterImplementation;
import com.sinapsi.engine.annotations.InitializationNeededObjects;
import com.sinapsi.model.module.ModuleMember;

/**
 * WifiAdapter implementation for Android.
 */
@ModuleMember(DefaultAndroidModules.ANTARES_ANDROID_COMMON_ADAPTERS_MODULE_NAME)
@AdapterImplementation(
        value = WifiAdapter.ADAPTER_WIFI,
        platform = SinapsiPlatforms.PLATFORM_ANDROID
)
@InitializationNeededObjects(
        PlatformDependantObjectProvider.ObjectKey.ANDROID_SERVICE_CONTEXT
)
public class AndroidWifiAdapter implements WifiAdapter{

    private WifiManager wm;
    private ConnectivityManager cm;


    @Override
    public void init(Object... requiredPlatformDependantObjects) {
        Context context = (Context) requiredPlatformDependantObjects[0];
        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public SwitchStatusChoices getStatus() {
        int s = wm.getWifiState();
        switch (s){
            case WifiManager.WIFI_STATE_DISABLED:
                return SwitchStatusChoices.DISABLED;
            case WifiManager.WIFI_STATE_DISABLING:
                return SwitchStatusChoices.DISABLING;
            case WifiManager.WIFI_STATE_ENABLED:
                return SwitchStatusChoices.ENABLED;
            case WifiManager.WIFI_STATE_ENABLING:
                return SwitchStatusChoices.ENABLING;
            case WifiManager.WIFI_STATE_UNKNOWN:
            default:
                return null;
        }
    }

    @Override
    public ConnectionStatusChoices getConnectionStatus() {
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(ni == null) return ConnectionStatusChoices.DISCONNECTED;
        if(ni.isConnected()){
            return ConnectionStatusChoices.CONNECTED;
        }else if(ni.isConnectedOrConnecting()){
            return ConnectionStatusChoices.CONNECTING;
        }else{
            return ConnectionStatusChoices.DISCONNECTED;
        }
    }

    @Override
    public String getSSID() {
        return wm.getConnectionInfo().getSSID();
    }

    @Override
    public void connectToSSID(String id) {
        //HINT: this is possible: http://stackoverflow.com/questions/8818290/how-to-connect-to-a-specific-wifi-network-in-android-programmatically
    }

    @Override
    public void setStatus(boolean status) {
        wm.setWifiEnabled(status);
    }

    @Override
    public void setConnectionStatus(boolean status) {
        if(status){
            wm.reconnect();
        }else{
            wm.disconnect();
        }
    }

}
