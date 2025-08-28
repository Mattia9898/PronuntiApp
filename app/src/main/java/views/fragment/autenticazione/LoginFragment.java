package views.fragment.autenticazione;


import it.uniba.dib.pronuntiapp.R;

import android.os.Bundle;

import models.domain.profili.Profilo;
import models.autenticazione.AutenticazioneSharedPreferences;
import views.activity.AbstractAppActivity;
import viewsModels.autenticazioneViewsModels.LoginViewsModels;
import views.fragment.AbstractNavigationBetweenFragment;
import views.dialog.InfoDialog;

import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.CompletableFuture;

import android.widget.Button;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;


public class LoginFragment extends AbstractNavigationBetweenFragment {

    private LoginViewsModels loginViewsModels;

    private Button buttonLogin;

    private Button buttonRegister;

    private Button buttonRapidAccess;

    private TextInputEditText emailFragmentLogin;

    private TextInputEditText passwordFragmentLogin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        this.loginViewsModels = new ViewModelProvider(requireActivity()).get(LoginViewsModels.class);
        this.emailFragmentLogin = view.findViewById(R.id.emailFragmentLogin);
        this.passwordFragmentLogin = view.findViewById(R.id.passwordFragmentLogin);
        this.buttonLogin = view.findViewById(R.id.buttonLogin);
        this.buttonRegister = view.findViewById(R.id.buttonRegistration);
        this.buttonRapidAccess = view.findViewById(R.id.buttonRapidAccess);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonLogin.setOnClickListener(v -> executeLogin());
        buttonRapidAccess.setOnClickListener(v -> navigationTo(R.id.action_loginFragment_to_avvioRapidoFragment));
        buttonRegister.setOnClickListener(v -> navigationTo(R.id.action_loginFragment_to_registrazioneFragment));
    }

    private void executeLogin() {
        String email = emailFragmentLogin.getText().toString();
        String password = passwordFragmentLogin.getText().toString();

        loginActivityProfile(email, password);
    }

    private void loginActivityProfile(String email, String password) {
        CompletableFuture<Boolean> loginCompletableFuture = loginViewsModels.checkUserLogin(email, password);
        loginCompletableFuture.thenAccept(isLoginCorrect -> {
            if (!isLoginCorrect) {
                InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.erroreLoginCredenziali), getString(R.string.tastoRiprova));
                infoDialog.show();
                infoDialog.setOnConfirmButtonClickListener(null);
            }
            else {
                AutenticazioneSharedPreferences autenticazioneSharedPreferences = new AutenticazioneSharedPreferences(requireActivity());
                autenticazioneSharedPreferences.saveCredentials(email, password);

                CompletableFuture<Profilo> profiloCompletableFuture = loginViewsModels.userLogin();
                profiloCompletableFuture.thenAccept(profilo -> {
                    Log.d("LoginFragment.loginActivityProfilo()", "Profilo: " + profilo.toString());

                    getActivity().runOnUiThread(() -> ((AbstractAppActivity) getActivity()).navigationWithProfile(profilo, getActivity()));
                });
            }
        });
    }



}
