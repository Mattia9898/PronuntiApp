package views.fragment.userLogopedista.elencoPazienti.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import android.widget.SeekBar;
import android.widget.ImageView;
import android.widget.ImageButton;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import models.domain.profili.Paziente;
import models.domain.esercizi.EsercizioCoppiaImmagini;
import models.utils.audioPlayer.AudioPlayerLink;
import models.utils.audioRecorder.AudioRecorder;

import android.os.Handler;
import android.os.Bundle;

import android.media.MediaPlayer;

import java.io.File;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import views.fragment.AbstractNavigazioneFragment;


public class RisultatiEserciziCoppiaImmaginiLogopedistaFragment extends AbstractNavigazioneFragment {


    private String idPaziente;

    private int indexTherapy;

    private int indexScenery;

    private int indexExercise;

    private MediaPlayer mediaPlayer;

    private AudioPlayerLink audioPlayerLink;

    private AudioRecorder audioRecorder;

    private EsercizioCoppiaImmagini esercizioCoppiaImmagini;

    private SeekBar seekBar;

    private LogopedistaViewsModels logopedistaViewsModels;

    private ImageButton buttonPause;

    private ImageButton buttonPlay;

    private ImageView wrongExercise;

    private ImageView checkExercise;

    private ImageView notDoneExercise;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risultati_esercizi_coppia_immagini, container, false);
        setToolBar(view, getString(R.string.risultatoEsercizio));
        savedInstanceState = getArguments();

        if (savedInstanceState != null && savedInstanceState.containsKey("indexExercise") &&
                savedInstanceState.containsKey("indexScenery") &&
                savedInstanceState.containsKey("indexTherapy")) {

            indexExercise = savedInstanceState.getInt("indexExercise");
            indexScenery = savedInstanceState.getInt("indexScenery");
            indexTherapy = savedInstanceState.getInt("indexTherapy");
            idPaziente = savedInstanceState.getString("idPaziente");
        } else {
            indexTherapy = 0;
            indexExercise = 0;
            indexScenery = 0;
        }

        logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        seekBar = view.findViewById(R.id.seekBar);
        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonPause = view.findViewById(R.id.buttonPause);

        checkExercise = view.findViewById(R.id.checkExercise);
        wrongExercise = view.findViewById(R.id.wrongExercise);
        notDoneExercise = view.findViewById(R.id.notDoneExercise);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.esercizioCoppiaImmagini = getEsercizioCoppiaImmagini();

        this.audioRecorder = audioRecorder();
        this.audioPlayerLink = new AudioPlayerLink(esercizioCoppiaImmagini.getAudioEsercizioCoppiaImmagini());
        this.mediaPlayer = audioPlayerLink.getMediaPlayer();

        if (isNotDone()){
            checkExercise.setVisibility(View.GONE);
            wrongExercise.setVisibility(View.GONE);
            notDoneExercise.setVisibility(View.VISIBLE);
        }
        else if (isCorrect()) {
            checkExercise.setVisibility(View.VISIBLE);
            wrongExercise.setVisibility(View.GONE);
        } else {
            checkExercise.setVisibility(View.GONE);
            wrongExercise.setVisibility(View.VISIBLE);
        }

        buttonPlay.setOnClickListener(v -> startReproductionAudio());
        buttonPause.setOnClickListener(v -> stopReproductionAudio());
    }

    private boolean isCorrect() {
        return esercizioCoppiaImmagini.getRisultatoEsercizio().isEsitoCorretto();
    }

    private boolean isNotDone() {
        return this.esercizioCoppiaImmagini.getRisultatoEsercizio() == null;
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

        final int delay = 5; // Milliseconds

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


    private EsercizioCoppiaImmagini getEsercizioCoppiaImmagini(){

        for (Paziente paziente : logopedistaViewsModels.getLogopedistaLiveData().getValue().getListaPazienti()) {
            if(paziente.getIdProfilo().equals(idPaziente)){
                return (EsercizioCoppiaImmagini) paziente.getTerapie().
                        get(indexTherapy).getListScenariGioco().
                        get(indexScenery).getlistEsercizioRealizzabile().
                        get(indexExercise);
            }
        }
        return new EsercizioCoppiaImmagini(0,0,"","","");
    }

}
