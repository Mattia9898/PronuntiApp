package views.dialog;

import android.content.Context;

import it.uniba.dib.pronuntiapp.R;

public class PermessiDialog extends PopUpDialog {

    public PermessiDialog(Context context, String descrizione) {
        super(context);
        setTitle(context.getString(R.string.errorPermissionTitle));
        setDescription(descrizione);
        setConfirmButton(context.getString(R.string.errorPermissionOption1));
        setCancelButton(context.getString(R.string.errorPermissionOption2));
    }

}

