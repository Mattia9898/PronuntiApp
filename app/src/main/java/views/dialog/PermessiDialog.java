package views.dialog;

import android.content.Context;

import it.uniba.dib.pronuntiapp.R;

public class PermessiDialog extends PopUpDialog {

    public PermessiDialog(Context context, String descrizione) {
        super(context);
        setTitolo(context.getString(R.string.errorPermissionTitle));
        setDescrizione(descrizione);
        setConfermaButtonText(context.getString(R.string.errorPermissionOption1));
        setAnnullaButtonText(context.getString(R.string.errorPermissionOption2));
    }

}

