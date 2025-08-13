package views.fragment.profilo;

import android.os.Bundle;

import android.text.Editable;

import android.text.TextWatcher;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Logopedista;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;


public class ProfiloLogopedistaFragment extends ProfiloConImmagineFragment {

    private TextInputEditText textInputEditTextTelefono;
    private TextInputEditText textInputEditTextIndirizzo;

    private LogopedistaViewsModels mLogopedistaViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profilo_logopedista, container, false);
        setToolBar(view, getString(R.string.tuoProfilo));

        this.mLogopedistaViewModel = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        usernameProfile = view.findViewById(R.id.textViewUsernameProfiloLogopedista);
        name = view.findViewById(R.id.textInputEditTextNomeProfiloLogopedista);
        surname = view.findViewById(R.id.textInputEditTextCognomeProfiloLogopedista);
        email = view.findViewById(R.id.textInputEditTextEmailProfiloLogopedista);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        imageViewEditProfile = view.findViewById(R.id.imageViewEditProfile);
        buttonModificaProfilo= view.findViewById(R.id.buttonModificaProfiloLogopedista);
        setPickMedia();

        textInputEditTextTelefono = view.findViewById(R.id.textInputEditTextTelefonoProfiloLogopedista);
        textInputEditTextIndirizzo = view.findViewById(R.id.textInputEditTextIndirizzoProfiloLogopedista);

        setData();

        return view;
    }

    @Override
    public void setData(){
        Logopedista logopedista = mLogopedistaViewModel.getLogopedistaLiveData().getValue();

        name.setText(logopedista.getNome());
        name.setEnabled(false);
        surname.setText(logopedista.getCognome());
        surname.setEnabled(false);
        email.setText(logopedista.getEmail());
        email.setEnabled(false);
        usernameProfile.setText(logopedista.getUsername());
        textInputEditTextTelefono.setText(logopedista.getNumeroCellulare());
        textInputEditTextTelefono.setEnabled(false);
        textInputEditTextIndirizzo.setText(logopedista.getIndirizzo());
        textInputEditTextIndirizzo.setEnabled(false);
    }

    @Override
    public void editProfile() {
        textInputEditTextTelefono.setEnabled(true);
        textInputEditTextIndirizzo.setEnabled(true);

        imageViewProfile.setOnClickListener(v -> pickImage());

        buttonModificaProfilo.setText(getString(R.string.confirm_modify_profile));
        buttonModificaProfilo.setOnClickListener(v -> confirmEditProfile());

        imageViewEditProfile.setOnClickListener(v -> pickImage());
        imageViewEditProfile.setVisibility(View.VISIBLE);

        name.requestFocus();

        textInputEditTextIndirizzo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String indirizzo = s.toString();
                mLogopedistaViewModel.getLogopedistaLiveData().getValue().setIndirizzo(indirizzo);
            }
        });

        textInputEditTextTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String cellulare = s.toString();
                mLogopedistaViewModel.getLogopedistaLiveData().getValue().setNumeroCellulare(cellulare);
            }
        });
    }

    @Override
    void confirmEditProfile() {
        super.confirmEditProfile();

        mLogopedistaViewModel.aggiornaLogopedistaRemoto();
        setData();
    }

}