package views.activity;


import it.uniba.dib.pronuntiapp.R;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import android.os.Bundle;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;

import models.domain.scenariGioco.TemplateScenarioGioco;
import models.domain.profili.Logopedista;
import models.domain.profili.Appuntamento;
import models.domain.esercizi.Esercizio;

import java.util.List;

import com.google.android.material.navigation.NavigationBarView;

import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.ActionBar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.Navigation;


public class LogopedistaActivity extends AbstractAppActivity {

    private LogopedistaViewsModels logopedistaViewsModels;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.backgroundWhite));
        setContentView(R.layout.activity_logopedista);

        //setup di navigazione
        navigationController = Navigation.findNavController(this, R.id.fragmentContainerLogopedista);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        NavigationUI.setupWithNavController(bottomNavigationView, navigationController);
        setOnBackPressedCallback(R.id.pazientiFragment);

        //setup dei dati
        this.logopedistaViewsModels = new ViewModelProvider(this).get(LogopedistaViewsModels.class);

        this.logopedistaViewsModels.setLogopedista((Logopedista) getIntent().getSerializableExtra("mLogopedista"));
        this.logopedistaViewsModels.setAppuntamenti((List<Appuntamento>) getIntent().getSerializableExtra("mAppuntamenti"));
        this.logopedistaViewsModels.setTemplateScenariGioco((List<TemplateScenarioGioco>) getIntent().getSerializableExtra("mTemplateScenariGioco"));
        this.logopedistaViewsModels.setTemplateEsercizi((List<Esercizio>) getIntent().getSerializableExtra("mTemplateEsercizi"));
    }

}

