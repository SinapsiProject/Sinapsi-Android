package com.sinapsi.android.enginesystem.components;

import com.sinapsi.android.enginesystem.AndroidDeviceInfo;
import com.sinapsi.android.enginesystem.AndroidDialogAdapter;
import com.sinapsi.android.enginesystem.AndroidNotificationAdapter;
import com.sinapsi.android.enginesystem.AndroidSMSAdapter;
import com.sinapsi.android.enginesystem.AndroidWifiAdapter;
import com.sinapsi.android.enginesystem.ToastAdapter;
import com.sinapsi.engine.DefaultCoreModules;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.SinapsiVersions;
import com.sinapsi.model.impl.FactoryModel;
import com.sinapsi.model.module.SinapsiModule;

/**
 * Collection of sinapsi modules for android platform
 */
public class DefaultAndroidModules {


    public static final String ANTARES_ANDROID_PACKAGE_NAME = "ANTARES_ANDROID_MODULE";
    public static final String ANTARES_ANDROID_COMMONS_PACKAGE_NAME = "ANTARES_ANDROID_COMMONS_MODULE";

    public static final SinapsiModule ANTARES_ANDROID_MODULE = new FactoryModel().newModule(
            SinapsiVersions.ANTARES.ordinal(),
            SinapsiVersions.ANTARES.ordinal(),
            ANTARES_ANDROID_PACKAGE_NAME,
            DefaultCoreModules.SINAPSI_TEAM_DEVELOPER_ID,
            SinapsiPlatforms.PLATFORM_ANDROID,

            ActionToast.class,

            ToastAdapter.class,

            AndroidDeviceInfo.class
    );

    public static final SinapsiModule ANTARES_ANDROID_COMMONS_MODULE = new FactoryModel().newModule(
            SinapsiVersions.ANTARES.ordinal(),
            SinapsiVersions.ANTARES.ordinal(),
            ANTARES_ANDROID_COMMONS_PACKAGE_NAME,
            DefaultCoreModules.SINAPSI_TEAM_DEVELOPER_ID,
            SinapsiPlatforms.PLATFORM_ANDROID,

            AndroidDialogAdapter.class,
            AndroidNotificationAdapter.class,
            AndroidSMSAdapter.class,
            AndroidWifiAdapter.class

    );

    private DefaultAndroidModules(){} //don't instantiate pls
}
