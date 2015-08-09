package com.sinapsi.android.enginesystem.components;

import com.sinapsi.android.enginesystem.ToastAdapter;
import com.sinapsi.engine.component.Action;
import com.sinapsi.engine.SinapsiPlatforms;
import com.sinapsi.engine.execution.ExecutionInterface;
import com.sinapsi.engine.parameters.FormalParamBuilder;
import com.sinapsi.engine.annotations.Component;
import com.sinapsi.engine.annotations.Requirement;
import com.sinapsi.engine.annotations.Requires;
import com.sinapsi.model.module.ModuleMember;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Action toast. This is android-exclusive. Prints a "toast" message on the screen.
 */
@ModuleMember(DefaultAndroidModules.ANTARES_ANDROID_MODULE_NAME)
@Component( value = ActionToast.ACTION_TOAST,
            platform = SinapsiPlatforms.PLATFORM_ANDROID)
@Requires({
        @Requirement(name = DefaultAndroidModules.REQUIREMENT_TOAST, value =1)
})
public class ActionToast extends Action {

    public static final String ACTION_TOAST = "ACTION_TOAST";

    @Override
    protected void onActivate(ExecutionInterface ei) throws JSONException {
        ToastAdapter ta = (ToastAdapter) ei.getSystemFacade().getSystemService(ToastAdapter.ADAPTER_TOAST);
        JSONObject pjo = getParsedParams(ei.getLocalVars(), ei.getGlobalVars());
        ta.printMessage(pjo.getString("message"));
    }

    @Override
    public JSONObject getFormalParametersJSON() throws JSONException {
        return new FormalParamBuilder()
                .put("message", FormalParamBuilder.Types.STRING, false)
                .create();
    }

}
