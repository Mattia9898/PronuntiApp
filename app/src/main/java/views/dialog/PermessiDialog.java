package views.dialog;


import it.uniba.dib.pronuntiapp.R;

import android.content.Context;


public class PermessiDialog extends PopUpDialog {

    public PermessiDialog(Context context, String description) {

        super(context);
        setTitle(context.getString(R.string.errorPermissionTitle));
        setDescription(description);
        setConfirmButton(context.getString(R.string.errorPermissionOption1));
        setCancelButton(context.getString(R.string.errorPermissionOption2));

    }

}

