package com.sinapsi.android.enginesystem;

import android.content.Context;
import android.widget.Toast;

import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.system.ComponentSystemAdapter;
import com.sinapsi.model.module.SinapsiModuleDescriptor;

/**
 * This provides a way for actions to call Toast.makeText().show() .
 */
public class ToastAdapter implements ComponentSystemAdapter{
    public static final String SERVICE_TOAST = "SERVICE_TOAST";
    public static final String REQUIREMENT_TOAST = "REQUIREMENT_TOAST";

    private final Context context;

    public ToastAdapter(Context context){
        this.context = context;
    }

    public void printMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public SinapsiModuleDescriptor getBelongingSinapsiModule() {
        return DefaultAndroidModules.ANTARES_ANDROID_MODULE;
    }
}
