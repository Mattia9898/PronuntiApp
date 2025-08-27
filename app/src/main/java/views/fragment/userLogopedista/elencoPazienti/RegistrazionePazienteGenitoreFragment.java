package views.fragment.userLogopedista.elencoPazienti;


import it.uniba.dib.pronuntiapp.R;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import models.domain.profili.Genitore;
import models.domain.profili.Logopedista;
import models.domain.profili.Paziente;

import views.dialog.InfoDialog;
import views.fragment.AbstractNavigationBetweenFragment;
import views.fragment.CustomDate;

import static viewsModels.autenticazioneViewsModels.RegistrazioneViewsModels.verificaRegistrazione;
import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;
import viewsModels.logopedistaViewsModels.controller.RegistrazionePazienteGenitoreController;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import android.widget.Button;
import android.widget.Spinner;

import java.time.LocalDate;

import java.util.concurrent.CompletableFuture;


public class RegistrazionePazienteGenitoreFragment extends AbstractNavigationBetweenFragment {

    private TextInputEditText namePatient;

    private TextInputEditText surnamePatient;

    private TextInputEditText emailPatient;

    private TextInputEditText usernamePatient;

    private TextInputEditText passwordPatient;

    private TextInputEditText confirmPasswordPatient;

    private TextInputEditText agePatient;

    private TextInputEditText birthdatePatient;


    private TextInputEditText nameParent;

    private TextInputEditText surnameParent;

    private TextInputEditText emailParent;

    private TextInputEditText usernameParent;

    private TextInputEditText passwordParent;

    private TextInputEditText confirmPasswordParent;

    private TextInputEditText phoneNumberParent;

    private Spinner spinnerSexPatient;

    private Button buttonRegisterPatientAndParent;

    private LogopedistaViewsModels logopedistaViewsModels;

    private RegistrazionePazienteGenitoreController registrazionePazienteGenitoreController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registrazione_paziente_genitore, container, false);
        setToolBar(view, getString(R.string.registrazionePaziente));

        this.logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);
        this.registrazionePazienteGenitoreController = logopedistaViewsModels.getRegistrazionePazienteGenitoreController();

        this.namePatient = view.findViewById(R.id.namePatient);
        this.surnamePatient = view.findViewById(R.id.surnamePatient);
        this.emailPatient = view.findViewById(R.id.emailPatient);
        this.usernamePatient = view.findViewById(R.id.usernamePatient);
        this.passwordPatient = view.findViewById(R.id.passwordPatient);
        this.confirmPasswordPatient = view.findViewById(R.id.confirmPasswordPatient);
        this.agePatient = view.findViewById(R.id.agePatient);
        this.spinnerSexPatient = view.findViewById(R.id.spinnerSexPatient);
        this.birthdatePatient = view.findViewById(R.id.birthdatePatient);

        this.nameParent = view.findViewById(R.id.nameParent);
        this.surnameParent = view.findViewById(R.id.surnameParent);
        this.emailParent = view.findViewById(R.id.emailParent);
        this.usernameParent = view.findViewById(R.id.usernameParent);
        this.passwordParent = view.findViewById(R.id.passwordParent);
        this.confirmPasswordParent = view.findViewById(R.id.confirmPasswordParent);
        this.phoneNumberParent = view.findViewById(R.id.phoneNumberParent);

        this.buttonRegisterPatientAndParent = view.findViewById(R.id.buttonRegisterPatientAndParent);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        birthdatePatient.setOnClickListener(v -> CustomDate.datePickerDialog(getContext(), birthdatePatient));

        buttonRegisterPatientAndParent.setOnClickListener(v -> {
            Logopedista logopedista = logopedistaViewsModels.getLogopedistaLiveData().getValue();
            String idLogopedista = logopedista.getIdProfilo();

            executeRegistrationPatient(idLogopedista).thenAccept(patient -> {
                executeRegistrationParent(idLogopedista, patient.getIdProfilo()).thenAccept(parent -> {
                    patient.setGenitore(parent);

                    // una volta completata la registrazione e aver premuto con successo il button per la registrazione di
                    // paziente e genitore, tornerà al fragment precedente (fragment_pazienti.xml)
                    registrazionePazienteGenitoreController.
                            reLogLogopedista(logopedista.getEmail(), logopedista.getPassword()).thenAccept(userId -> {
                                logopedista.addPaziente(patient);
                                logopedista.aggiornaClassificaPazienti();
                        logopedistaViewsModels.aggiornaLogopedistaRemoto();
                        getActivity().runOnUiThread(() -> navigationTo(R.id.action_registrazionePazienteGenitoreFragment_to_pazientiFragment));
                    });

                });
            });
        });
    }


    public void showDialogErrorFields(int typeError) {

        String messageError = "";

        switch (typeError) {
            case 1:
                messageError = getString(R.string.erroreRegistrazionePazienteCampiMancanti);
                break;
            case 2:
                messageError = getString(R.string.erroreRegistrazionePazientePasswordDifformi);
                break;
            case 3:
                messageError = getString(R.string.erroreRegistrazionePazienteEtaNonValida);
                break;
            case 4:
                messageError = getString(R.string.erroreRegistrazionePazienteDataNonValida);
                break;
            case 5:
                messageError = getString(R.string.erroreRegistrazionePazienteSessoNonValido);
                break;
            case 6:
                messageError = getString(R.string.erroreRegistrazioneGenitoreCampiMancanti);
                break;
            case 7:
                messageError = getString(R.string.erroreRegistrazioneGenitorePasswordDifformi);
                break;
            case 8:
                messageError = getString(R.string.erroreRegistrazionePazienteAutenticazione);
                break;
            case 9:
                messageError = getString(R.string.erroreRegistrazioneGenitoreAutenticazione);
                break;
        }

        InfoDialog infoDialog = new InfoDialog(getContext(), messageError, getString(R.string.tastoRiprova));
        infoDialog.show();
        infoDialog.setOnConfirmButtonClickListener(null);
    }

    private CompletableFuture<Genitore> executeRegistrationParent(String idLogopedista, String idPaziente) {

        String name = nameParent.getText().toString();
        String surname = surnameParent.getText().toString();
        String email = emailParent.getText().toString();
        String username = usernameParent.getText().toString();
        String password = passwordParent.getText().toString();
        String confirmPassword = confirmPasswordParent.getText().toString();
        String phoneNumber = phoneNumberParent.getText().toString();

        CompletableFuture<Genitore> completableFutureParent = new CompletableFuture<>();

        int statusFields = registrazionePazienteGenitoreController.
                verificaCorrettezzaCampiGenitore(name, surname, email, username,
                        password, confirmPassword, phoneNumber);

        if (statusFields != 0) {
            showDialogErrorFields(statusFields);
        }
        else {

            CompletableFuture<String> completableFutureRegistrationCorrect = verificaRegistrazione(email, password);

            completableFutureRegistrationCorrect.thenAccept(userId -> {
                if (userId == null) {
                    showDialogErrorFields(9);
                } else {
                    Genitore genitore = registrazionePazienteGenitoreController.
                            registrazioneGenitore(userId, name, surname, username,
                                    email, password, phoneNumber, idLogopedista, idPaziente);

                    Log.d("RegistrazionePazienteGenitoreFragment.eseguiRegistrazioneGenitore()", genitore.toString());

                    completableFutureParent.complete(genitore);
                }
            });
        }

        return completableFutureParent;
    }

    private CompletableFuture<Paziente> executeRegistrationPatient(String idLogopedista) {

        String name = namePatient.getText().toString();
        String surname = surnamePatient.getText().toString();
        String email = emailPatient.getText().toString();
        String username = usernamePatient.getText().toString();
        String password = passwordPatient.getText().toString();
        String confirmPassword = confirmPasswordPatient.getText().toString();

        CompletableFuture<Paziente> completableFuturePatient = new CompletableFuture<>();

        int statusFields = registrazionePazienteGenitoreController.
                verificaCorrettezzaCampiPaziente(name, surname, email, username,
                        password, confirmPassword, agePatient.getText().toString(),
                        birthdatePatient.getText().toString(), spinnerSexPatient.getSelectedItem().toString());

        if (statusFields != 0) {
            showDialogErrorFields(statusFields);
        }
        else {
            char sex = spinnerSexPatient.getSelectedItem().toString().charAt(0);
            LocalDate birthdate = LocalDate.parse(birthdatePatient.getText().toString());

            int age = Integer.parseInt(agePatient.getText().toString());
            CompletableFuture<String> completableFutureRegistrationCorrect = verificaRegistrazione(email, password);

            completableFutureRegistrationCorrect.thenAccept(userId -> {
                if (userId == null) {
                    showDialogErrorFields(8);
                } else {
                    Paziente paziente = registrazionePazienteGenitoreController.
                            registrazionePaziente(userId, name, surname, username,
                                    email, password, age, birthdate,
                                    sex, idLogopedista);

                    Log.d("RegistrazionePazienteGenitoreFragment.eseguiRegistrazionePaziente()", paziente.toString());

                    completableFuturePatient.complete(paziente);
                }
            });
        }

        return completableFuturePatient;
    }


}

