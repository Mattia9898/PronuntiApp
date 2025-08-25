package views.fragment.autenticazione;


import it.uniba.dib.pronuntiapp.R;

import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.CompletableFuture;

import views.dialog.InfoDialog;
import views.fragment.AbstractNavigationFragment;
import views.activity.LogopedistaActivity;

import models.domain.profili.Logopedista;
import models.autenticazione.AutenticazioneSharedPreferences;

import android.widget.Button;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.util.Log;
import android.content.Intent;

import com.google.android.material.textfield.TextInputEditText;

import viewsModels.autenticazioneViewsModels.RegistrazioneViewsModels;


public class RegistrazioneFragment extends AbstractNavigationFragment {


    private RegistrazioneViewsModels registrazioneViewsModels;

    private TextInputEditText usernameRegistration;

    private TextInputEditText nameRegistration;

    private TextInputEditText surnameRegistration;

    private TextInputEditText phoneRegistration;

    private TextInputEditText addressRegistration;

    private TextInputEditText emailRegistration;

    private TextInputEditText passwordRegistration;

    private TextInputEditText confirmPasswordRegistration;

    private Button buttonGoToLogin;

    private Button buttonRegister;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registrazione, container, false);
        setToolBar(view, getString(R.string.registrazione));

        this.registrazioneViewsModels = new ViewModelProvider(this).get(RegistrazioneViewsModels.class);
        this.nameRegistration = view.findViewById(R.id.nameFragmentRegistration);
        this.surnameRegistration = view.findViewById(R.id.surnameFragmentRegistration);
        this.emailRegistration = view.findViewById(R.id.emailFragmentRegistration);
        this.usernameRegistration = view.findViewById(R.id.usernameFragmentRegistration);
        this.passwordRegistration = view.findViewById(R.id.passwordFragmentRegistration);
        this.confirmPasswordRegistration = view.findViewById(R.id.confirmPasswordFragmentRegistration);
        this.phoneRegistration = view.findViewById(R.id.phoneFragmentRegistration);
        this.addressRegistration = view.findViewById(R.id.addressFragmentRegistration);
        this.buttonRegister = view.findViewById(R.id.buttonRegister);
        this.buttonGoToLogin = view.findViewById(R.id.buttonGoToLogin);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonRegister.setOnClickListener(v -> executeRegistration());
        buttonGoToLogin.setOnClickListener(v -> navigateTo(R.id.action_registrazioneFragment_to_loginFragment));
    }

    public void dialogErrorFields(int typeError) {

        String errorMessage = "";

        switch (typeError) {
            case 1:
                errorMessage = getString(R.string.erroreRegistrazioneLogopedistaCampiIncompleti);
                break;
            case 2:
                errorMessage = getString(R.string.erroreRegistrazioneLogopedistaPasswordDifformi);
                break;
            case 3:
                errorMessage = getString(R.string.erroreRegistrazioneLogopedistaAutenticazione);
                break;
        }

        InfoDialog infoDialog = new InfoDialog(getContext(), errorMessage, getString(R.string.tastoRiprova));
        infoDialog.show();
        infoDialog.setOnConfirmButtonClickListener(null);
    }

    private void executeRegistration() {

        String name = nameRegistration.getText().toString();
        String surname = surnameRegistration.getText().toString();
        String email = emailRegistration.getText().toString();
        String username = usernameRegistration.getText().toString();
        String password = passwordRegistration.getText().toString();
        String confirmPassword = confirmPasswordRegistration.getText().toString();
        String phone = phoneRegistration.getText().toString();
        String address = addressRegistration.getText().toString();

        int statusRightFields = registrazioneViewsModels.checkRightFieldsLogopedista
                (name, surname, username, email, password, confirmPassword, phone, address);

        if (statusRightFields != 0) {
            dialogErrorFields(statusRightFields);
        }
        else {
            CompletableFuture<String> correctRegistrationCompletableFuture = RegistrazioneViewsModels.verificaRegistrazione(email, password);
            correctRegistrationCompletableFuture.thenAccept(userId -> {
                if (userId == null) {
                    dialogErrorFields(3);
                }
                else {
                    Logopedista logopedista = registrazioneViewsModels.registrazioneLogopedista
                            (userId, name, surname, username, email, password, phone, address);

                    Log.d("RegistrazioneFragment.eseguiRegistrazione()", "Logopedista: " + logopedista.toString());

                    AutenticazioneSharedPreferences autenticazioneSharedPreferences = new AutenticazioneSharedPreferences(requireActivity());
                    autenticazioneSharedPreferences.saveCredentials(email, password);

                    getActivity().runOnUiThread(() -> {
                        Intent intent = new Intent(getActivity(), LogopedistaActivity.class);
                        intent.putExtra("profilo", logopedista);
                        startActivity(intent);
                    });
                }
            });
        }
    }


}
