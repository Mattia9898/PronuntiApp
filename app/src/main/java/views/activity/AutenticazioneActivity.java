package views.activity;

import android.graphics.Color;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.Navigation;

import it.uniba.dib.pronuntiapp.R;

import viewsModels.autenticazioneViewsModels.LoginViewsModels;

public class AutenticazioneActivity extends AbstractAppActivity {

    private LoginViewsModels mLoginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.backgroundWhite));


        setContentView(R.layout.activity_autenticazione);

        this.mLoginViewModel = new ViewModelProvider(this).get(LoginViewsModels.class);

        navigationController = Navigation.findNavController(this, R.id.fragmentContainerAutenticazione);
        setOnBackPressedCallback(R.id.loginFragment);

    }
}
