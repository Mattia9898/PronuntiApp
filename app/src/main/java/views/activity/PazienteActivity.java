package views.activity;


import it.uniba.dib.pronuntiapp.R;

import java.util.List;

import viewsModels.pazienteViewsModels.PazienteViewsModels;
import viewsModels.pazienteViewsModels.controller.CharactersController;

import models.domain.profili.Paziente;
import models.domain.profili.personaggio.Personaggio;

import views.fragment.userPaziente.ranking.Ranking;

import com.google.android.material.navigation.NavigationBarView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.activity.OnBackPressedCallback;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ImageButton;


public class PazienteActivity extends AbstractAppActivity {

    private PazienteViewsModels pazienteViewsModels;

    private ImageButton navigationBarGames;

    private ImageButton navigationBarCharacters;

    private ImageButton navigationBarRanking;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paziente);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        //setup di navigazione
        navigationController = Navigation.findNavController(this, R.id.fragmentContainerPaziente);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_UNLABELED);

        navigationBarGames = findViewById(R.id.bottomNavigationBarGames);
        navigationBarCharacters = findViewById(R.id.bottomNavigationBarCharacters);
        navigationBarRanking = findViewById(R.id.bottomNavigationBarRanking);

        //first fragment
        highlightsNavigationBarButton(R.id.fragmentScenarioGenerico);

        navigationBarGames.setOnClickListener(v -> {
            Navigation.findNavController(this, R.id.fragmentContainerPaziente).navigate(R.id.fragmentScenarioGenerico);
            resetNavigationBarButton(R.id.fragmentScenarioGenerico);
        });

        navigationBarCharacters.setOnClickListener(v -> {
            Navigation.findNavController(this, R.id.fragmentContainerPaziente).navigate(R.id.personaggiFragment);
            resetNavigationBarButton(R.id.personaggiFragment);
        });

        navigationBarRanking.setOnClickListener(v -> {
            Navigation.findNavController(this, R.id.fragmentContainerPaziente).navigate(R.id.classificaFragment);
            resetNavigationBarButton(R.id.classificaFragment);
        });

        setOnBackPressedCallback(R.id.fragmentScenarioGenerico);

        //setup dei dati
        this.pazienteViewsModels = new ViewModelProvider(this).get(PazienteViewsModels.class);

        this.pazienteViewsModels.setPaziente((Paziente) getIntent().getSerializableExtra("patient"));
        this.pazienteViewsModels.setPersonaggi((List<Personaggio>) getIntent().getSerializableExtra("characters"));
        this.pazienteViewsModels.setClassifica((List<Ranking>) getIntent().getSerializableExtra("ranking"));

        this.pazienteViewsModels.setTexturePersonaggioSelezionato(CharactersController.getTextureSelectedCharacter(
                                pazienteViewsModels.getListaPersonaggiLiveData().getValue(),
                                pazienteViewsModels.getPazienteLiveData().getValue().getPersonaggiSbloccati()));
    }


    private void selectorNavigationBarButton(ImageButton navigationBarButton, int drawableId){

        navigationBarButton.setScaleX(1.2f);
        navigationBarButton.setScaleY(1.2f);

        int strokeWidthAttribute = 25;

        Bitmap initialBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        Bitmap newBitmap = Bitmap.createBitmap(initialBitmap.getWidth() + 2 * strokeWidthAttribute, initialBitmap.getHeight() + 2 * strokeWidthAttribute, Bitmap.Config.ARGB_8888);

        float scaleX = (float) newBitmap.getWidth() / initialBitmap.getWidth();
        float scaleY = (float) newBitmap.getHeight() / initialBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);

        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(initialBitmap, matrix, null);
        canvas.drawColor(getColor(R.color.backgroundWhite), PorterDuff.Mode.SRC_ATOP);
        canvas.drawBitmap(initialBitmap, strokeWidthAttribute, strokeWidthAttribute, null);

        navigationBarButton.setImageBitmap(newBitmap);
    }

    private void highlightsNavigationBarButton(int id){

        Log.d("PazienteActivity.highlightNavBarButton()", "id: " + id);

        if (id == R.id.fragmentScenarioGenerico) {
            selectorNavigationBarButton(navigationBarGames, R.drawable.icona_game);
            navigationBarRanking.setScaleX(1f);
            navigationBarRanking.setScaleY(1f);
            navigationBarRanking.setImageDrawable(getDrawable(R.drawable.icona_classifica_colori));
            navigationBarCharacters.setScaleX(1f);
            navigationBarCharacters.setScaleY(1f);
            navigationBarCharacters.setImageDrawable(getDrawable(R.drawable.icona_personaggi_sbloccati));
        }
        else if (id == R.id.personaggiFragment) {
            selectorNavigationBarButton(navigationBarCharacters, R.drawable.icona_personaggi_sbloccati);
            navigationBarRanking.setScaleX(1f);
            navigationBarRanking.setScaleY(1f);
            navigationBarRanking.setImageDrawable(getDrawable(R.drawable.icona_classifica_colori));
            navigationBarGames.setScaleX(1f);
            navigationBarGames.setScaleY(1f);
            navigationBarGames.setImageDrawable(getDrawable(R.drawable.icona_game));
        }
        else if (id == R.id.classificaFragment) {
            selectorNavigationBarButton(navigationBarRanking, R.drawable.icona_classifica_colori);
            navigationBarGames.setScaleX(1f);
            navigationBarGames.setScaleY(1f);
            navigationBarGames.setImageDrawable(getDrawable(R.drawable.icona_game));
            navigationBarCharacters.setScaleX(1f);
            navigationBarCharacters.setScaleY(1f);
            navigationBarCharacters.setImageDrawable(getDrawable(R.drawable.icona_personaggi_sbloccati));
        }
    }

    private void resetNavigationBarButton(int id){
        if(id == R.id.fragmentScenarioGenerico){
            selectorNavigationBarButton(navigationBarGames, R.drawable.icona_game);
            navigationBarRanking.setScaleX(1f);
            navigationBarRanking.setScaleY(1f);
            navigationBarRanking.setImageDrawable(getDrawable(R.drawable.icona_classifica_colori));
            navigationBarCharacters.setScaleX(1f);
            navigationBarCharacters.setScaleY(1f);
            navigationBarCharacters.setImageDrawable(getDrawable(R.drawable.icona_personaggi_sbloccati));
        }
        else if(id == R.id.personaggiFragment){
            selectorNavigationBarButton(navigationBarCharacters, R.drawable.icona_personaggi_sbloccati);
            navigationBarRanking.setScaleX(1f);
            navigationBarRanking.setScaleY(1f);
            navigationBarRanking.setImageDrawable(getDrawable(R.drawable.icona_classifica_colori));
            navigationBarGames.setScaleX(1f);
            navigationBarGames.setScaleY(1f);
            navigationBarGames.setImageDrawable(getDrawable(R.drawable.icona_game));
        }
        else if(id == R.id.classificaFragment){
            selectorNavigationBarButton(navigationBarRanking, R.drawable.icona_classifica_colori);
            navigationBarGames.setScaleX(1f);
            navigationBarGames.setScaleY(1f);
            navigationBarGames.setImageDrawable(getDrawable(R.drawable.icona_game));
            navigationBarCharacters.setScaleX(1f);
            navigationBarCharacters.setScaleY(1f);
            navigationBarCharacters.setImageDrawable(getDrawable(R.drawable.icona_personaggi_sbloccati));
        }
    }


    protected void setOnBackPressedCallback(int id) {

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            private boolean doubleBackToExit = false;

            @Override
            public void handleOnBackPressed() {
                highlightsNavigationBarButton(id);
                if (navigationController.getCurrentDestination().getId() == id && doubleBackToExit) {
                    finishAffinity();
                } else if (navigationController.getCurrentDestination().getId() == id) {
                    doubleBackToExit = true;
                    Toast.makeText(getApplicationContext(),getString(R.string.closeApp) , Toast.LENGTH_SHORT).show();
                }
                else {
                    navigationController.navigate(id);
                }
            }
        });
    }


}
