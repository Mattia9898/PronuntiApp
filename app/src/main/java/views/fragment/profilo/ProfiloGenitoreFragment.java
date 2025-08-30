package views.fragment.profilo;


import it.uniba.dib.pronuntiapp.R;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;
import models.domain.profili.Genitore;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;

import android.text.TextWatcher;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;


public class ProfiloGenitoreFragment extends ProfiloImageFragment {

    private TextInputEditText phone;

    private GenitoreViewsModels genitoreViewsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profilo_genitore, container, false);
        setToolBar(view, getString(R.string.tuoProfilo));

        this.genitoreViewsModels = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        usernameProfile = view.findViewById(R.id.usernameProfileGenitore);
        name = view.findViewById(R.id.nameProfileGenitore);
        surname = view.findViewById(R.id.surnameProfileGenitore);
        email = view.findViewById(R.id.emailProfileGenitore);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        imageViewEditProfile = view.findViewById(R.id.imageViewEditProfile);
        buttonEditProfile= view.findViewById(R.id.buttonEditProfileGenitore);
        setPickMedia();
        phone = view.findViewById(R.id.phoneProfileGenitore);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    @Override
    void editProfile() {

        phone.setEnabled(true);

        buttonEditProfile.setText(getString(R.string.confirm_modify_profile));
        buttonEditProfile.setOnClickListener(v->confirmEditProfile());

        imageViewProfile.setOnClickListener(v->pickImage());

        imageViewEditProfile.setOnClickListener(v->pickImage());
        imageViewEditProfile.setVisibility(View.VISIBLE);

        //focus automatico per far intendere che si puù modificare
        phone.requestFocus();

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumber = s.toString();
                genitoreViewsModels.getGenitoreLiveData().getValue().setnumeroCellulare(phoneNumber);
            }
        });
    }

    @Override
    void confirmEditProfile() {

        super.confirmEditProfile();
        genitoreViewsModels.updateParent();
        setData();
    }

    @Override
    void setData() {

        Genitore genitore = genitoreViewsModels.getGenitoreLiveData().getValue();

        name.setText(genitore.getNome());
        name.setEnabled(false);

        surname.setText(genitore.getCognome());
        surname.setEnabled(false);

        email.setText(genitore.getEmail());
        email.setEnabled(false);

        usernameProfile.setText(genitore.getUsername());

        phone.setText(genitore.getnumeroCellulare());
        phone.setEnabled(false);
    }


}
