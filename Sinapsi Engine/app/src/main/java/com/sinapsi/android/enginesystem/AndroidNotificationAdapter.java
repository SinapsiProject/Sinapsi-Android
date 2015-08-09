package com.sinapsi.android.enginesystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.sinapsi.android.R;
import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.system.PlatformDependantObjectProvider;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.modules.common.NotificationAdapter;
import com.sinapsi.engine.annotations.AdapterImplementation;
import com.sinapsi.engine.annotations.InitializationNeededObjects;
import com.sinapsi.model.module.ModuleMember;

/**
 * NotificationAdapter - implementation for Android platform
 */
@ModuleMember(DefaultAndroidModules.ANTARES_ANDROID_COMMON_ADAPTERS_MODULE_NAME)
@AdapterImplementation(
        value = NotificationAdapter.ADAPTER_NOTIFICATION,
        platform = SinapsiPlatforms.PLATFORM_ANDROID)
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

}
