package views.fragment.userGenitore.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import java.io.File;

import views.fragment.AbstractNavigationFragment;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import androidx.lifecycle.ViewModelProvider;

import models.utils.audioRecorder.AudioRecorder;
import models.utils.audioPlayer.AudioPlayerLink;
import models.domain.esercizi.EsercizioCoppiaImmagini;

import android.util.Log;
import android.content.pm.ActivityInfo;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.os.Bundle;
import android.os.Handler;


public class RisultatiEserciziCoppiaImmaginiGenitoreFragment extends AbstractNavigationFragment {


    private AudioRecorder audioRecorder;

    private SeekBar seekBar;

    private EsercizioCoppiaImmagini esercizioCoppiaImmagini;

    private GenitoreViewsModels genitoreViewsModels;

    private AudioPlayerLink audioPlayerLink;

    private MediaPlayer mediaPlayer;

    private ImageView notDoneExercise;

    private ImageView wrongExercise;

    private ImageView checkExercise;

    private int therapy;

    private int scenery;

    private int exercise;

    private ImageButton buttonPause;

    private ImageButton buttonPlay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risultati_esercizi_coppia_immagini, container, false);
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

        seekBar = view.findViewById(R.id.seekBar);
        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonPause = view.findViewById(R.id.buttonPause);
        notDoneExercise = view.findViewById(R.id.notDoneExercise);
        checkExercise = view.findViewById(R.id.checkExercise);
        wrongExercise = view.findViewById(R.id.wrongExercise);

        genitoreViewsModels = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.esercizioCoppiaImmagini = getEsercizioCoppiaImmagini(exercise, scenery, therapy);
        this.audioRecorder = audioRecorder();
        this.audioPlayerLink = new AudioPlayerLink(esercizioCoppiaImmagini.getAudioEsercizioCoppiaImmagini());
        this.mediaPlayer = audioPlayerLink.getMediaPlayer();

        if(isNotDone()){
            checkExercise.setVisibility(View.GONE);
            wrongExercise.setVisibility(View.GONE);
            notDoneExercise.setVisibility(View.VISIBLE);
        }
        else if(isCorrect()) {
            checkExercise.setVisibility(View.VISIBLE);
            wrongExercise.setVisibility(View.GONE);
        } else {
            checkExercise.setVisibility(View.GONE);
            wrongExercise.setVisibility(View.VISIBLE);
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
            Log.d("EsercizioCoppiaImmagini", "Audio completed");
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
        return this.esercizioCoppiaImmagini.getRisultatoEsercizio().isEsitoCorretto();
    }

    private boolean isNotDone() {
        return this.esercizioCoppiaImmagini.getRisultatoEsercizio() == null;
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


    private EsercizioCoppiaImmagini getEsercizioCoppiaImmagini(int indexExercise, int indexScenery, int indexTherapy){
        Log.d("RisultatoDenominazione", ":"+ indexExercise);
        return (EsercizioCoppiaImmagini) genitoreViewsModels.getPazienteLiveData().getValue().
                getTerapie().get(indexTherapy).getListScenariGioco().get(indexScenery).
                getlistEsercizioRealizzabile().get(indexExercise);
    }


}
