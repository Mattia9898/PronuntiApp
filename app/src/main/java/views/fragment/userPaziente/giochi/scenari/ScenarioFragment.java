package views.fragment.userPaziente.giochi.scenari;


import it.uniba.dib.pronuntiapp.R;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import models.domain.esercizi.EsercizioDenominazioneImmagini;
import models.domain.esercizi.EsercizioRealizzabile;
import models.domain.esercizi.EsercizioSequenzaParole;
import models.domain.scenariGioco.ScenarioGioco;
import models.domain.terapie.Terapia;
import viewsModels.pazienteViewsModels.PazienteViewsModels;
import views.fragment.AbstractNavigationFragment;
import views.fragment.userPaziente.giochi.FineScenarioView;

import android.widget.ImageView;
import android.util.Log;
import android.util.TypedValue;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;


public class ScenarioFragment extends AbstractNavigationFragment {


    private float characterCoordinatesX, characterCoordinatesY, characterWidth, characterHeight;

    private float alphaCoordinatesX, alphaCoordinatesY;

    private float bottomHeight, topHeight;

    private boolean isOnVibration = false;

    private Bundle bundle;

    private Vibrator vibrator;

    private ImageView character;

    private ImageView positionFirstGame, positionSecondGame, positionThirdGame;

    private PazienteViewsModels pazienteViewsModels;

    private ScenarioGioco scenarioGioco;

    private FineScenarioView fineScenarioView;

    private ConstraintLayout mainConstraintLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scenario, container, false);
        bundle = getArguments();
        this.pazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);

        fineScenarioView = view.findViewById(R.id.fineScenarioView);
        fineScenarioView.setVisibility(View.GONE);

        mainConstraintLayout = view.findViewById(R.id.mainConstraintLayout);

        character = view.findViewById(R.id.character);

        positionFirstGame = view.findViewById(R.id.positionFirstGame);
        positionSecondGame = view.findViewById(R.id.positionSecondGame);
        positionThirdGame = view.findViewById(R.id.positionThirdGame);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        int indexScenery = bundle.getInt("actualscenarioIndex");
        int indexTherapy = bundle.getInt("indiceTerapia");

        Log.d("actualscenarioIndex", "actualscenarioIndex "+String.valueOf(indexScenery));

        pazienteViewsModels.getTexturePersonaggioSelezionatoLiveData().observe(getViewLifecycleOwner(), texture -> {
            Picasso.get().load(texture).into(character);
        });

        pazienteViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {

            List<Terapia> listTherapies = paziente.getTerapie();
            Terapia terapia  = listTherapies.get(indexTherapy);
            scenarioGioco = terapia.getListScenariGioco().get(indexScenery);

            String backgroundImage = scenarioGioco.getSfondoImmagine();
            String firstImage = scenarioGioco.getImmagine1();
            String secondImage = scenarioGioco.getImmagine2();
            String thirdImage = scenarioGioco.getImmagine3();

            Glide.with(this).load(backgroundImage).centerCrop().into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    mainConstraintLayout.setBackground(resource);
                }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {}
            });

            Glide.with(this).load(firstImage).centerInside().into(positionFirstGame);
            Glide.with(this).load(secondImage).centerInside().into(positionSecondGame);
            Glide.with(this).load(thirdImage).centerInside().into(positionThirdGame);

            // se l'esercizio è completato, allora bisogna disattivare la relativa immagine
            if (isDone(0)) {
                disableImageView(positionFirstGame);
            }
            if (isDone(1)) {
                disableImageView(positionSecondGame);
            }
            if (isDone(2)) {
                disableImageView(positionThirdGame);
            }

            pazienteViewsModels.getTexturePersonaggioSelezionatoLiveData().observe(getViewLifecycleOwner(), texture -> {
                Picasso.get().load(texture).into(character);
            });

            vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            character.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    character.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int imageDP = 125;
                    topHeight= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, imageDP, getResources().getDisplayMetrics());
                    bottomHeight = getResources().getDimension(R.dimen.nav_bar_height);
                    bottomHeight += bottomHeight * 0.2f;

                    // per abilitare il drag dell'immagine del personaggio
                    enableImageDrag(character);
                }

            });

            Bundle endScenery = getArguments();
            Log.d("Bundle", "onViewCreated fine scenario: " + endScenery);

            if(endScenery != null) {
                if(endScenery.containsKey("checkEndScenery") && endScenery.getBoolean("checkEndScenery")) {
                    showEndScenery();
                }
            }

        });
    }

    private void showEndScenery(){
        fineScenarioView.setVisibility(View.VISIBLE);
        Log.d("endScenery", "end scenery" + scenarioGioco.getlistEsercizioRealizzabile().toString());
        int finalReward = scenarioGioco.getRicompensaFinale();
        fineScenarioView.showFineScenario(finalReward, positionFirstGame, positionSecondGame, positionThirdGame);
    }

    private void disableImageView(ImageView imageView) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorMatrixColorFilter);
    }

    private boolean isDone(int indexExercise){
        if(scenarioGioco.getlistEsercizioRealizzabile().get(indexExercise).getRisultatoEsercizio() != null){
            Log.d("Exercise", indexExercise + " completed");
            return true;
        }else{
            Log.d("Exercise", indexExercise + " not completed");
            return false;
        }
    }

    private void vibration() {
        if (!isOnVibration) {
            if (vibrator.hasVibrator())
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            isOnVibration = true;
        }
    }

    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private void highlightPosition(ImageView imageView) {
        zoomInPosition(imageView);
        vibration();
        imageView.setBackground(getContext().getDrawable(R.drawable.esercizio_highlight_background));
    }

    private void setPositionCharacter() {
        characterCoordinatesX = character.getX();
        characterCoordinatesY = character.getY();
        characterWidth = character.getWidth();
        characterHeight = character.getHeight();
    }

    private void resizePosition(ImageView imageView){
        imageView.setScaleX(1f);
        imageView.setScaleY(1f);
    }

    private void zoomInPosition(ImageView imageView){
        imageView.setScaleX(1.3f);
        imageView.setScaleY(1.3f);
    }


    private boolean isCharacterInFirstExercise() {
        setPositionCharacter();
        float areaCoordinatesX = positionFirstGame.getX();
        float areaCoordinatesY = positionFirstGame.getY();
        float areaWidth = positionFirstGame.getWidth();
        float areaHeight = positionFirstGame.getHeight();

        return characterCoordinatesX < (areaCoordinatesX + areaWidth) &&
                (characterCoordinatesX + characterWidth) > areaCoordinatesX &&
                characterCoordinatesY < (areaCoordinatesY + areaHeight) &&
                (characterCoordinatesY + characterHeight) > areaCoordinatesY;
    }

    private boolean isCharacterInSecondExercise() {
        setPositionCharacter();
        float areaCoordinatesX = positionSecondGame.getX();
        float areaCoordinatesY = positionSecondGame.getY();
        float areaWidth = positionSecondGame.getWidth();
        float areaHeight = positionSecondGame.getHeight();

        return characterCoordinatesX < (areaCoordinatesX + areaWidth) &&
                (characterCoordinatesX + characterWidth) > areaCoordinatesX &&
                characterCoordinatesY < (areaCoordinatesY + areaHeight) &&
                (characterCoordinatesY + characterHeight) > areaCoordinatesY;
    }

    private boolean isCharacterInThirdExercise() {
        setPositionCharacter();
        float areaCoordinatesX = positionThirdGame.getX();
        float areaCoordinatesY = positionThirdGame.getY();
        float areaWidth = positionThirdGame.getWidth();
        float areaHeight = positionThirdGame.getHeight();

        return characterCoordinatesX < (areaCoordinatesX + areaWidth) &&
                (characterCoordinatesX + characterWidth) > areaCoordinatesX &&
                characterCoordinatesY < (areaCoordinatesY + areaHeight) &&
                (characterCoordinatesY + characterHeight) > areaCoordinatesY;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void enableImageDrag(ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View wiev, MotionEvent motionEvent) {
                final float x = motionEvent.getRawX();
                final float y = motionEvent.getRawY();

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        // Salva le coordinate iniziali del tocco
                        alphaCoordinatesX = x - wiev.getX();
                        alphaCoordinatesY = y - wiev.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // Calcola la nuova posizione della ImageView
                        float newX = x - alphaCoordinatesX;
                        float newY = y - alphaCoordinatesY;

                        if (isCharacterInFirstExercise() && !isDone(0)) {
                            resizePosition(positionSecondGame);
                            resizePosition(positionThirdGame);
                            positionSecondGame.setBackground(null);
                            positionThirdGame.setBackground(null);
                            highlightPosition(positionFirstGame);

                        } else if (isCharacterInSecondExercise() && !isDone(1)) {
                            resizePosition(positionFirstGame);
                            resizePosition(positionThirdGame);
                            positionFirstGame.setBackground(null);
                            positionThirdGame.setBackground(null);
                            highlightPosition(positionSecondGame);

                        } else if (isCharacterInThirdExercise() && !isDone(2)) {
                            resizePosition(positionFirstGame);
                            resizePosition(positionSecondGame);
                            positionFirstGame.setBackground(null);
                            positionSecondGame.setBackground(null);
                            highlightPosition(positionThirdGame);

                        } else {
                            Log.d("Character", "not in exercise area");
                            isOnVibration = false;
                            resizePosition(positionFirstGame);
                            resizePosition(positionSecondGame);
                            resizePosition(positionThirdGame);
                            positionFirstGame.setBackground(null);
                            positionSecondGame.setBackground(null);
                            positionThirdGame.setBackground(null);
                        }

                        // per verificare che la ImageView non esca dalla schermata
                        if (newX > 0 && newX < (getScreenWidth() - wiev.getWidth())) {
                            wiev.setX(newX);
                        }

                        if (newY > topHeight && newY < getScreenHeight() - bottomHeight) {
                            wiev.setY(newY);
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        List<EsercizioRealizzabile> listEsercizioRealizzabile = scenarioGioco.getlistEsercizioRealizzabile();
                        if (isCharacterInFirstExercise() && !isDone(0)) {
                            checkExercise(listEsercizioRealizzabile.get(0),0 );
                        } else if (isCharacterInSecondExercise() && !isDone(1)) {
                            checkExercise(listEsercizioRealizzabile.get(1),1 );
                        } else if (isCharacterInThirdExercise() && !isDone(2)) {
                            checkExercise(listEsercizioRealizzabile.get(2),2 );
                        }

                }
                return true;
            }
        });
    }

    private void checkExercise(EsercizioRealizzabile esercizioRealizzabile, int indexExercise){
        if(esercizioRealizzabile instanceof EsercizioDenominazioneImmagini){
            bundle.putInt("indexExercise", indexExercise);
            navigateTo(R.id.action_scenarioFragment_to_esercizioDenominazioneImmagineFragment2, bundle);
        }else if(esercizioRealizzabile instanceof EsercizioSequenzaParole){
            bundle.putInt("indexExercise", indexExercise);
            navigateTo(R.id.action_scenarioFragment_to_esercizioSequenzaParole, bundle);
        }else{
            bundle.putInt("indexExercise", indexExercise);
            navigateTo(R.id.action_scenarioFragment_to_esercizioCoppiaImmagini2, bundle);
        }
    }

}

