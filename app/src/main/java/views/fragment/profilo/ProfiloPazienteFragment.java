package views.fragment.profilo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import it.uniba.dib.pronuntiapp.R;
import models.domain.profili.Paziente;
import viewsModels.genitoreViewsModels.GenitoreViewsModels;

public class ProfiloPazienteFragment extends AsbtractProfiloFragment{

    private TextInputEditText textInputEditTextDataNascita;

    private EditText spinnerSesso;

    private TextView textViewDatiBambino;

    private ImageView textViewArrowDown;

    private LinearLayout linearLayoutDatiBambinoClick;

    private LinearLayout linearLayoutContainerBambino;

    private GenitoreViewsModels mGenitoreViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profilo_paziente, container, false);

        this.mGenitoreViewModel = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        textViewUsernameProfilo = view.findViewById(R.id.textInputEditTextUsernameProfiloPaziente);
        textInputEditTextNome = view.findViewById(R.id.textInputEditTextNomeProfiloPaziente);
        textInputEditTextCognome = view.findViewById(R.id.textInputEditTextCognomeProfiloPaziente);
        textInputEditTextEmail = view.findViewById(R.id.textInputEditTextEmailProfiloPaziente);
        textInputEditTextDataNascita = view.findViewById(R.id.textInputEditTextDataNascitaProfiloPaziente);
        spinnerSesso = view.findViewById(R.id.spinnerSessoProfiloPaziente);
        textViewDatiBambino = view.findViewById(R.id.textViewDatiBambino);
        textViewArrowDown = view.findViewById(R.id.arrowImageView);
        linearLayoutDatiBambinoClick = view.findViewById(R.id.llDatiPazientiClick);
        linearLayoutDatiBambinoClick.setOnClickListener(v->onDatiBambiniClick());
        linearLayoutContainerBambino = view.findViewById(R.id.linearLayoutProfiloPaziente);

        setData();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    private void onDatiBambiniClick() {
        if (linearLayoutContainerBambino.getVisibility() == View.VISIBLE) {
            linearLayoutContainerBambino.setVisibility(View.GONE);
            textViewArrowDown.setRotation(0);
        } else {
            linearLayoutContainerBambino.setVisibility(View.VISIBLE);
            textViewArrowDown.setRotation(180);
        }
    }

    @Override
    public void setData() {

        Paziente paziente = mGenitoreViewModel.getPazienteLiveData().getValue();

        textInputEditTextNome.setText(paziente.getNome());
        textInputEditTextNome.setEnabled(false);
        textInputEditTextCognome.setText(paziente.getCognome());
        textInputEditTextCognome.setEnabled(false);
        textViewUsernameProfilo.setText(paziente.getUsername());
        textInputEditTextDataNascita.setText(paziente.getDataNascita().toString());
        textInputEditTextDataNascita.setEnabled(false);
        textInputEditTextEmail.setText(paziente.getEmail());
        textInputEditTextEmail.setEnabled(false);
        spinnerSesso.setText(Character.toString(paziente.getSesso()));
        spinnerSesso.setEnabled(false);
    }

    @Override
    public void modificaProfilo(){

    }

    @Override
    public void confermaModificaProfilo(){
        setData();
    }

}

