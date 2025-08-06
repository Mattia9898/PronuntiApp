package views.fragment.userLogopedista.elencoPazienti.monitoraggio;

import android.media.MediaPlayer;

import android.os.Bundle;

import android.os.Handler;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageButton;

import android.widget.ImageView;

import android.widget.SeekBar;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import java.io.File;

import it.uniba.dib.pronuntiapp.R;

import models.domain.esercizi.EsercizioCoppiaImmagini;

import models.domain.profili.Paziente;

import models.utils.audioPlayer.AudioPlayerLink;

import models.utils.audioRecorder.AudioRecorder;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import views.fragment.AbstractNavigazioneFragment;

public class RisultatiEserciziCoppiaImmaginiLogopedistaFragment extends AbstractNavigazioneFragment {

    private SeekBar seekBarEsercizioCoppiaImmagini;

    private AudioRecorder audioRecorder;

    private MediaPlayer mMediaPlayer;

    private AudioPlayerLink audioPlayerLink;

    private EsercizioCoppiaImmagini mEsercizioCoppiaImmagini;

    private ImageButton imageButtonPlay;

    private ImageButton imageButtonPause;

    private ImageView imageViewCheck;

    private ImageView imageViewWrong;

    private String idPaziente;

    private int indiceTerapia;

    private int indiceEsercizio;

    private int indiceScenario;

    private LogopedistaViewsModels mLogopedistaViewModel;

    private ImageView imageViewNonSvoltoEsercizio;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risultati_esercizi_coppia_immagini, container, false);

        setToolBar(view, getString(R.string.risultatoEsercizio));

        savedInstanceState = getArguments();

        if (savedInstanceState != null && savedInstanceState.containsKey("indiceEsercizio") && savedInstanceState.containsKey("indiceScenario") && savedInstanceState.containsKey("indiceTerapia")) {
            indiceEsercizio = savedInstanceState.getInt("indiceEsercizio");
            indiceScenario = savedInstanceState.getInt("indiceScenario");
            indiceTerapia = savedInstanceState.getInt("indiceTerapia");
            idPaziente = savedInstanceState.getString("idPaziente");
        } else {
            indiceTerapia = 0;
            indiceEsercizio = 0;
            indiceScenario = 0;
        }

        mLogopedistaViewModel = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        seekBarEsercizioCoppiaImmagini = view.findViewById(R.id.seekBarScorrimentoAudioEsercizioCoppiaImmagini);
        imageButtonPlay = view.findViewById(R.id.playButton);
        imageButtonPause = view.findViewById(R.id.pauseButton);

        imageViewCheck = view.findViewById(R.id.imageViewCheckEsercizio);
        imageViewWrong = view.findViewById(R.id.imageViewWrongEsercizio);
        imageViewNonSvoltoEsercizio = view.findViewById(R.id.imageViewNonSvoltoEsercizio);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.mEsercizioCoppiaImmagini = getEsercizioCoppiaImmaginiFromViewModel();

        this.audioRecorder = initAudioRecorder();
        this.audioPlayerLink = new AudioPlayerLink(mEsercizioCoppiaImmagini.getAudioEsercizioCoppiaImmagini());
        this.mMediaPlayer = audioPlayerLink.getMediaPlayer();

        if(isNonSvolto()){
            imageViewCheck.setVisibility(View.GONE);
            imageViewWrong.setVisibility(View.GONE);
            imageViewNonSvoltoEsercizio.setVisibility(View.VISIBLE);
        }
        else if (isCorrect()) {
            imageViewCheck.setVisibility(View.VISIBLE);
            imageViewWrong.setVisibility(View.GONE);
        } else {
            imageViewCheck.setVisibility(View.GONE);
            imageViewWrong.setVisibility(View.VISIBLE);
        }

        imageButtonPlay.setOnClickListener(v -> avviaRiproduzioneAudio());
        imageButtonPause.setOnClickListener(v -> stoppaRiproduzioneAudio());
    }

    private boolean isNonSvolto() {
        return this.mEsercizioCoppiaImmagini.getRisultatoEsercizio() == null;
    }

    private AudioRecorder initAudioRecorder() {
        File cartellaApp = getContext().getFilesDir();
        File audioRegistrazione = new File(cartellaApp, "tempAudioRegistrato");

        return new AudioRecorder(audioRegistrazione);
    }

    private void avviaRiproduzioneAudio() {
        imageButtonPlay.setVisibility(View.GONE);
        imageButtonPause.setVisibility(View.VISIBLE);
        inizializzaBarraAvanzamento();
        audioPlayerLink.playAudio();
    }

    private void inizializzaBarraAvanzamento() {
        mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
            Log.d("EsercizioCoppiaImmagini", "Audio completato");
            imageButtonPlay.setVisibility(View.VISIBLE);
            imageButtonPause.setVisibility(View.GONE);
        });

        seekBarEsercizioCoppiaImmagini.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress * mMediaPlayer.getDuration() / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress() * mMediaPlayer.getDuration() / 100);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress() * mMediaPlayer.getDuration() / 100);
            }
        });

        mMediaPlayer.setOnSeekCompleteListener(mediaPlayer ->
                seekBarEsercizioCoppiaImmagini.setProgress((int) (mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration())));

        final int delay = 5;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying() && seekBarEsercizioCoppiaImmagini != null) {
                    seekBarEsercizioCoppiaImmagini.setProgress((int) (mMediaPlayer.getCurrentPosition() * 100 / mMediaPlayer.getDuration()));
                }
                handler.postDelayed(this, delay);
            }
        };

        mMediaPlayer.setOnPreparedListener(mp -> handler.postDelayed(runnable, delay));
    }
    public void stoppaRiproduzioneAudio() {
        imageButtonPlay.setVisibility(View.VISIBLE);
        imageButtonPause.setVisibility(View.GONE);
        audioPlayerLink.stopAudio();
    }

    private boolean isCorrect() {
        return mEsercizioCoppiaImmagini.getRisultatoEsercizio().isEsitoCorretto();
    }

    private EsercizioCoppiaImmagini getEsercizioCoppiaImmaginiFromViewModel(){

        for (Paziente pazienti : mLogopedistaViewModel.getLogopedistaLiveData().getValue().getListaPazienti()) {
            if(pazienti.getIdProfilo().equals(idPaziente)){
                return (EsercizioCoppiaImmagini) pazienti.getTerapie().get(indiceTerapia).getListScenariGioco().get(indiceScenario).getlistEsercizioRealizzabile().get(indiceEsercizio);
            }
        }
        return new EsercizioCoppiaImmagini(0,0,"","","");
    }

}
