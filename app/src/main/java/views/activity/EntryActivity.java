package views.activity;


import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Profilo;
import models.autenticazione.AutenticazioneSharedPreferences;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;

import views.fragment.CaricamentoFragment;

import viewsModels.autenticazioneViewsModels.LoginViewsModels;

import java.util.concurrent.CompletableFuture;

import androidx.lifecycle.ViewModelProvider;


public class EntryActivity extends AbstractAppActivity {

    private LoginViewsModels loginViewsModels;


    @Override
    protected void onStart() {
        super.onStart();
        verifyPreviousLogin();
    }

    private void verifyPreviousLogin() {
        AutenticazioneSharedPreferences autenticazioneSharedPreferences = new AutenticazioneSharedPreferences(this);
        String email = autenticazioneSharedPreferences.getEmail();
        String password = autenticazioneSharedPreferences.getPassword();

        if (email != null && password != null) {
            loginActivityProfile(email, password);
        } else {
            Intent intent = new Intent(this, AutenticazioneActivity.class);
            startActivity(intent);
        }
    }

    private void loginActivityProfile(String email, String password) {

        CompletableFuture<Boolean> loginCorrect = loginViewsModels.verificaLogin(email, password);

        loginCorrect.thenAccept(isLoginCorrect -> {
            CompletableFuture<Profilo> completableFutureProfilo = loginViewsModels.login();
            completableFutureProfilo.thenAccept(profilo -> {
                Log.d("LoginFragment.loginActivityProfilo()", "Profilo: " + profilo.toString());
                this.runOnUiThread(() -> navigationWithProfile(profilo, this));
            });
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        this.loginViewsModels = new ViewModelProvider(this).get(LoginViewsModels.class);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerEntry, new CaricamentoFragment()).commit();
    }


}

