package views.fragment.userGenitore.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import com.squareup.picasso.Picasso;

import android.os.Bundle;

import android.content.pm.ActivityInfo;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import views.fragment.AbstractNavigationFragment;

import models.utils.audioPlayer.AudioPlayerLink;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import models.domain.esercizi.EsercizioDenominazioneImmagini;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.util.Log;


public class RisultatiEserciziDenominazioneImmaginiGenitoreFragment extends AbstractNavigationFragment {


    private int scenery;

    private int therapy;

    private int exercise;

    private ImageView notDoneExercise;

    private ImageView wrongExercise;

    private ImageView checkExercise;

    private ImageButton buttonPlay;

    private ImageButton buttonPause;

    private AudioPlayerLink audioPlayerLink;

    private TextView aidsUsed;

    private ImageView imageEsercizioDenominazione;

    private LinearLayout answerGiven;

    private EsercizioDenominazioneImmagini esercizioDenominazioneImmagini;

    private GenitoreViewsModels genitoreViewsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risultati_esercizi_denominazione_immagini, container, false);
        setToolBar(view, getString(R.string.risultatoEsercizio));
        savedInstanceState = getArguments();

        if (savedInstanceState != null && savedInstanceState.containsKey("exercise") &&
                savedInstanceState.containsKey("scenery") &&
                savedInstanceState.containsKey("therapy")) {

            exercise = savedInstanceState.getInt("exercise");
            scenery = savedInstanceState.getInt("scenery");
            therapy = savedInstanceState.getInt("therapy");
        } else {
            therapy = 0;
            exercise = 0;
            scenery = 0;
        }

        imageEsercizioDenominazione = view.findViewById(R.id.imageEsercizioDenominazione);

        checkExercise = view.findViewById(R.id.checkExercise);
        wrongExercise = view.findViewById(R.id.wrongExercise);
        notDoneExercise = view.findViewById(R.id.notDoneExercise);

        aidsUsed = view.findViewById(R.id.aidsUsed);
        answerGiven = view.findViewById(R.id.answerGiven);

        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonPause = view.findViewById(R.id.buttonPause);
        buttonPause.setVisibility(View.GONE);

        genitoreViewsModels = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        this.esercizioDenominazioneImmagini = getEsercizioDenominazioneImmagini(exercise, scenery, therapy);
        Picasso.get().load(esercizioDenominazioneImmagini.getImmagineEsercizioDenominazioneImmagini()).into(imageEsercizioDenominazione);

        if (isNotDone()) {
            checkExercise.setVisibility(View.GONE);
            wrongExercise.setVisibility(View.GONE);
            aidsUsed.setVisibility(View.GONE);
            answerGiven.setVisibility(View.INVISIBLE);
            notDoneExercise.setVisibility(View.VISIBLE);
        } else {
            if (isCorrect()) {
                checkExercise.setVisibility(View.VISIBLE);
                wrongExercise.setVisibility(View.GONE);
            } else {
                checkExercise.setVisibility(View.GONE);
                wrongExercise.setVisibility(View.VISIBLE);
            }
            int aids = esercizioDenominazioneImmagini.getRisultatoEsercizio().getCounterAiutiUtilizzati();
            aidsUsed.setText(aidsUsed.getText().toString() + " " + aids);
        }

        buttonPlay.setOnClickListener(v -> playAudio());
        buttonPause.setOnClickListener(v -> stopAudio());

    }


    private boolean isNotDone() {
        return this.esercizioDenominazioneImmagini.getRisultatoEsercizio() == null;
    }

    private boolean isCorrect() {
        return esercizioDenominazioneImmagini.getRisultatoEsercizio().isEsitoCorretto();
    }


    // per bloccare l'orientamento in portrait
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    // per sbloccare l'orientamento quando il fragment non è più visibile
    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    private void stopAudio() {
        buttonPlay.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.GONE);
        audioPlayerLink.stopAudio();
    }

    private void playAudio() {
        buttonPlay.setVisibility(View.GONE);
        buttonPause.setVisibility(View.VISIBLE);
        audioPlayerLink = new AudioPlayerLink(esercizioDenominazioneImmagini.getRisultatoEsercizio().getAudioRegistrato());
        audioPlayerLink.playAudio();
    }


    private EsercizioDenominazioneImmagini getEsercizioDenominazioneImmagini(int exercise, int scenery, int therapy){

        Log.d("RisultatoDenominazione", ":"+ exercise);

        Log.d("RisultatoSequenzaParole", ":"+  genitoreViewsModels.getPazienteLiveData().
                getValue().getTerapie().get(therapy).getListScenariGioco().
                get(scenery).getlistEsercizioRealizzabile().get(exercise).toString());

        return (EsercizioDenominazioneImmagini) genitoreViewsModels.getPazienteLiveData().
                getValue().getTerapie().get(therapy).getListScenariGioco().
                get(scenery).getlistEsercizioRealizzabile().get(exercise);
    }


}