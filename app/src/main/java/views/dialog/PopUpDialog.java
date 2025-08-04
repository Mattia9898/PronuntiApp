package views.dialog;

import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;

import it.uniba.dib.pronuntiapp.R;

public abstract class PopUpDialog extends AlertDialog.Builder {

    protected Context context;

    protected AlertDialog alertDialog;

    private TextView textViewTitoloPopUp;

    private TextView textViewDescrizione;

    private Button buttonConferma;

    private Button buttonAnnulla;

    private static final int LAYOUT_ID = R.layout.pop_up_dialog;

    public PopUpDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogTheme);
        this.context = context;

        // Inizializzazione della vista del dialogo
        View view = LayoutInflater.from(context).inflate(LAYOUT_ID, null);
        setView(view);

        // Riferimenti agli elementi di layout
        textViewTitoloPopUp = view.findViewById(R.id.textViewTitoloPopUp);
        textViewDescrizione = view.findViewById(R.id.textViewDescrizionePopUp);
        buttonConferma = view.findViewById(R.id.buttonConfermaPopUp);
        buttonAnnulla = view.findViewById(R.id.buttonAnnullaPopUp);
    }


    public AlertDialog createCustome() {
        if (alertDialog == null) {
            alertDialog = create();
        }
        return alertDialog;
    }

    @Override
    public AlertDialog show() {
        if (alertDialog == null) {
            alertDialog = create();
        }
        alertDialog.show();
        return alertDialog;
    }


    protected void setTitolo(String titolo) {
        textViewTitoloPopUp.setText(titolo);
    }

    protected void setDescrizione(String descrizione) {
        textViewDescrizione.setText(descrizione);
    }

    protected void setConfermaButtonText(String text) {
        buttonConferma.setVisibility(View.VISIBLE);
        buttonConferma.setText(text);
    }

    protected void setAnnullaButtonText(String text) {
        buttonAnnulla.setVisibility(View.VISIBLE);
        buttonAnnulla.setText(text);
    }

    public void setOnConfermaButtonClickListener(OnConfermaButtonClickListener listener) {
        buttonConferma.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfermaButtonClicked();
            }
            alertDialog.dismiss();
        });
    }

    public void setOnAnnullaButtonClickListener(OnAnnullaButtonClickListener listener) {
        buttonAnnulla.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAnnullaButtonClicked();
            }
            alertDialog.dismiss();
        });
    }


    public interface OnConfermaButtonClickListener {
        void onConfermaButtonClicked();
    }

    public interface OnAnnullaButtonClickListener {
        void onAnnullaButtonClicked();
    }

}
