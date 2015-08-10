package com.sinapsi.android.enginesystem;

import android.content.Context;
import android.os.Build;

import com.sinapsi.android.AndroidAppConsts;
import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.system.PlatformDependantObjectProvider;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.modules.common.DeviceInfoAdapter;
import com.sinapsi.engine.annotations.AdapterImplementation;
import com.sinapsi.engine.annotations.InitializationNeededObjects;
import com.sinapsi.model.module.ModuleMember;

/**
 * Android dependent class, give model and name of the current device
 */
@ModuleMember(DefaultAndroidModules.ANTARES_ANDROID_MODULE_NAME)
@AdapterImplementation(
        value = DeviceInfoAdapter.ADAPTER_DEVICE_INFO,
        platform = SinapsiPlatforms.PLATFORM_ANDROID)
@InitializationNeededObjects({
        PlatformDependantObjectProvider.ObjectKey.ANDROID_APPLICATION_CONTEXT
})
public class AndroidDeviceInfo implements DeviceInfoAdapter {

    private Context context;

    @Override
    public void init(Object... requiredPlatformDependantObjects) {
        this.context = (Context) requiredPlatformDependantObjects[0];
    }

    /**
     * Return client version
     *
     * @return
     */
    public int getVersion() {
        return AndroidAppConsts.CLIENT_VERSION;
    }

    /**
     * Return the name od the device
     *
     * @return
     */
    @Override
    public String getDeviceName() {
        return InstallationUUIDManager.id(context);
    }

    /**
     * Return the model of the device
     *
     * @return
     */
    @Override
    public String getDeviceModel() {
        return Build.MODEL + " " + Build.PRODUCT;
    }

    /**
     * Return the type od the device
     *
     * @return
     */
    @Override
    public String getDeviceType() {
        return SinapsiPlatforms.PLATFORM_ANDROID;
    }


}
