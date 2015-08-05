package com.sinapsi.android.enginesystem;

import android.content.Context;
import android.os.Build;

import com.sinapsi.android.AndroidAppConsts;
import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.PlatformDependantObjectProvider;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.system.DeviceInfoAdapter;
import com.sinapsi.engine.system.DialogAdapter;
import com.sinapsi.engine.system.annotations.AdapterImplementation;
import com.sinapsi.engine.system.annotations.InitializationNeededObjects;
import com.sinapsi.model.module.SinapsiModuleDescriptor;

/**
 * Android dependent class, give model and name of the current device
 */
@AdapterImplementation(DeviceInfoAdapter.SERVICE_DEVICE_INFO)
@InitializationNeededObjects(
        PlatformDependantObjectProvider.ObjectKey.ANDROID_APPLICATION_CONTEXT
)
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

    @Override
    public SinapsiModuleDescriptor getBelongingSinapsiModule() {
        return DefaultAndroidModules.ANTARES_ANDROID_MODULE;
    }


}
