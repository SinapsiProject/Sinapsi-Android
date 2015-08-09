package com.sinapsi.android.enginesystem;

import android.content.Context;
import android.widget.Toast;

import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.system.PlatformDependantObjectProvider;
import com.sinapsi.engine.system.ComponentSystemAdapter;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.annotations.AdapterImplementation;
import com.sinapsi.engine.annotations.InitializationNeededObjects;
import com.sinapsi.model.module.ModuleMember;

/**
 * This provides a way for actions to call Toast.makeText().show() .
 */
@ModuleMember(DefaultAndroidModules.ANTARES_ANDROID_MODULE_NAME)
@AdapterImplementation(
        value = ToastAdapter.ADAPTER_TOAST,
        platform = SinapsiPlatforms.PLATFORM_ANDROID
)
@InitializationNeededObjects(
        PlatformDependantObjectProvider.ObjectKey.ANDROID_SERVICE_CONTEXT
)
public class ToastAdapter implements ComponentSystemAdapter{
    public static final String ADAPTER_TOAST = "ADAPTER_TOAST";

    private Context context;

    @Override
    public void init(Object... requiredPlatformDependantObjects) {
        this.context = (Context) requiredPlatformDependantObjects[0];
    }

    public void printMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
