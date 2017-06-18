package org.amoustakos.linker.ui.dialogs;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.amoustakos.linker.R;
import org.amoustakos.linker.io.db.RealmManager;
import org.amoustakos.linker.io.models.Server;
import org.amoustakos.linker.ui.base.BaseDialog;
import org.amoustakos.linker.util.StringUtils;
import org.amoustakos.linker.util.ValidationUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class AddServerDialog extends BaseDialog implements Application.ActivityLifecycleCallbacks {

    private static boolean lastState = false;

    @Inject
    RealmManager realmManager;

    //Views
    EditText ipET, portET, nameET, descrET;
    Spinner protocolSpin;

    /*
     * Constructors
     */
    public AddServerDialog(@NonNull Context context) {
        super(context);
        bindUI();
    }

    public AddServerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        bindUI();
    }

    protected AddServerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        bindUI();
    }


    /*
     * UI
     */
    private void bindUI(){
        component().inject(this);
        setContentView(R.layout.dialog_add_server);
        setTitle(R.string.dialog_add_server_title);

        //Adjust dialog size
        if(getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(params);
        }

        //Bind views
        Button okBtn = ButterKnife.findById(this, R.id.dlg_btn_ok);
        ipET = ButterKnife.findById(this, R.id.dlg_et_server_ip);
        protocolSpin = ButterKnife.findById(this, R.id.dlg_spin_server_protocol);
        portET = ButterKnife.findById(this, R.id.dlg_et_server_port);
        nameET = ButterKnife.findById(this, R.id.dlg_et_server_name);
        descrET = ButterKnife.findById(this, R.id.dlg_et_server_descr);


        //Listeners
        okBtn.setOnClickListener(v -> {
            String ip = ipET.getText().toString();
            String protocol = protocolSpin.getSelectedItem().toString();
            String port = portET.getText().toString();
            String name = nameET.getText().toString();
            String description = descrET.getText().toString();

            //Check if input is valid
            if(!validateFields(ip, protocol, port, name, description))
                return;

            Server srv = new Server();
            srv.id = System.currentTimeMillis();
            srv.ip = ip;
            srv.port = Integer.parseInt(port);
            srv.name = name;
            srv.protocol = protocol;
            srv.description = description;

            //Check if server exists
            if(realmManager.serverExists(srv)){
                Toast.makeText(getContext(), R.string.toast_server_exists, Toast.LENGTH_LONG).show();
                return;
            }

            //Add server to database
            realmManager.addOrUpdate(srv);

            Toast.makeText(getContext(), R.string.toast_server_added, Toast.LENGTH_LONG).show();
            //TODO: consider erasing input
            lastState = false;
            dismiss();
        });
    }



    /*
     * Functionality
     */
    private boolean validateFields(String ip, String protocol, String port, String name, String description){
        //IP
        if(!ValidationUtil.isValidIPv4(ip)){
            ipET.setError(getContext().getString(R.string.error_et_generic));
            return false;
        }

        //Port
        if(!ValidationUtil.isValidPort(port)){
            portET.setError(getContext().getString(R.string.error_et_generic));
            return false;
        }

        //URL
        String url = StringUtils.compileUrl(protocol.toLowerCase(), ip, port);
        if(!ValidationUtil.isValidURL(url)){
            Toast.makeText(getContext(), R.string.toast_error_url_compile, Toast.LENGTH_LONG).show();
            ipET.setError(getContext().getString(R.string.error_et_generic));
            portET.setError(getContext().getString(R.string.error_et_generic));
            return false;
        }

        //Name
        if(StringUtils.isEmptyString(name)){
            nameET.setError(getContext().getString(R.string.error_et_generic));
            return false;
        }

        //Description
        if(StringUtils.isEmptyString(description)){
            descrET.setError(getContext().getString(R.string.error_et_generic));
            return false;
        }

        return true;
    }


    /*
     * Activity lifecycle
     */
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    public void onActivityStarted(Activity activity) {}
    public void onActivityResumed(Activity activity) {
        if(lastState)
            this.show();
    }
    public void onActivityPaused(Activity activity) {
        if(lastState)
            this.hide();
    }
    public void onActivityStopped(Activity activity) {}
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
    public void onActivityDestroyed(Activity activity) {
        this.dismiss();
    }

    public void show() {
        super.show();
        lastState = true;
    }
    public void cancel() {
        super.cancel();
        lastState = false;
    }
}
