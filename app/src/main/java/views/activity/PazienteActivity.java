package views.activity;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Matrix;

import android.graphics.PorterDuff;

import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.ImageButton;

import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Paziente;

import models.domain.profili.personaggio.Personaggio;

import viewsModels.pazienteViewsModels.PazienteViewsModels;

import viewsModels.pazienteViewsModels.controller.PersonaggiController;

import views.fragment.userPaziente.classifica.EntryClassifica;

public class PazienteActivity extends AbstractAppActivity {

    private PazienteViewsModels mPazienteViewModel;

    private ImageButton navBarGiochi;

    private ImageButton navBarPersonaggi;

    private ImageButton navBarClassifica;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paziente);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //getWindow().setNavigationBarColor(getColor(R.color.navBarPaziente));

        //Setup Navigazione
        navcontroller = Navigation.findNavController(this, R.id.fragmentContainerPaziente);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_UNLABELED);

        navBarGiochi = findViewById(R.id.bottom_nav_giochi);
        navBarPersonaggi = findViewById(R.id.bottom_nav_personaggi);
        navBarClassifica = findViewById(R.id.bottom_nav_classifica);

        //primo fragment
        highlightNavBarButton(R.id.scenariFragment);

        //onclick navBar custom
        navBarGiochi.setOnClickListener(v -> {
            Navigation.findNavController(this, R.id.fragmentContainerPaziente).navigate(R.id.scenariFragment);
            resetNavBarButtons(R.id.scenariFragment);
        });
        navBarPersonaggi.setOnClickListener(v -> {
            Navigation.findNavController(this, R.id.fragmentContainerPaziente).navigate(R.id.personaggiFragment);
            resetNavBarButtons(R.id.personaggiFragment);
        });
        navBarClassifica.setOnClickListener(v -> {
            Navigation.findNavController(this, R.id.fragmentContainerPaziente).navigate(R.id.classificaFragment);
            resetNavBarButtons(R.id.classificaFragment);
        });

        //NavigationUI.setupWithNavController(bottomNavigationView, navcontroller);
        setOnBackPressedCallback(R.id.scenariFragment);

        //Setup Dati
        this.mPazienteViewModel = new ViewModelProvider(this).get(PazienteViewsModels.class);
        this.mPazienteViewModel.setPaziente((Paziente) getIntent().getSerializableExtra("mPaziente"));
        this.mPazienteViewModel.setPersonaggi((List<Personaggio>) getIntent().getSerializableExtra("mPersonaggi"));
        this.mPazienteViewModel.setClassifica((List<EntryClassifica>) getIntent().getSerializableExtra("mClassifica"));
        this.mPazienteViewModel.setTexturePersonaggioSelezionato(PersonaggiController.getTexturePersonaggioSelezionato(mPazienteViewModel.getListaPersonaggiLiveData().getValue(), mPazienteViewModel.getPazienteLiveData().getValue().getPersonaggiSbloccati()));
    }


    private void resetNavBarButtons(int id){
        if(id == R.id.scenariFragment){
            selectorNavBarButton(navBarGiochi, R.drawable.ic_game);
            navBarClassifica.setScaleX(1f);
            navBarClassifica.setScaleY(1f);
            navBarClassifica.setImageDrawable(getDrawable(R.drawable.ic_classifica_color));
            navBarPersonaggi.setScaleX(1f);
            navBarPersonaggi.setScaleY(1f);
            navBarPersonaggi.setImageDrawable(getDrawable(R.drawable.ic_characters));
        }
        else if(id == R.id.personaggiFragment){
            selectorNavBarButton(navBarPersonaggi, R.drawable.ic_characters);
            navBarClassifica.setScaleX(1f);
            navBarClassifica.setScaleY(1f);
            navBarClassifica.setImageDrawable(getDrawable(R.drawable.ic_classifica_color));
            navBarGiochi.setScaleX(1f);
            navBarGiochi.setScaleY(1f);
            navBarGiochi.setImageDrawable(getDrawable(R.drawable.ic_game));
        }
        else if(id == R.id.classificaFragment){
            selectorNavBarButton(navBarClassifica, R.drawable.ic_classifica_color);
            navBarGiochi.setScaleX(1f);
            navBarGiochi.setScaleY(1f);
            navBarGiochi.setImageDrawable(getDrawable(R.drawable.ic_game));
            navBarPersonaggi.setScaleX(1f);
            navBarPersonaggi.setScaleY(1f);
            navBarPersonaggi.setImageDrawable(getDrawable(R.drawable.ic_characters));
        }
    }

    private void selectorNavBarButton(ImageButton navBarButton, int drawableId){
        navBarButton.setScaleX(1.2f);
        navBarButton.setScaleY(1.2f);
        int strokeWidth = 25;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        Bitmap newStrokedBitmap = Bitmap.createBitmap(originalBitmap.getWidth() + 2 * strokeWidth, originalBitmap.getHeight() + 2 * strokeWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newStrokedBitmap);
        float scaleX = (float) newStrokedBitmap.getWidth() / originalBitmap.getWidth();
        float scaleY = (float) newStrokedBitmap.getHeight() / originalBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
        canvas.drawBitmap(originalBitmap, matrix, null);
        canvas.drawColor(getColor(R.color.backgroundWhite), PorterDuff.Mode.SRC_ATOP);
        canvas.drawBitmap(originalBitmap, strokeWidth, strokeWidth, null);
        navBarButton.setImageBitmap(newStrokedBitmap);
    }


    protected void setOnBackPressedCallback(int id) {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            private boolean doubleBackToExit= false;
            @Override
            public void handleOnBackPressed() {
                highlightNavBarButton(id);
                if (navcontroller.getCurrentDestination().getId() == id && doubleBackToExit) {
                    finishAffinity();
                } else if (navcontroller.getCurrentDestination().getId() == id) {
                    doubleBackToExit = true;
                    Toast.makeText(getApplicationContext(),getString(R.string.closeApp) , Toast.LENGTH_SHORT).show();
                }
                else {
                    navcontroller.navigate(id);
                }
            }
        });
    }

    private void highlightNavBarButton(int id){
        Log.d("PazienteActivity.highlightNavBarButton()", "id: " + id);
        if (id == R.id.scenariFragment) {
            selectorNavBarButton(navBarGiochi, R.drawable.ic_game);
            navBarClassifica.setScaleX(1f);
            navBarClassifica.setScaleY(1f);
            navBarClassifica.setImageDrawable(getDrawable(R.drawable.ic_classifica_color));
            navBarPersonaggi.setScaleX(1f);
            navBarPersonaggi.setScaleY(1f);
            navBarPersonaggi.setImageDrawable(getDrawable(R.drawable.ic_characters));
        }
        else if (id == R.id.personaggiFragment) {
            selectorNavBarButton(navBarPersonaggi, R.drawable.ic_characters);
            navBarClassifica.setScaleX(1f);
            navBarClassifica.setScaleY(1f);
            navBarClassifica.setImageDrawable(getDrawable(R.drawable.ic_classifica_color));
            navBarGiochi.setScaleX(1f);
            navBarGiochi.setScaleY(1f);
            navBarGiochi.setImageDrawable(getDrawable(R.drawable.ic_game));
        }
        else if (id == R.id.classificaFragment) {
            selectorNavBarButton(navBarClassifica, R.drawable.ic_classifica_color);
            navBarGiochi.setScaleX(1f);
            navBarGiochi.setScaleY(1f);
            navBarGiochi.setImageDrawable(getDrawable(R.drawable.ic_game));
            navBarPersonaggi.setScaleX(1f);
            navBarPersonaggi.setScaleY(1f);
            navBarPersonaggi.setImageDrawable(getDrawable(R.drawable.ic_characters));
        }
    }
}
