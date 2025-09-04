package views.fragment.profilo;


import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Logopedista;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import android.text.TextWatcher;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.os.Bundle;


public class ProfiloLogopedistaFragment extends ProfiloImageFragment {

    private TextInputEditText phone;

    private TextInputEditText address;

    private LogopedistaViewsModels logopedistaViewsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profilo_logopedista, container, false);
        setToolBar(view, getString(R.string.tuoProfilo));

        this.logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);
        usernameProfile = view.findViewById(R.id.usernameProfileLogopedista);
        name = view.findViewById(R.id.nameProfileLogopedista);
        surname = view.findViewById(R.id.surnameProfileLogopedista);
        email = view.findViewById(R.id.emailProfileLogopedista);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        imageViewEditProfile = view.findViewById(R.id.imageViewEditProfile);
        buttonEditProfile= view.findViewById(R.id.buttonEditProfileLogopedista);
        setPickMedia();
        phone = view.findViewById(R.id.phoneProfileLogopedista);
        address = view.findViewById(R.id.addressProfileLogopedista);

        setData();

        return view;
    }

    @Override
    public void editProfile() {
        phone.setEnabled(true);
        address.setEnabled(true);

        imageViewProfile.setOnClickListener(v -> pickImage());

        buttonEditProfile.setText(getString(R.string.confirm_modify_profile));
        buttonEditProfile.setOnClickListener(v -> confirmEditProfile());

        imageViewEditProfile.setOnClickListener(v -> pickImage());
        imageViewEditProfile.setVisibility(View.VISIBLE);

        name.requestFocus();

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String addressLogopedista = s.toString();
                logopedistaViewsModels.getLogopedistaLiveData().getValue().setIndirizzo(addressLogopedista);
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumber = s.toString();
                logopedistaViewsModels.getLogopedistaLiveData().getValue().setNumeroCellulare(phoneNumber);
            }
        });
    }

    @Override
    void confirmEditProfile() {

        super.confirmEditProfile();
        logopedistaViewsModels.updateLogopedistaRemoto();
        setData();
    }

    @Override
    public void setData(){

        Logopedista logopedista = logopedistaViewsModels.getLogopedistaLiveData().getValue();

        name.setText(logopedista.getNome());
        name.setEnabled(false);

        surname.setText(logopedista.getCognome());
        surname.setEnabled(false);

        email.setText(logopedista.getEmail());
        email.setEnabled(false);

        usernameProfile.setText(logopedista.getUsername());

        phone.setText(logopedista.getNumeroCellulare());
        phone.setEnabled(false);

        address.setText(logopedista.getIndirizzo());
        address.setEnabled(false);
    }



}