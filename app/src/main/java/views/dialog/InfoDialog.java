package views.dialog;


import it.uniba.dib.pronuntiapp.R;

import android.content.Context;


public class InfoDialog extends PopUpDialog {

    public InfoDialog(Context context, String descriptionDialog, String confirmButton) {

        super(context);
        setTitle(context.getString(R.string.infoTitle));
        setDescription(descriptionDialog);
        setConfirmButton(confirmButton);

    }


}
