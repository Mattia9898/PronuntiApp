package views.dialog;


import it.uniba.dib.pronuntiapp.R;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;


public class RequestConfirmDialog extends PopUpDialog {

    public RequestConfirmDialog(Context context, String title, String description) {

        super(context);
        setTitle(title);
        setDescription(description);
        setConfirmButton(context.getString(R.string.confirm));
        setCancelButton(context.getString(R.string.cancel));

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

