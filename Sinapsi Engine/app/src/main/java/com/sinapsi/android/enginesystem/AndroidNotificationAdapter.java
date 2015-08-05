package com.sinapsi.android.enginesystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.sinapsi.android.R;
import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.PlatformDependantObjectProvider;
import com.sinapsi.engine.system.NotificationAdapter;
import com.sinapsi.engine.system.annotations.AdapterImplementation;
import com.sinapsi.engine.system.annotations.InitializationNeededObjects;
import com.sinapsi.model.module.SinapsiModuleDescriptor;

/**
 * NotificationAdapter - implementation for Android platform
 */
@AdapterImplementation(NotificationAdapter.ADAPTER_NOTIFICATION)
@InitializationNeededObjects(
        PlatformDependantObjectProvider.ObjectKey.ANDROID_APPLICATION_CONTEXT
)
public class AndroidNotificationAdapter implements NotificationAdapter {

    private Context context;

    @Override
    public void init(Object... requiredPlatformDependantObjects) {
        this.context = (Context) requiredPlatformDependantObjects[0];
    }

    @Override
    public void showSimpleNotification(String title, String message) {

        // Build the notification
        Notification n  = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notif_icon) //HINT: let the user choose
                .setAutoCancel(true).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }

    @Override
    public SinapsiModuleDescriptor getBelongingSinapsiModule() {
        return DefaultAndroidModules.ANTARES_ANDROID_COMMONS_MODULE;
    }

}
