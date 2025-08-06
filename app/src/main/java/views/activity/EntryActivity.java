package views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.CompletableFuture;

import it.uniba.dib.pronuntiapp.R;
import models.autenticazione.AutenticazioneSharedPreferences;
import models.domain.profili.Profilo;
import viewsModels.autenticazioneViewsModels.LoginViewsModels;
import views.fragment.CaricamentoFragment;

public class EntryActivity extends AbstractAppActivity {
    private LoginViewsModels mLoginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        this.mLoginViewModel = new ViewModelProvider(this).get(LoginViewsModels.class);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerEntry, new CaricamentoFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyLoginPrecedente();
    }

    private void verifyLoginPrecedente() {
        AutenticazioneSharedPreferences authSharedPreferences = new AutenticazioneSharedPreferences(this);
        String email = authSharedPreferences.getEmail();
        String password = authSharedPreferences.getPassword();

        if (email != null && password != null) {
            loginActivityProfilo(email, password);
        } else {
            Intent intent = new Intent(this, AutenticazioneActivity.class);
            startActivity(intent);
        }
    }

    private void loginActivityProfilo(String email, String password) {
        CompletableFuture<Boolean> futureIsLoginCorrect = mLoginViewModel.verificaLogin(email, password);
        futureIsLoginCorrect.thenAccept(isLoginCorrect -> {
            CompletableFuture<Profilo> futureProfilo = mLoginViewModel.login();
            futureProfilo.thenAccept(profilo -> {
                Log.d("LoginFragment.loginActivityProfilo()", "Profilo: " + profilo.toString());

                this.runOnUiThread(() -> navigationWithProfile(profilo, this));
            });
        });
    }

}

