package views.dialog;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import it.uniba.dib.pronuntiapp.R;

public class RichiestaConfermaDialog extends PopUpDialog {

    public RichiestaConfermaDialog(Context context, String titolo, String descrizione) {
        super(context);
        setTitle(titolo);
        setDescription(descrizione);
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

