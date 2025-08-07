package views.activity;

import it.uniba.dib.pronuntiapp.R;

import viewsModels.autenticazioneViewsModels.LoginViewsModels;

import android.graphics.Color;

import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.Navigation;


import android.os.Bundle;


public class AutenticazioneActivity extends AbstractAppActivity {

    private LoginViewsModels loginViewsModels;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.backgroundWhite));

        setContentView(R.layout.activity_autenticazione);

        this.loginViewsModels = new ViewModelProvider(this).get(LoginViewsModels.class);

        navigationController = Navigation.findNavController(this, R.id.fragmentContainerAutenticazione);
        setOnBackPressedCallback(R.id.loginFragment);

    }
}
