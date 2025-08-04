package views.fragment.profilo;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import views.fragment.AbstractNavigazioneFragment;

public abstract class AsbtractProfiloFragment extends AbstractNavigazioneFragment {

    protected TextView textViewUsernameProfilo;

    protected TextInputEditText textInputEditTextNome;

    protected TextInputEditText textInputEditTextCognome;

    protected TextInputEditText textInputEditTextEmail;


    abstract void modificaProfilo();
    abstract void confermaModificaProfilo();
    abstract void setData();

}

