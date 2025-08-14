package views.fragment.profilo;


import it.uniba.dib.pronuntiapp.R;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;
import models.domain.profili.Paziente;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;


public class ProfiloPazienteFragment extends AsbtractProfiloFragment{

    private LinearLayout linearLayoutContainerPaziente;

    private TextInputEditText birthdate;

    private TextView dataPaziente;

    private LinearLayout dataPazienteClick;

    private EditText spinnerSex;

    private ImageView arrowDown;

    private GenitoreViewsModels genitoreViewsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profilo_paziente, container, false);

        this.genitoreViewsModels = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);
        usernameProfile = view.findViewById(R.id.usernameProfilePaziente);
        name = view.findViewById(R.id.nameProfilePaziente);
        surname = view.findViewById(R.id.surnameProfilePaziente);
        email = view.findViewById(R.id.emailProfilePaziente);
        birthdate = view.findViewById(R.id.birthdateProfilePaziente);
        spinnerSex = view.findViewById(R.id.spinnerSexProfilePaziente);
        dataPaziente = view.findViewById(R.id.dataPaziente);
        arrowDown = view.findViewById(R.id.arrowDown);
        dataPazienteClick = view.findViewById(R.id.dataPazienteClick);
        dataPazienteClick.setOnClickListener(v->onDataPazienteClick());
        linearLayoutContainerPaziente = view.findViewById(R.id.linearLayoutContainerPaziente);

        setData();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }


    @Override
    public void editProfile(){

    }

    @Override
    public void confirmEditProfile(){
        setData();
    }

    private void onDataPazienteClick() {
        if (linearLayoutContainerPaziente.getVisibility() == View.VISIBLE) {
            linearLayoutContainerPaziente.setVisibility(View.GONE);
            arrowDown.setRotation(0);
        } else {
            linearLayoutContainerPaziente.setVisibility(View.VISIBLE);
            arrowDown.setRotation(180);
        }
    }

    @Override
    public void setData() {

        Paziente paziente = genitoreViewsModels.getPazienteLiveData().getValue();

        name.setText(paziente.getNome());
        name.setEnabled(false);

        surname.setText(paziente.getCognome());
        surname.setEnabled(false);

        usernameProfile.setText(paziente.getUsername());

        birthdate.setText(paziente.getDataNascita().toString());
        birthdate.setEnabled(false);

        email.setText(paziente.getEmail());
        email.setEnabled(false);

        spinnerSex.setText(Character.toString(paziente.getSesso()));
        spinnerSex.setEnabled(false);
    }


}

