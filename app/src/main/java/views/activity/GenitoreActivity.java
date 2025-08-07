package views.activity;


import it.uniba.dib.pronuntiapp.R;

import java.util.List;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import models.domain.profili.Genitore;
import models.domain.profili.Paziente;
import models.domain.profili.Appuntamento;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.Navigation;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.google.android.material.navigation.NavigationBarView;


public class GenitoreActivity extends AbstractAppActivity {

    private GenitoreViewsModels genitoreViewsModels;


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

        //setup dei dati
        this.genitoreViewsModels = new ViewModelProvider(this).get(GenitoreViewsModels.class);

        this.genitoreViewsModels.setGenitore((Genitore) getIntent().getSerializableExtra("mGenitore"));
        this.genitoreViewsModels.setPaziente((Paziente) getIntent().getSerializableExtra("mPaziente"));
        this.genitoreViewsModels.setAppuntamenti((List<Appuntamento>) getIntent().getSerializableExtra("mAppuntamenti"));
    }

}
