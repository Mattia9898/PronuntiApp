package views.fragment.userLogopedista.elencoPazienti.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import java.io.File;

import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import android.media.MediaPlayer;

import views.fragment.AbstractNavigazioneFragment;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.LinearLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.util.Log;

import models.utils.audioRecorder.AudioRecorder;
import models.utils.audioPlayer.AudioPlayerLink;
import models.domain.profili.Paziente;
import models.domain.esercizi.EsercizioSequenzaParole;


public class RisultatiEserciziSequenzaParoleLogopedistaFragment extends AbstractNavigazioneFragment {


    private int scenery;

    private int therapy;

    private int exercise;

    private ImageView notDoneExercise;

    private ImageView wrongExercise;

    private ImageView checkExercise;

    private ImageButton buttonPlay;

    private ImageButton buttonPause;

    private ImageButton buttonAnswerPlay;

    private ImageButton buttonAnswerPause;

    private AudioPlayerLink audioPlayerLink;

    private AudioPlayerLink audioPlayerLinkRegistration;

    private EsercizioSequenzaParole esercizioSequenzaParole;

    private LinearLayout answerGiven;

    private SeekBar seekBar;

    private AudioRecorder audioRecorder;

    private MediaPlayer mediaPlayer;

    private String idPaziente;

    private LogopedistaViewsModels logopedistaViewsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risultati_esercizi_sequenza_parole, container, false);
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

        logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        seekBar = view.findViewById(R.id.seekBar);

        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonPause = view.findViewById(R.id.buttonPause);

        notDoneExercise = view.findViewById(R.id.notDoneExercise);
        checkExercise = view.findViewById(R.id.checkExercise);
        wrongExercise = view.findViewById(R.id.wrongExercise);

        answerGiven = view.findViewById(R.id.answerGiven);

        buttonAnswerPlay = view.findViewById(R.id.buttonAnswerPlay);
        buttonAnswerPause = view.findViewById(R.id.buttonAnswerPause);
        buttonAnswerPause.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.esercizioSequenzaParole = getEsercizioSequenzaParole();

        this.audioRecorder = audioRecorder();
        this.audioPlayerLink = new AudioPlayerLink(esercizioSequenzaParole.getAudioEsercizioSequenzaParole());

        this.mediaPlayer = audioPlayerLink.getMediaPlayer();

        if(isNotDone()) {
            answerGiven.setVisibility(View.INVISIBLE);
            wrongExercise.setVisibility(View.GONE);
            checkExercise.setVisibility(View.GONE);
            notDoneExercise.setVisibility(View.VISIBLE);
        }
        else {
            if (isCorrect()) {
                checkExercise.setVisibility(View.VISIBLE);
                wrongExercise.setVisibility(View.GONE);
            } else {
                checkExercise.setVisibility(View.GONE);
                wrongExercise.setVisibility(View.VISIBLE);
            }

            buttonAnswerPlay.setOnClickListener(v -> playAudio());
            buttonAnswerPause.setOnClickListener(v -> stopAudio());

            this.audioPlayerLinkRegistration = new AudioPlayerLink(esercizioSequenzaParole.getRisultatoEsercizio().getAudioRegistrato());

        }

        buttonPlay.setOnClickListener(v -> startReproductionAudio());
        buttonPause.setOnClickListener(v -> stopReproductionAudio());
    }

    private boolean isNotDone() {
        return this.esercizioSequenzaParole.getRisultatoEsercizio() == null;
    }

    private boolean isCorrect() {
        return esercizioSequenzaParole.getRisultatoEsercizio().isEsitoCorretto();
    }

    private void stopAudio() {
        buttonAnswerPlay.setVisibility(View.VISIBLE);
        buttonAnswerPause.setVisibility(View.GONE);
        audioPlayerLinkRegistration.stopAudio();
    }

    private void playAudio() {
        buttonAnswerPlay.setVisibility(View.GONE);
        buttonAnswerPause.setVisibility(View.VISIBLE);
        audioPlayerLinkRegistration.playAudio();
    }

    private AudioRecorder audioRecorder() {

        File appDirectory = getContext().getFilesDir();
        File audioRegistration = new File(appDirectory, "audioRegistrato");

        return new AudioRecorder(audioRegistration);
    }

    public void stopReproductionAudio() {
        buttonPlay.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.GONE);
        audioPlayerLink.stopAudio();
    }

    private void startReproductionAudio() {
        buttonPlay.setVisibility(View.GONE);
        buttonPause.setVisibility(View.VISIBLE);
        fillProgressBar();
        audioPlayerLink.playAudio();
    }

    private void fillProgressBar() {
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            Log.d("EsercizioCoppiaImmagini", "Audio completato");
            buttonPlay.setVisibility(View.VISIBLE);
            buttonPause.setVisibility(View.GONE);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress * mediaPlayer.getDuration() / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress() * mediaPlayer.getDuration() / 100);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress() * mediaPlayer.getDuration() / 100);
            }
        });

        mediaPlayer.setOnSeekCompleteListener(mediaPlayer ->
                seekBar.setProgress((int) (mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration())));

        final int delay = 5;

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying() && seekBar != null) {
                    seekBar.setProgress((int) (mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration()));
                }
                handler.postDelayed(this, delay);
            }
        };

        mediaPlayer.setOnPreparedListener(mp -> handler.postDelayed(runnable, delay));
    }


    private EsercizioSequenzaParole getEsercizioSequenzaParole(){

        for (Paziente paziente : logopedistaViewsModels.getLogopedistaLiveData().getValue().getListaPazienti()) {
            if(paziente.getIdProfilo().equals(idPaziente)){
                return (EsercizioSequenzaParole) paziente.getTerapie().
                        get(therapy).getListScenariGioco().get(scenery).
                        getlistEsercizioRealizzabile().get(exercise);
            }
        }
        return new EsercizioSequenzaParole(0,0,"","","","");
    }

}
