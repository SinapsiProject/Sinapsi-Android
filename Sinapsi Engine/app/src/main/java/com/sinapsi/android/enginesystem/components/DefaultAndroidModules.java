package com.sinapsi.android.enginesystem.components;

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

    public static final SinapsiModule ANTARES_ANDROID_MODULE = new FactoryModel().newModule(
            SinapsiVersions.ANTARES.ordinal(),
            SinapsiVersions.ANTARES.ordinal(),
            ANTARES_ANDROID_PACKAGE_NAME,
            DefaultCoreModules.SINAPSI_TEAM_DEVELOPER_ID,
            SinapsiPlatforms.PLATFORM_ANDROID,
            ActionToast.class
    );

    private DefaultAndroidModules(){} //don't instantiate pls
}
