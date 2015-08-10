package com.sinapsi.android.background;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.sinapsi.android.SinapsiAndroidApplication;
import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.android.persistence.AndroidDiffDBManager;
import com.sinapsi.android.persistence.AndroidLocalDBManager;
import com.sinapsi.android.utils.DialogUtils;
import com.sinapsi.android.view.LoginActivity;
import com.sinapsi.android.view.MainActivity;
import com.sinapsi.android.web.AndroidBase64DecodingMethod;
import com.sinapsi.android.web.AndroidBase64EncodingMethod;
import com.sinapsi.client.AppConsts;
import com.sinapsi.android.Lol;
import com.sinapsi.android.persistence.AndroidUserSettingsFacade;
import com.sinapsi.client.SyncManager;
import com.sinapsi.client.background.SinapsiDaemonThread;
import com.sinapsi.client.persistence.DiffDBManager;
import com.sinapsi.client.persistence.InconsistentMacroChangeException;
import com.sinapsi.client.persistence.LocalDBManager;
import com.sinapsi.client.persistence.UserSettingsFacade;
import com.sinapsi.client.persistence.syncmodel.MacroChange;
import com.sinapsi.client.persistence.syncmodel.MacroSyncConflict;
import com.sinapsi.client.web.SinapsiWebServiceFacade;
import com.sinapsi.client.websocket.WSClient;
import com.sinapsi.engine.modules.DefaultCoreModules;
import com.sinapsi.engine.requirements.DefaultRequirementResolver;
import com.sinapsi.engine.system.PlatformDependantObjectProvider;
import com.sinapsi.model.DeviceInterface;
import com.sinapsi.model.impl.Device;
import com.sinapsi.utils.Triplet;
import com.sinapsi.client.web.OnlineStatusProvider;
import com.sinapsi.android.enginesystem.AndroidActivationManager;
import com.sinapsi.engine.component.ComponentFactory;
import com.sinapsi.engine.MacroEngine;
import com.sinapsi.android.R;
import com.sinapsi.engine.execution.RemoteExecutionDescriptor;
import com.sinapsi.engine.log.LogMessage;
import com.sinapsi.engine.log.SinapsiLog;
import com.sinapsi.engine.log.SystemLogInterface;
import com.sinapsi.engine.system.SystemFacade;
import com.sinapsi.model.MacroInterface;
import com.sinapsi.model.UserInterface;
import com.sinapsi.model.impl.FactoryModel;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.android.AndroidLog;

/**
 * Sinapsi background service on android platform.
 * This should be started in foreground notification mode
 * in order to remain running on the system. The engine is initialized
 * here and
 */
public class SinapsiBackgroundService extends Service
        implements
        OnlineStatusProvider,
        SinapsiDaemonThread.DaemonCallbacks{

    private SinapsiDaemonThread daemon;

    private UserSettingsFacade settings;

    private SinapsiLog sinapsiLog;

    private static FactoryModel fm = new FactoryModel();


    private static final UserInterface logoutUser = fm.newUser(-1, "Not logged in yet.", "", false, "user");
    private UserInterface loggedUser = logoutUser;

    private REDHandler redHandler;



    @Override
    public void onCreate() {
        super.onCreate();

        redHandler = new REDHandler(Looper.getMainLooper());

        settings = new AndroidUserSettingsFacade(AppConsts.PREFS_FILE_NAME, this);
        //loadSettings(settings);


        // initializing sinapsi log ---------------------------------
        sinapsiLog = new SinapsiLog();
        sinapsiLog.addLogInterface(new SystemLogInterface() {
            @Override
            public void printMessage(LogMessage lm) {
                Lol.d(lm.getTag(), lm.getMessage());
            }
        });

        daemon = new SinapsiDaemonThread(
                settings,
                sinapsiLog,
                new AndroidLog("RETROFIT"),
                new AndroidBase64EncodingMethod(),
                new AndroidBase64DecodingMethod(),
                new AndroidActivationManager(this),
                new DefaultRequirementResolver() {
                    @Override
                    public void resolveRequirements(SystemFacade sf) {
                        sf.setRequirementSpec(DefaultCoreModules.REQUIREMENT_RESTARTABLE_MACRO_ENGINE, true);
                    }
                },
                new PlatformDependantObjectProvider() {
                    @Override
                    public Object getObject(ObjectKey key) throws ObjectNotAvailableException {
                        switch (key) {
                            case LOGGED_USER:
                                return loggedUser;

                            case ANDROID_SERVICE_CONTEXT:
                                return SinapsiBackgroundService.this;

                            case ANDROID_APPLICATION_CONTEXT:
                                return getApplicationContext();

                            default:
                                throw new ObjectNotAvailableException(key.name());
                        }
                    }
                },
                new SinapsiDaemonThread.DBManagerProvider() {
                    @Override
                    public LocalDBManager openLocalDBManager(String fileName, ComponentFactory componentFactory) {
                        return new AndroidLocalDBManager(getApplicationContext(), fileName, componentFactory);
                    }

                    @Override
                    public DiffDBManager openDiffDBManager(String fileName) {
                        return new AndroidDiffDBManager(getApplicationContext(), fileName);
                    }
                },
                this,
                this,
                DefaultCoreModules.ANTARES_CORE_MODULE,
                DefaultCoreModules.ANTARES_COMMON_COMPONENTS_MODULE,
                DefaultAndroidModules.ANTARES_ANDROID_MODULE
        );

        daemon.initializeDaemon();
    }

    @Override
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public DeviceInterface getDevice() {
        return daemon.getDevice();
    }

    public ComponentFactory getComponentFactory() {
        return daemon.getComponentFactory();
    }

    public SinapsiWebServiceFacade getWeb() {
        return daemon.getWeb();
    }

    public void setDevice(Device device) {
        daemon.setDevice(device);
    }

    public void initEngine() {
        daemon.initEngine();
    }

    public WSClient getWSClient() {
        return daemon.getWSClient();
    }

    public MacroEngine getEngine() {
        return daemon.getEngine();
    }

    public void syncMacrosAndStartEngine() {
        daemon.syncMacrosAndStartEngine();
    }

    public void syncMacros(SinapsiDaemonThread.BackgroundSyncCallback callback, boolean userIntention) {
        daemon.syncMacros(callback, userIntention);
    }

    public void addMacro(MacroInterface macro, SinapsiDaemonThread.BackgroundSyncCallback callback, boolean userIntention) {
        daemon.updateMacro(macro, callback, userIntention);
    }

    public void removeMacro(int id, SinapsiDaemonThread.BackgroundSyncCallback backgroundSyncCallback, boolean userIntention) {
        daemon.removeMacro(id, backgroundSyncCallback, userIntention);
    }

    public void updateMacro(MacroInterface macro, SinapsiDaemonThread.BackgroundSyncCallback callback, boolean userIntention) {
        daemon.updateMacro(macro, callback, userIntention);
    }

    public MacroInterface newEmptyMacro() {
        return daemon.newEmptyMacro();
    }

    public SinapsiDaemonThread getDaemon() {
        return daemon;
    }

    public UserInterface getLoggedUser() {
        return daemon.getLoggedUser();
    }

    public class SinapsiServiceBinder extends Binder {
        public SinapsiBackgroundService getService() {
            return SinapsiBackgroundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SinapsiServiceBinder();
    }

    @Override
    public void onEngineStarted() {
        startForegroundMode();
    }

    @Override
    public void onEnginePaused() {
        stopForegroundMode();
    }

    @Override
    public void onUserLogin(UserInterface user) {
        SinapsiAndroidApplication app = (SinapsiAndroidApplication) getApplication();
        app.setLoggedIn(true);
    }

    @Override
    public void onUserLogout() {
        this.loggedUser = logoutUser;
        SinapsiAndroidApplication app = (SinapsiAndroidApplication) getApplication();
        app.setLoggedIn(false);
    }

    @Override
    public void onSyncConflicts(List<MacroSyncConflict> conflicts, SyncManager.ConflictResolutionCallback callback) {
        handleConflicts(conflicts, callback);
    }

    @Override
    public void onSyncFailure(Throwable e, boolean showError) {
        handleSyncFailure(e, showError);
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Lol.d("WEB_SOCKET", "WebSocket Error: " + ex.getMessage());
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Lol.d("WEB_SOCKET", "WebSocket Close: " + code + ":" + reason);
    }

    @Override
    public void onWebSocketOpen() {
        Lol.d("WEB_SOCKET", "WebSocket Open");
    }

    @Override
    public void onWebSocketMessage(String message) {
        Lol.d("WEB_SOCKET", "WebSocket message received: '" + message + "'");
    }

    @Override
    public void onWebSocketUpdatedNotificationReceived() {

    }

    @Override
    public void onWebSocketNewConnectionNotificationReceived() {
        //TODO: update devices table (maybe in daemon)
    }

    @Override
    public void onWebSocketConnectionLostNotificationReceived() {
        //TODO: update devices table (maybe in daemon)
    }

    @Override
    public boolean onWebSocketRemoteExecutionDescriptorReceived(RemoteExecutionDescriptor red) {
        handleWsRed(red);
        return true;
    }

    private void startForegroundMode() {
        //HINT: useful toggles instead of classic content pending intent

        Intent i1 = new Intent(this, MainActivity.class);
        PendingIntent maini = PendingIntent.getActivity(this, 0, i1, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(getString(R.string.app_name))
                .setContentText("Sinapsi Engine service is running")
                .setSmallIcon(R.drawable.ic_stat_1436506281_cog)
                .setContentIntent(maini);
        Notification forenotif = builder.build();
        startForeground(1, forenotif);
    }

    private void stopForegroundMode() {
        stopForeground(true);
    }

    public void handleConflicts(List<MacroSyncConflict> conflicts, SyncManager.ConflictResolutionCallback callback) {
        //TODO (show them to the user, only if sinapsi gui is open, otherwise show notification, duplicate and disable macros)
        //if(sinapsiGuiIsOpen){
        for (MacroSyncConflict conflict : conflicts) {
            //TODO: show dialog to the user
        }
        //}else{
        //  showConflictNotification(conflicts.size());
        //  engine.pause();
        //  setResolveConflictsOnNextGuiOpen(true, conflictCallback);
        //}
    }

    public void handleSyncFailure(Throwable e, boolean showError) {

        Lol.d(SinapsiBackgroundService.class, "Sync failed: " + e.getMessage());
        e.printStackTrace();

        if (showError) {
            if (e instanceof RetrofitError) {
                DialogUtils.handleRetrofitError(e, SinapsiBackgroundService.this, true);
            } else if (e instanceof InconsistentMacroChangeException) {
                DialogUtils.showOkDialog(this,
                        "Error",
                        "A data consistency error occurred while syncing data.\n" +
                                "Retry. If this fails again, contact developers of Sinapsi\n" +
                                "Error description:" + e.getMessage(),
                        true);
            } else {
                DialogUtils.showOkDialog(SinapsiBackgroundService.this, "Sync failed", e.getMessage(), true);
            }
        }
    }

    private void handleWsRed(RemoteExecutionDescriptor red){
        Message msg = redHandler.obtainMessage();
        msg.obj = new Triplet<>(red, daemon.getEngine(), true);
        msg.sendToTarget();
    }

    private final class REDHandler extends Handler{

        public REDHandler(Looper looper) {
            super(looper); //SUPER LOOPER!
        }

        private void handleRecursive(RemoteExecutionDescriptor red, MacroEngine engine, boolean firstcall){
            Message msg = obtainMessage();
            msg.obj = new Triplet<>(red, engine, firstcall);
            handleMessage(msg);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            final Triplet<RemoteExecutionDescriptor, MacroEngine, Boolean> triplet = (Triplet<RemoteExecutionDescriptor, MacroEngine, Boolean>) msg.obj;

            try {
                daemon.getEngine().continueMacro(triplet.getFirst());
            } catch (MacroEngine.MissingMacroException e) {
                if (triplet.getThird()) {
                    //retries after a sync

                    daemon.syncMacros(new SinapsiDaemonThread.BackgroundSyncCallback() {
                        @Override
                        public void onBackgroundSyncSuccess(List<MacroInterface> currentMacros) {
                            handleRecursive(triplet.getFirst(), triplet.getSecond(), false);
                        }

                        @Override
                        public void onBackgroundSyncFail(Throwable error) {
                            //TODO: a remote execution descriptor arrived,
                            //----:     the macro is not present on this
                            //----:     client, and an attempt to sync failed.
                            //----:     what to do?
                        }
                    }, false);

                } else {
                    e.printStackTrace();
                    //the server is trying to tell the client to execute a macro that doesn't exist (neither in the server)
                    //just prints the stack trace and ignores the message
                }
            }
        }



    }


}
