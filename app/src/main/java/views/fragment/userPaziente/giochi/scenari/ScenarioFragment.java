package views.fragment.userPaziente.giochi.scenari;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;
import models.domain.esercizi.EsercizioDenominazioneImmagini;
import models.domain.esercizi.EsercizioRealizzabile;
import models.domain.esercizi.EsercizioSequenzaParole;
import models.domain.scenariGioco.ScenarioGioco;
import models.domain.terapie.Terapia;
import viewsModels.pazienteViewsModels.PazienteViewsModels;
import views.fragment.AbstractNavigationFragment;
import views.fragment.userPaziente.giochi.FineScenarioView;

public class ScenarioFragment extends AbstractNavigationFragment {
    private float xDelta, yDelta;
    private float bottomHeight;
    private float topHeight;
    private ImageView personaggioImageView;
    private ImageView posizioneGioco1ImageView, posizioneGioco2ImageView, posizioneGioco3ImageView;
    private float personaggioX, personaggioY, personaggioWidth, personaggioHeight;
    private ConstraintLayout constraintLayout;
    private Vibrator vibrator;
    private boolean isVibrating = false;
    private PazienteViewsModels mPazienteViewsModels;
    private Bundle bundle;
    private ScenarioGioco scenarioGioco;
    private FineScenarioView fineScenarioView;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenario, container, false);
        bundle = getArguments();
        this.mPazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);

        fineScenarioView = view.findViewById(R.id.fineScenarioView);
        fineScenarioView.setVisibility(View.GONE);

        constraintLayout = view.findViewById(R.id.constraintLayoutScenario);

        personaggioImageView = view.findViewById(R.id.imageViewPersonaggio);

        posizioneGioco1ImageView = view.findViewById(R.id.posizione_primo_esercizio);
        posizioneGioco2ImageView = view.findViewById(R.id.posizione_secondo_esercizio);
        posizioneGioco3ImageView = view.findViewById(R.id.posizione_terzo_esercizio);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int scenarioIndex = bundle.getInt("indiceScenarioCorrente");
        int terapiaIndex = bundle.getInt("indiceTerapia");
        Log.d("indiceScenarioCorrente","indiceScenarioCorrente "+String.valueOf(scenarioIndex));
        mPazienteViewsModels.getTexturePersonaggioSelezionatoLiveData().observe(getViewLifecycleOwner(), texture -> {
            Picasso.get().load(texture).into(personaggioImageView);
        });

        mPazienteViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {

            List<Terapia> terapie = paziente.getTerapie();
            Terapia terapia  = terapie.get(terapiaIndex);
            scenarioGioco = terapia.getListScenariGioco().get(scenarioIndex);

            String immagineSfondo = scenarioGioco.getSfondoImmagine();
            String immagineTappa1 = scenarioGioco.getImmagine1();
            String immagineTappa2 = scenarioGioco.getImmagine2();
            String immagineTappa3 = scenarioGioco.getImmagine3();
            Glide.with(this).load(immagineSfondo).centerCrop().into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    constraintLayout.setBackground(resource);
                }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {}
            });



            Glide.with(this).load(immagineTappa1).centerInside().into(posizioneGioco1ImageView);
            Glide.with(this).load(immagineTappa2).centerInside().into(posizioneGioco2ImageView);
            Glide.with(this).load(immagineTappa3).centerInside().into(posizioneGioco3ImageView);

            //se è completato, allora bisogna disattivarlo
            if (isCompletato(0)) {
                disableImageView(posizioneGioco1ImageView);
            }
            if (isCompletato(1)) {
                disableImageView(posizioneGioco2ImageView);
            }
            if (isCompletato(2)) {
                disableImageView(posizioneGioco3ImageView);
            }

            mPazienteViewsModels.getTexturePersonaggioSelezionatoLiveData().observe(getViewLifecycleOwner(), texture -> {
                Picasso.get().load(texture).into(personaggioImageView);
            });

            vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            personaggioImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    personaggioImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int dp=125;
                    topHeight= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
                    bottomHeight = getResources().getDimension(R.dimen.nav_bar_height);
                    bottomHeight += bottomHeight*0.2f;
                    // Abilita il drag dell'immagine del personaggio
                    enableImageDrag(personaggioImageView);
                }

            });

            Bundle bundleFineScenario = getArguments();
            Log.d("Bundle", "onViewCreated fine scenario: " + bundleFineScenario);
            if(bundleFineScenario != null) {
                if(bundleFineScenario.containsKey("checkFineScenario") && bundleFineScenario.getBoolean("checkFineScenario")) {
                    showFineScenario();
                }
            }

        });
    }

    private void showFineScenario(){
        fineScenarioView.setVisibility(View.VISIBLE);
        Log.d("FineScenario", "fine scenario" + scenarioGioco.getlistEsercizioRealizzabile().toString());
        int ricompensaFinale = scenarioGioco.getRicompensaFinale();
        fineScenarioView.showFineScenario(ricompensaFinale, posizioneGioco1ImageView, posizioneGioco2ImageView, posizioneGioco3ImageView);
    }

    private void disableImageView(ImageView view) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        view.setColorFilter(filter);
    }

    private boolean isCompletato(int index){
        if(scenarioGioco.getlistEsercizioRealizzabile().get(index).getRisultatoEsercizio()!=null){
            Log.d("Esercizio", index + " completato");
            return true;
        }else{
            Log.d("Esercizio", index + " non completato");
            return false;
        }
    }

    private void setPersonaggioPosition() {
        personaggioX = personaggioImageView.getX();
        personaggioY = personaggioImageView.getY();
        personaggioWidth = personaggioImageView.getWidth();
        personaggioHeight = personaggioImageView.getHeight();
    }

    private void highlightPosizione(ImageView imageView) {
        ingrandisciPosizione(imageView);
        vibrate();
        imageView.setBackground(getContext().getDrawable(R.drawable.esercizio_highlight_background));
    }

    private void ingrandisciPosizione(ImageView imageView){
        imageView.setScaleX(1.3f);
        imageView.setScaleY(1.3f);
    }

    private void ridimensionaPosizione(ImageView imageView){
        imageView.setScaleX(1f);
        imageView.setScaleY(1f);
    }

    private boolean isPersonaggioInAreaPrimoEsercizio() {
        setPersonaggioPosition();
        float areaPrimoEsercizioX = posizioneGioco1ImageView.getX();
        float areaPrimoEsercizioY = posizioneGioco1ImageView.getY();
        float areaPrimoEsercizioWidth = posizioneGioco1ImageView.getWidth();
        float areaPrimoEsercizioHeight = posizioneGioco1ImageView.getHeight();

        return personaggioX < (areaPrimoEsercizioX + areaPrimoEsercizioWidth) &&
                (personaggioX + personaggioWidth) > areaPrimoEsercizioX &&
                personaggioY < (areaPrimoEsercizioY + areaPrimoEsercizioHeight) &&
                (personaggioY + personaggioHeight) > areaPrimoEsercizioY;
    }

    private boolean isPersonaggioInAreaSecondaEsercizio() {
        setPersonaggioPosition();
        float areaSecondoEsercizioX = posizioneGioco2ImageView.getX();
        float areaSecondoEsercizioY = posizioneGioco2ImageView.getY();
        float areaSecondoEsercizioWidth = posizioneGioco2ImageView.getWidth();
        float areaSecondoEsercizioHeight = posizioneGioco2ImageView.getHeight();

        return personaggioX < (areaSecondoEsercizioX + areaSecondoEsercizioWidth) &&
                (personaggioX + personaggioWidth) > areaSecondoEsercizioX &&
                personaggioY < (areaSecondoEsercizioY + areaSecondoEsercizioHeight) &&
                (personaggioY + personaggioHeight) > areaSecondoEsercizioY;
    }

    private boolean isPersonaggioInAreaTerzoEsercizio() {
        setPersonaggioPosition();
        float areaTerzoEsercizioX = posizioneGioco3ImageView.getX();
        float areaTerzoEsercizioY = posizioneGioco3ImageView.getY();
        float areaTerzoEsercizioWidth = posizioneGioco3ImageView.getWidth();
        float areaTerzoEsercizioHeight = posizioneGioco3ImageView.getHeight();

        return personaggioX < (areaTerzoEsercizioX + areaTerzoEsercizioWidth) &&
                (personaggioX + personaggioWidth) > areaTerzoEsercizioX &&
                personaggioY < (areaTerzoEsercizioY + areaTerzoEsercizioHeight) &&
                (personaggioY + personaggioHeight) > areaTerzoEsercizioY;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void enableImageDrag(ImageView view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getRawX();
                final float y = event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        // Salva le coordinate iniziali del tocco
                        xDelta = x - v.getX();
                        yDelta = y - v.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // Calcola la nuova posizione della ImageView
                        float newX = x - xDelta;
                        float newY = y - yDelta;

                        if (isPersonaggioInAreaPrimoEsercizio() && !isCompletato(0)) {
                            ridimensionaPosizione(posizioneGioco2ImageView);
                            ridimensionaPosizione(posizioneGioco3ImageView);
                            posizioneGioco2ImageView.setBackground(null);
                            posizioneGioco3ImageView.setBackground(null);
                            highlightPosizione(posizioneGioco1ImageView);

                        } else if (isPersonaggioInAreaSecondaEsercizio() && !isCompletato(1)) {
                            ridimensionaPosizione(posizioneGioco1ImageView);
                            ridimensionaPosizione(posizioneGioco3ImageView);
                            posizioneGioco1ImageView.setBackground(null);
                            posizioneGioco3ImageView.setBackground(null);
                            highlightPosizione(posizioneGioco2ImageView);

                        } else if (isPersonaggioInAreaTerzoEsercizio() && !isCompletato(2)) {
                            ridimensionaPosizione(posizioneGioco1ImageView);
                            ridimensionaPosizione(posizioneGioco2ImageView);
                            posizioneGioco1ImageView.setBackground(null);
                            posizioneGioco2ImageView.setBackground(null);
                            highlightPosizione(posizioneGioco3ImageView);

                        } else {
                            Log.d("Personaggio", "non in area esercizio");
                            //isPersonaggioInAreaEsercizio = false;
                            isVibrating = false;
                            ridimensionaPosizione(posizioneGioco1ImageView);
                            ridimensionaPosizione(posizioneGioco2ImageView);
                            ridimensionaPosizione(posizioneGioco3ImageView);
                            posizioneGioco1ImageView.setBackground(null);
                            posizioneGioco2ImageView.setBackground(null);
                            posizioneGioco3ImageView.setBackground(null);
                        }

                        // Verifica che la ImageView non esca dalla schermata
                        if (newX > 0 && newX < (getScreenWidth() - v.getWidth())) {
                            v.setX(newX);
                        }

                        if (newY > topHeight && newY < getScreenHeight() - bottomHeight) {
                            v.setY(newY);
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        List<EsercizioRealizzabile> esercizioRealizzabileList = scenarioGioco.getlistEsercizioRealizzabile();
                        if (isPersonaggioInAreaPrimoEsercizio() && !isCompletato(0)) {
                            verificaEssercizio(esercizioRealizzabileList.get(0),0 );
                        } else if (isPersonaggioInAreaSecondaEsercizio() && !isCompletato(1)) {
                            verificaEssercizio(esercizioRealizzabileList.get(1),1 );
                        } else if (isPersonaggioInAreaTerzoEsercizio() && !isCompletato(2)) {
                            verificaEssercizio(esercizioRealizzabileList.get(2),2 );
                        }

                }
                return true;
            }
        });
    }

    private void verificaEssercizio(EsercizioRealizzabile esercizioRealizzabile, int index){
        if(esercizioRealizzabile instanceof EsercizioDenominazioneImmagini){
            bundle.putInt("indiceEsercizio",index);
            navigateTo(R.id.action_scenarioFragment_to_esercizioDenominazioneImmagineFragment2,bundle);
        }else if(esercizioRealizzabile instanceof EsercizioSequenzaParole){
            bundle.putInt("indiceEsercizio",index);
            navigateTo(R.id.action_scenarioFragment_to_esercizioSequenzaParole,bundle);
        }else{
            bundle.putInt("indiceEsercizio",index);
            navigateTo(R.id.action_scenarioFragment_to_esercizioCoppiaImmagini2,bundle);
        }
    }

    private void vibrate() {
        if (!isVibrating) {
            if (vibrator.hasVibrator())
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            isVibrating = true;
        }
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

}

