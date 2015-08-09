package com.sinapsi.android.enginesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

import com.sinapsi.android.R;
import com.sinapsi.android.enginesystem.components.DefaultAndroidModules;
import com.sinapsi.engine.system.PlatformDependantObjectProvider;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.modules.common.DialogAdapter;
import com.sinapsi.engine.annotations.AdapterImplementation;
import com.sinapsi.engine.annotations.InitializationNeededObjects;
import com.sinapsi.model.module.ModuleMember;

/**
 * DialogAdapter implementation for Android.
 */
@ModuleMember(DefaultAndroidModules.ANTARES_ANDROID_COMMON_ADAPTERS_MODULE_NAME)
@AdapterImplementation(
        value = DialogAdapter.ADAPTER_DIALOGS,
        platform = SinapsiPlatforms.PLATFORM_ANDROID)
@InitializationNeededObjects(
        PlatformDependantObjectProvider.ObjectKey.ANDROID_SERVICE_CONTEXT
)
public class AndroidDialogAdapter implements DialogAdapter {

    private Context context;

    @Override
    public void init(Object... requiredPlatformDependantObjects) {
        this.context = (Context) requiredPlatformDependantObjects[0];
    }

    @Override
    public void showSimpleConfirmDialog(String title, String message, final OnDialogChoiceListener onYes, final OnDialogChoiceListener onNo) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onYes.onDialogChoice();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onNo.onDialogChoice();
                dialog.dismiss();
            }
        });
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }

    @Override
    public void showStringInputDialog(String title, String message, final OnInputDialogChoiceListener onDo, final OnInputDialogChoiceListener onCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message);

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               onDo.onDialogChoice(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCancel.onDialogChoice(input.getText().toString());
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }


}
