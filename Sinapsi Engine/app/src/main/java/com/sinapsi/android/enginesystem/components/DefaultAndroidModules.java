package com.sinapsi.android.enginesystem.components;

import android.content.Context;
import android.content.pm.PackageManager;

import com.sinapsi.android.enginesystem.AndroidDeviceInfo;
import com.sinapsi.android.enginesystem.AndroidDialogAdapter;
import com.sinapsi.android.enginesystem.AndroidNotificationAdapter;
import com.sinapsi.android.enginesystem.AndroidSMSAdapter;
import com.sinapsi.android.enginesystem.AndroidWifiAdapter;
import com.sinapsi.android.enginesystem.ToastAdapter;
import com.sinapsi.engine.modules.DefaultCoreModules;
import com.sinapsi.engine.system.PlatformDependantObjectProvider;
import com.sinapsi.engine.requirements.RequirementResolver;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.SinapsiVersions;
import com.sinapsi.engine.system.SystemFacade;
import com.sinapsi.model.impl.FactoryModel;
import com.sinapsi.model.module.SinapsiModule;

/**
 * Collection of sinapsi modules for android platform
 */
public class DefaultAndroidModules {


    public static final String ANTARES_ANDROID_MODULE_NAME = "ANTARES_ANDROID_MODULE";

    public static final String REQUIREMENT_TOAST = "REQUIREMENT_TOAST";

    public static final SinapsiModule ANTARES_ANDROID_MODULE = new FactoryModel().newModule(
            SinapsiVersions.ANTARES.ordinal(),
            SinapsiVersions.ANTARES.ordinal(),
            ANTARES_ANDROID_MODULE_NAME,
            DefaultCoreModules.SINAPSI_TEAM_DEVELOPER_ID,
            SinapsiPlatforms.PLATFORM_ANDROID,
            new RequirementResolver() {
                @Override
                public PlatformDependantObjectProvider.ObjectKey[] getPlatformDependantObjectsKeys() {
                    return null;
                }

                @Override
                public void setPlatformDependantObjects(Object... objects) {
                    //does nothing
                }

                @Override
                public void resolveRequirements(SystemFacade sf) {
                    sf.setRequirementSpec(REQUIREMENT_TOAST, true);
                }
            },
            null,
            null,
            null,

            ActionToast.class,

            ToastAdapter.class,

            AndroidDeviceInfo.class
    );

    public static final String ANTARES_ANDROID_COMMON_ADAPTERS_MODULE_NAME = "ANTARES_ANDROID_COMMON_ADAPTERS_MODULE";

    public static final SinapsiModule ANTARES_ANDROID_COMMON_ADAPTERS_MODULE = new FactoryModel().newModule(
            SinapsiVersions.ANTARES.ordinal(),
            SinapsiVersions.ANTARES.ordinal(),
            ANTARES_ANDROID_COMMON_ADAPTERS_MODULE_NAME,
            DefaultCoreModules.SINAPSI_TEAM_DEVELOPER_ID,
            SinapsiPlatforms.PLATFORM_ANDROID,
            new RequirementResolver() {

                private Context c;

                @Override
                public PlatformDependantObjectProvider.ObjectKey[] getPlatformDependantObjectsKeys() {
                    return new PlatformDependantObjectProvider.ObjectKey[]{
                            PlatformDependantObjectProvider.ObjectKey.ANDROID_SERVICE_CONTEXT
                    };
                }

                @Override
                public void setPlatformDependantObjects(Object... objects) {
                    c = (Context)objects[0];

                }

                @Override
                public void resolveRequirements(SystemFacade sf) {
                    PackageManager pm = c.getPackageManager();

                    sf.setRequirementSpec(DefaultCoreModules.REQUIREMENT_WIFI, pm.hasSystemFeature(PackageManager.FEATURE_WIFI));
                    sf.setRequirementSpec(DefaultCoreModules.REQUIREMENT_SMS_READ, pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY));

                    sf.setRequirementSpec(DefaultCoreModules.REQUIREMENT_SIMPLE_DIALOGS, true);
                    sf.setRequirementSpec(DefaultCoreModules.REQUIREMENT_SIMPLE_NOTIFICATIONS, true);
                    sf.setRequirementSpec(DefaultCoreModules.REQUIREMENT_INTERCEPT_SCREEN_POWER, true);
                    sf.setRequirementSpec(DefaultCoreModules.REQUIREMENT_AC_CHARGER, true);
                    sf.setRequirementSpec(DefaultCoreModules.REQUIREMENT_INPUT_DIALOGS, true);
                }
            },
            null,
            new String[]{DefaultCoreModules.ROLE_ANTARES_COMMON_ADAPTERS},
            null,

            AndroidDialogAdapter.class,
            AndroidNotificationAdapter.class,
            AndroidSMSAdapter.class,
            AndroidWifiAdapter.class

    );

    private DefaultAndroidModules(){} //don't instantiate pls
}
