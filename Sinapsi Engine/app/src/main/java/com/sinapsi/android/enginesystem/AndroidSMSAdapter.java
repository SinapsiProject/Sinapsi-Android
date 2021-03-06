package com.sinapsi.android.enginesystem;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;

import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.system.PlatformDependantObjectProvider;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.modules.common.SMSAdapter;
import com.sinapsi.engine.annotations.AdapterImplementation;
import com.sinapsi.engine.annotations.InitializationNeededObjects;
import com.sinapsi.model.module.ModuleMember;

import java.util.ArrayList;
import java.util.List;

/**
 * SMSAdapter implementation for Android.
 */
@ModuleMember(DefaultAndroidModules.ANTARES_ANDROID_COMMON_ADAPTERS_MODULE_NAME)
@AdapterImplementation(
        value = SMSAdapter.ADAPTER_SMS,
        platform = SinapsiPlatforms.PLATFORM_ANDROID)
@InitializationNeededObjects(
        PlatformDependantObjectProvider.ObjectKey.ANDROID_SERVICE_CONTEXT
)
public class AndroidSMSAdapter implements SMSAdapter {

    private Context context;

    @Override
    public void init(Object... requiredPlatformDependantObjects) {
        this.context = (Context) requiredPlatformDependantObjects[0];
    }

    @Override
    public boolean sendSMSMessage(Sms m) {
        String phoneNo = m.getAddress();
        String message = m.getMsg();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Sms> getInboxMessages() {
        return getFolderMessages("inbox");
    }

    @Override
    public List<Sms> getSentMessages() {
        return getFolderMessages("sent");
    }

    private List<Sms> getFolderMessages(String folder){
        List<Sms> lstSms = new ArrayList<>();
        Uri message = Uri.parse("content://sms/"+folder);
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(message, null, null, null, null);
        int totalSMS = c.getCount();
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                Sms objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndex("_id")));
                objSms.setAddress(c.getString(c.getColumnIndex("address")));
                objSms.setMsg(c.getString(c.getColumnIndex("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndex("date")));

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        c.close();

        return lstSms;
    }
}
