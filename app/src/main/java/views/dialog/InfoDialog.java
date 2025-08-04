package views.dialog;

import android.content.Context;

import it.uniba.dib.pronuntiapp.R;

public class InfoDialog extends PopUpDialog {

    public InfoDialog(Context context, String descrizione, String testoBottoneConferma) {
        super(context);
        setTitolo(context.getString(R.string.infoTitle));
        setDescrizione(descrizione);
        setConfermaButtonText(testoBottoneConferma);
    }


}
