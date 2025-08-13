package views.fragment.profilo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import it.uniba.dib.pronuntiapp.R;
import models.domain.profili.Genitore;
import models.domain.profili.Paziente;
import viewsModels.genitoreViewsModels.GenitoreViewsModels;
import viewsModels.pazienteViewsModels.PazienteViewsModels;

public class ProfiloGenitoreFragment extends ProfiloConImmagineFragment{

    private TextInputEditText textInputEditTextTelefono;

    private GenitoreViewsModels mGenitoreViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profilo_genitore, container, false);
        setToolBar(view, getString(R.string.tuoProfilo));

        this.mGenitoreViewModel = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        usernameProfile = view.findViewById(R.id.textViewUsernameProfiloGenitore);
        name = view.findViewById(R.id.textInputEditTextNomeProfiloGenitore);
        surname = view.findViewById(R.id.textInputEditTextCognomeProfiloGenitore);
        email = view.findViewById(R.id.textInputEditTextEmailProfiloGenitore);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        imageViewEditProfile = view.findViewById(R.id.imageViewEditProfile);
        buttonModificaProfilo= view.findViewById(R.id.buttonModificaProfiloGenitore);
        setPickMedia();

        textInputEditTextTelefono = view.findViewById(R.id.textInputEditTextTelefonoProfiloGenitore);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    @Override
    void setData() {

        Genitore genitore = mGenitoreViewModel.getGenitoreLiveData().getValue();

        name.setText(genitore.getNome());
        name.setEnabled(false);
        surname.setText(genitore.getCognome());
        surname.setEnabled(false);
        email.setText(genitore.getEmail());
        email.setEnabled(false);
        usernameProfile.setText(genitore.getUsername());
        textInputEditTextTelefono.setText(genitore.getnumeroCellulare());
        textInputEditTextTelefono.setEnabled(false);
    }

    @Override
    void editProfile() {
        textInputEditTextTelefono.setEnabled(true);

        buttonModificaProfilo.setText(getString(R.string.confirm_modify_profile));
        buttonModificaProfilo.setOnClickListener(v->confirmEditProfile());

        imageViewProfile.setOnClickListener(v->pickImage());

        imageViewEditProfile.setOnClickListener(v->pickImage());
        imageViewEditProfile.setVisibility(View.VISIBLE);

        //focus automatico per far capire che si puÃ² modificare
        textInputEditTextTelefono.requestFocus();

        textInputEditTextTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String telefono = s.toString();
                mGenitoreViewModel.getGenitoreLiveData().getValue().setnumeroCellulare(telefono);
            }
        });
    }

    @Override
    void confirmEditProfile() {
        super.confirmEditProfile();

        mGenitoreViewModel.aggiornaGenitoreRemoto();
        setData();
    }

}
