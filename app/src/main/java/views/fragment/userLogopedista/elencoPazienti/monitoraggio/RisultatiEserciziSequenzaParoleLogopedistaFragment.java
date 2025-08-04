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

import android.widget.LinearLayout;

import android.widget.SeekBar;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import java.io.File;

import it.uniba.dib.pronuntiapp.R;

import models.domain.esercizi.EsercizioSequenzaParole;

import models.domain.profili.Paziente;

import models.utils.audioPlayer.AudioPlayerLink;

import models.utils.audioRecorder.AudioRecorder;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import views.fragment.AbstractNavigazioneFragment;

public class RisultatiEserciziSequenzaParoleLogopedistaFragment extends AbstractNavigazioneFragment {

    private ImageView imageViewCheck;

    private ImageView imageViewWrong;

    private ImageButton playButtonRisposta;

    private ImageButton pauseButtonRisposta;

    private ImageButton imageButtonPlay;

    private ImageButton imageButtonPause;

    private SeekBar seekBarEsercizioSequenzaParole;

    private AudioRecorder audioRecorder;

    private AudioPlayerLink audioPlayerLink;

    private AudioPlayerLink audioPlayerLinkRegistrazione;

    private MediaPlayer mMediaPlayer;

    private int indiceTerapia;

    private int indiceEsercizio;

    private int indiceScenario;

    private String idPaziente;

    private LogopedistaViewsModels mLogopedistaViewModel;

    private LinearLayout linearLayoutRispostaData;

    private ImageView imageViewNonSvoltoEsercizio;

    private EsercizioSequenzaParole mEsercizioSequenzaParole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risultati_esercizi_sequenza_parole, container, false);

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

        seekBarEsercizioSequenzaParole = view.findViewById(R.id.seekBarScorrimentoAudioEsercizioSequenzaParole);
        imageButtonPlay = view.findViewById(R.id.playButton);
        imageButtonPause = view.findViewById(R.id.pauseButton);

        imageViewCheck = view.findViewById(R.id.imageViewCheckEsercizio);
        imageViewWrong = view.findViewById(R.id.imageViewWrongEsercizio);
        linearLayoutRispostaData = view.findViewById(R.id.linearLayoutRispostaData);
        imageViewNonSvoltoEsercizio = view.findViewById(R.id.imageViewNonSvoltoEsercizio);
        playButtonRisposta = view.findViewById(R.id.imageButtonAvviaAudioRegistrato);
        pauseButtonRisposta = view.findViewById(R.id.imageButtonPausaAudioRegistrato);
        pauseButtonRisposta.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.mEsercizioSequenzaParole = getmEsercizioSequenzaParoleFromViewModel();

        this.audioRecorder = initAudioRecorder();
        this.audioPlayerLink = new AudioPlayerLink(mEsercizioSequenzaParole.getAudioEsercizioSequenzaParole());

        this.mMediaPlayer = audioPlayerLink.getMediaPlayer();

        if(isNonSvolto()) {
            linearLayoutRispostaData.setVisibility(View.INVISIBLE);
            imageViewWrong.setVisibility(View.GONE);
            imageViewCheck.setVisibility(View.GONE);
            imageViewNonSvoltoEsercizio.setVisibility(View.VISIBLE);
        }
        else {
            if (isCorrect()) {
                imageViewCheck.setVisibility(View.VISIBLE);
                imageViewWrong.setVisibility(View.GONE);
            } else {
                imageViewCheck.setVisibility(View.GONE);
                imageViewWrong.setVisibility(View.VISIBLE);
            }
            playButtonRisposta.setOnClickListener(v -> playAudio());
            pauseButtonRisposta.setOnClickListener(v -> stopAudio());
            this.audioPlayerLinkRegistrazione = new AudioPlayerLink(mEsercizioSequenzaParole.getRisultatoEsercizio().getAudioRegistrato());

        }

        imageButtonPlay.setOnClickListener(v -> avviaRiproduzioneAudio());
        imageButtonPause.setOnClickListener(v -> stoppaRiproduzioneAudio());
    }

    private boolean isNonSvolto() {
        return this.mEsercizioSequenzaParole.getRisultatoEsercizio() == null;
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

        seekBarEsercizioSequenzaParole.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                seekBarEsercizioSequenzaParole.setProgress((int) (mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration())));

        final int delay = 5;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying() && seekBarEsercizioSequenzaParole != null) {
                    seekBarEsercizioSequenzaParole.setProgress((int) (mMediaPlayer.getCurrentPosition() * 100 / mMediaPlayer.getDuration()));
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

    private void playAudio() {
        playButtonRisposta.setVisibility(View.GONE);
        pauseButtonRisposta.setVisibility(View.VISIBLE);
        audioPlayerLinkRegistrazione.playAudio();
    }

    private void stopAudio() {
        playButtonRisposta.setVisibility(View.VISIBLE);
        pauseButtonRisposta.setVisibility(View.GONE);
        audioPlayerLinkRegistrazione.stopAudio();
    }

    private boolean isCorrect() {
        return mEsercizioSequenzaParole.getRisultatoEsercizio().isEsitoCorretto();
    }

    private EsercizioSequenzaParole getmEsercizioSequenzaParoleFromViewModel(){

        for (Paziente pazienti : mLogopedistaViewModel.getLogopedistaLiveData().getValue().getPazienti()) {
            if(pazienti.getIdProfilo().equals(idPaziente)){
                return (EsercizioSequenzaParole) pazienti.getTerapie().get(indiceTerapia).getScenariGioco().get(indiceScenario).getEsercizi().get(indiceEsercizio);
            }
        }
        return new EsercizioSequenzaParole(0,0,"","","","");
    }

}
