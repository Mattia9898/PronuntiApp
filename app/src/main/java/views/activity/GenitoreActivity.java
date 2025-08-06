package views.activity;

import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.Navigation;

import androidx.navigation.ui.AppBarConfiguration;

import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Appuntamento;

import models.domain.profili.Genitore;

import models.domain.profili.Paziente;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

public class GenitoreActivity extends AbstractAppActivity {

    private GenitoreViewsModels mGenitoreViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.backgroundWhite));

        setContentView(R.layout.activity_genitore);

        //setup di navigazione
        navigationController = Navigation.findNavController(this, R.id.fragmentContainerGenitore);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        NavigationUI.setupWithNavController(bottomNavigationView, navigationController);
        setOnBackPressedCallback(R.id.terapieFragment);

        //Setup Dati
        this.mGenitoreViewModel = new ViewModelProvider(this).get(GenitoreViewsModels.class);
        this.mGenitoreViewModel.setGenitore((Genitore) getIntent().getSerializableExtra("mGenitore"));
        this.mGenitoreViewModel.setPaziente((Paziente) getIntent().getSerializableExtra("mPaziente"));
        this.mGenitoreViewModel.setAppuntamenti((List<Appuntamento>) getIntent().getSerializableExtra("mAppuntamenti"));
    }

}
