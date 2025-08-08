package views.fragment.autenticazione;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.CompletableFuture;

import it.uniba.dib.pronuntiapp.R;
import models.autenticazione.AutenticazioneSharedPreferences;
import models.domain.profili.Profilo;
import views.fragment.AbstractNavigazioneFragment;
import viewsModels.autenticazioneViewsModels.LoginViewsModels;
import views.activity.AbstractAppActivity;
import views.dialog.InfoDialog;

public class LoginFragment extends AbstractNavigazioneFragment {

    private TextInputEditText textInputEditTextEmail;

    private TextInputEditText textInputEditTextPassword;

    private Button buttonLogin;

    private Button buttonToRegister;

    private Button buttonAccessoRapido;

    private LoginViewsModels mLoginViewsModels;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        this.mLoginViewsModels = new ViewModelProvider(requireActivity()).get(LoginViewsModels.class);

        this.textInputEditTextEmail = view.findViewById(R.id.textInputEditTextEmailLogin);
        this.textInputEditTextPassword = view.findViewById(R.id.textInputEditTextPasswordLogin);

        this.buttonLogin = view.findViewById(R.id.buttonLogin);
        this.buttonToRegister = view.findViewById(R.id.buttonToRegister);
        this.buttonAccessoRapido = view.findViewById(R.id.buttonAccessoRapido);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonLogin.setOnClickListener(v -> eseguiLogin());
        buttonAccessoRapido.setOnClickListener(v -> navigateTo(R.id.action_loginFragment_to_avvioRapidoFragment));
        buttonToRegister.setOnClickListener(v -> navigateTo(R.id.action_loginFragment_to_registrazioneFragment));
    }

    private void eseguiLogin() {
        String email = textInputEditTextEmail.getText().toString();
        String password = textInputEditTextPassword.getText().toString();

        loginActivityProfilo(email, password);
    }

    private void loginActivityProfilo(String email, String password) {
        CompletableFuture<Boolean> futureIsLoginCorrect = mLoginViewsModels.verificaLogin(email, password);
        futureIsLoginCorrect.thenAccept(isLoginCorrect -> {
            if (!isLoginCorrect) {
                InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.erroreLoginCredenziali), getString(R.string.tastoRiprova));
                infoDialog.show();
                infoDialog.setOnConfirmButtonClickListener(null);
            }
            else {
                AutenticazioneSharedPreferences autenticazioneSharedPreferences = new AutenticazioneSharedPreferences(requireActivity());
                autenticazioneSharedPreferences.salvaCredenziali(email, password);

                CompletableFuture<Profilo> futureProfilo = mLoginViewsModels.login();
                futureProfilo.thenAccept(profilo -> {
                    Log.d("LoginFragment.loginActivityProfilo()", "Profilo: " + profilo.toString());

                    getActivity().runOnUiThread(() -> ((AbstractAppActivity) getActivity()).navigationWithProfile(profilo, getActivity()));
                });
            }
        });
    }



}
