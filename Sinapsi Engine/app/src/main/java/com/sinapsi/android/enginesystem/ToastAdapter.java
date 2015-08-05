package com.sinapsi.android.enginesystem;

import android.content.Context;
import android.widget.Toast;

import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.PlatformDependantObjectProvider;
import com.sinapsi.engine.ComponentSystemAdapter;
import com.sinapsi.engine.system.annotations.AdapterImplementation;
import com.sinapsi.engine.system.annotations.InitializationNeededObjects;
import com.sinapsi.model.module.SinapsiModuleDescriptor;

/**
 * This provides a way for actions to call Toast.makeText().show() .
 */
@AdapterImplementation(ToastAdapter.ADAPTER_TOAST)
@InitializationNeededObjects(
        PlatformDependantObjectProvider.ObjectKey.ANDROID_SERVICE_CONTEXT
)
public class ToastAdapter implements ComponentSystemAdapter{
    public static final String ADAPTER_TOAST = "ADAPTER_TOAST";
    public static final String REQUIREMENT_TOAST = "REQUIREMENT_TOAST";

    private Context context;

    @Override
    public void init(Object... requiredPlatformDependantObjects) {
        this.context = (Context) requiredPlatformDependantObjects[0];
    }

    public void printMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public SinapsiModuleDescriptor getBelongingSinapsiModule() {
        return DefaultAndroidModules.ANTARES_ANDROID_MODULE;
    }
}
