package views.activity;

import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.Navigation;

import androidx.navigation.ui.AppBarConfiguration;

import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.esercizi.Esercizio;

import models.domain.profili.Appuntamento;

import models.domain.profili.Logopedista;

import models.domain.scenariGioco.TemplateScenarioGioco;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

public class LogopedistaActivity extends AbstractAppActivity {
    private LogopedistaViewsModels mLogopedistaViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.backgroundWhite));

        setContentView(R.layout.activity_logopedista);

        //Setup Navigazione
        navcontroller = Navigation.findNavController(this, R.id.fragmentContainerLogopedista);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        NavigationUI.setupWithNavController(bottomNavigationView, navcontroller);
        setOnBackPressedCallback(R.id.pazientiFragment);

        //Setup Dati
        this.mLogopedistaViewModel = new ViewModelProvider(this).get(LogopedistaViewsModels.class);
        this.mLogopedistaViewModel.setLogopedista((Logopedista) getIntent().getSerializableExtra("mLogopedista"));
        this.mLogopedistaViewModel.setAppuntamenti((List<Appuntamento>) getIntent().getSerializableExtra("mAppuntamenti"));
        this.mLogopedistaViewModel.setTemplateScenariGioco((List<TemplateScenarioGioco>) getIntent().getSerializableExtra("mTemplateScenariGioco"));
        this.mLogopedistaViewModel.setTemplateEsercizi((List<Esercizio>) getIntent().getSerializableExtra("mTemplateEsercizi"));
    }

}

