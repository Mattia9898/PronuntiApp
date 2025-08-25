package views.fragment.userGenitore.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import views.fragment.AbstractNavigationFragment;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import models.utils.audioRecorder.AudioRecorder;
import models.utils.audioPlayer.AudioPlayerLink;
import models.domain.esercizi.EsercizioSequenzaParole;

import java.io.File;

import android.util.Log;
import android.widget.SeekBar;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.ImageButton;


public class RisultatiEserciziSequenzaParoleGenitoreFragment extends AbstractNavigationFragment {

    private ImageView checkExercise;

    private ImageView wrongExercise;

    private ImageView notDoneExercise;

    private ImageButton buttonAnswerPlay;

    private ImageButton buttonAnswerPause;

    private ImageButton buttonPlay;

    private ImageButton buttonPause;

    private SeekBar seekBar;

    private AudioRecorder audioRecorder;

    private AudioPlayerLink audioPlayerLink;

    private MediaPlayer mediaPlayer;

    private int exercise;

    private int therapy;

    private int scenery;

    private GenitoreViewsModels genitoreViewsModels;

    private EsercizioSequenzaParole esercizioSequenzaParole;

    private LinearLayout answerGiven;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risultati_esercizi_sequenza_parole, container, false);
        setToolBar(view, getString(R.string.risultatoEsercizio));
        savedInstanceState = getArguments();

        if (savedInstanceState != null && savedInstanceState.containsKey("exercise")
                && savedInstanceState.containsKey("scenery")
                && savedInstanceState.containsKey("therapy")) {

            exercise = savedInstanceState.getInt("exercise");
            scenery = savedInstanceState.getInt("scenery");
            therapy = savedInstanceState.getInt("therapy");
        } else {
            therapy = 0;
            exercise = 0;
            scenery = 0;
        }

        seekBar = view.findViewById(R.id.seekBar);

        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonPause = view.findViewById(R.id.buttonPause);

        answerGiven = view.findViewById(R.id.answerGiven);

        notDoneExercise = view.findViewById(R.id.notDoneExercise);
        checkExercise = view.findViewById(R.id.checkExercise);
        wrongExercise = view.findViewById(R.id.wrongExercise);

        buttonAnswerPlay = view.findViewById(R.id.buttonAnswerPlay);
        buttonAnswerPause = view.findViewById(R.id.buttonAnswerPause);
        buttonAnswerPause.setVisibility(View.GONE);

        genitoreViewsModels = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        this.esercizioSequenzaParole = getEsercizioSequenzaParole(exercise, scenery, therapy);

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
        }

        buttonPlay.setOnClickListener(v -> startReproductionAudio());
        buttonPause.setOnClickListener(v -> stopReproductionAudio());
    }

    private AudioRecorder audioRecorder() {

        File appDirectory = getContext().getFilesDir();
        File audioRegistration = new File(appDirectory, "audioRegistrato");

        return new AudioRecorder(audioRegistration);
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


    // per sbloccare l'orientamento quando il fragment non è più visibile
    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }


    // per bloccare l'orientamento in portrait
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    private boolean isCorrect() {
        return esercizioSequenzaParole.getRisultatoEsercizio().isEsitoCorretto();
    }

    private boolean isNotDone() {
        return this.esercizioSequenzaParole.getRisultatoEsercizio() == null;
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

    private EsercizioSequenzaParole getEsercizioSequenzaParole(int exercise, int scenery, int therapy){
        return (EsercizioSequenzaParole) genitoreViewsModels.getPazienteLiveData().
                getValue().getTerapie().get(therapy).getListScenariGioco().
                get(scenery).getlistEsercizioRealizzabile().get(exercise);
    }

    private void playAudio() {
        buttonAnswerPlay.setVisibility(View.GONE);
        buttonAnswerPause.setVisibility(View.VISIBLE);
        audioPlayerLink = new AudioPlayerLink(esercizioSequenzaParole.getRisultatoEsercizio().getAudioRegistrato());
        audioPlayerLink.playAudio();
    }

    private void stopAudio() {
        buttonAnswerPlay.setVisibility(View.VISIBLE);
        buttonAnswerPause.setVisibility(View.GONE);
        audioPlayerLink.stopAudio();
    }

}