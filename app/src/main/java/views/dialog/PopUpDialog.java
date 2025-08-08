package views.dialog;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.widget.TextView;
import android.widget.Button;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;


public abstract class PopUpDialog extends AlertDialog.Builder {

    private TextView titlePopUp;

    private TextView descriptionPopUp;

    private Button confirmButton;

    private Button cancelButton;

    protected Context context;

    protected AlertDialog alertDialog;

    private static final int LAYOUT_ID = R.layout.pop_up_dialog;


    public PopUpDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogTheme);
        this.context = context;

        //Inizializzazione della view del dialog
        View view = LayoutInflater.from(context).inflate(LAYOUT_ID, null);
        setView(view);

        // Riferimenti agli elementi di layout del file pop_up_dialog.xml
        titlePopUp = view.findViewById(R.id.textViewTitoloPopUp);
        descriptionPopUp = view.findViewById(R.id.textViewDescrizionePopUp);
        confirmButton = view.findViewById(R.id.buttonConfermaPopUp);
        cancelButton = view.findViewById(R.id.buttonAnnullaPopUp);
    }


    protected void setTitle(String TitlePopUp) {
        titlePopUp.setText(TitlePopUp);
    }

    protected void setDescription(String DescriptionPopUp) {
        descriptionPopUp.setText(DescriptionPopUp);
    }

    protected void setConfirmButton(String ConfirmButton) {
        confirmButton.setVisibility(View.VISIBLE);
        confirmButton.setText(ConfirmButton);
    }

    protected void setCancelButton(String CancelButton) {
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setText(CancelButton);
    }


    public void setOnConfirmButtonClickListener(OnConfirmButtonClickListener onConfirmButtonClickListener) {
        confirmButton.setOnClickListener(v -> {
            if (onConfirmButtonClickListener != null) {
                onConfirmButtonClickListener.onConfirmButtonClicked();
            }
            alertDialog.dismiss();
        });
    }

    public void setOnCancelButtonClickListener(OnCancelButtonClickListener onCancelButtonClickListener) {
        cancelButton.setOnClickListener(v -> {
            if (onCancelButtonClickListener != null) {
                onCancelButtonClickListener.onCancelButtonClicked();
            }
            alertDialog.dismiss();
        });
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


    public interface OnConfirmButtonClickListener {
        void onConfirmButtonClicked();
    }

    public interface OnCancelButtonClickListener {
        void onCancelButtonClicked();
    }


}
