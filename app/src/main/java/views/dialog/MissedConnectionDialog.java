package views.dialog;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import it.uniba.dib.pronuntiapp.R;

public class MissedConnectionDialog extends PopUpDialog {

    public MissedConnectionDialog(Context context) {
        super(context);
        setTitle(context.getString(R.string.errorConnectionTitle));
        setDescription(context.getString(R.string.errorConnectionDescription));
        setConfirmButton(context.getString(R.string.restartApp));
        alertDialog = create();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public AlertDialog show() {
        if (alertDialog != null) {
            alertDialog.show();
        }
        return alertDialog;
    }

}

