package views.fragment.userLogopedista.elencoPazienti.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import android.os.Bundle;

import models.utils.audioPlayer.AudioPlayerLink;

import models.domain.esercizi.EsercizioDenominazioneImmagini;
import models.domain.profili.Paziente;

import views.fragment.AbstractNavigationFragment;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import com.squareup.picasso.Picasso;

import androidx.lifecycle.ViewModelProvider;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;


public class RisultatiEserciziDenominazioneImmaginiLogopedistaFragment extends AbstractNavigationFragment {


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

    private LogopedistaViewsModels mLogopedistaViewModel;

    private String idPaziente;


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
            idPaziente = savedInstanceState.getString("idPaziente");
        } else {
            therapy = 0;
            exercise = 0;
            scenery = 0;
        }

        mLogopedistaViewModel = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        imageEsercizioDenominazione = view.findViewById(R.id.imageEsercizioDenominazione);

        checkExercise = view.findViewById(R.id.checkExercise);
        wrongExercise = view.findViewById(R.id.wrongExercise);
        notDoneExercise = view.findViewById(R.id.notDoneExercise);

        aidsUsed = view.findViewById(R.id.aidsUsed);
        answerGiven = view.findViewById(R.id.answerGiven);

        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonPause = view.findViewById(R.id.buttonPause);
        buttonPause.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.esercizioDenominazioneImmagini = getEsercizioDenominazioneImmagine();
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
            aidsUsed.setText("Aids used: " + esercizioDenominazioneImmagini.getRisultatoEsercizio().getCounterAiutiUtilizzati());
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


    private void stopAudio() {
        buttonPlay.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.GONE);
        audioPlayerLink.stopAudio();
    }

    private void playAudio() {
        buttonPlay.setVisibility(View.GONE);
        buttonPause.setVisibility(View.VISIBLE);
        this.audioPlayerLink = new AudioPlayerLink(esercizioDenominazioneImmagini.getRisultatoEsercizio().getAudioRegistrato());
        audioPlayerLink.playAudio();
    }


    private EsercizioDenominazioneImmagini getEsercizioDenominazioneImmagine(){

        for (Paziente paziente : mLogopedistaViewModel.getLogopedistaLiveData().getValue().getListaPazienti()) {
            if(paziente.getIdProfilo().equals(idPaziente)){
                return (EsercizioDenominazioneImmagini) paziente.getTerapie().
                        get(therapy).getListScenariGioco().get(scenery).
                        getlistEsercizioRealizzabile().get(exercise);
            }
        }
        return new EsercizioDenominazioneImmagini(0,0,"","","");
    }




}