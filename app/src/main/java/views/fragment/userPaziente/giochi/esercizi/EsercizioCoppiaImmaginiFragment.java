package views.fragment.userPaziente.giochi.esercizi;

import android.annotation.SuppressLint;

import android.media.MediaPlayer;

import android.os.Bundle;

import android.os.Handler;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.MotionEvent;

import android.view.View;

import android.view.ViewGroup;

import android.view.animation.AlphaAnimation;

import android.view.animation.Animation;

import android.widget.FrameLayout;

import android.widget.ImageButton;

import android.widget.ImageView;

import android.widget.SeekBar;

import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import it.uniba.dib.pronuntiapp.R;

import models.domain.esercizi.EsercizioCoppiaImmagini;

import models.domain.esercizi.risultati.RisultatoEsercizioCoppiaImmagini;

import models.domain.scenariGioco.ScenarioGioco;

import models.domain.terapie.Terapia;

import models.utils.audioPlayer.AudioPlayerLink;

import viewsModels.pazienteViewsModels.PazienteViewsModels;

import viewsModels.pazienteViewsModels.controller.CoppiaImmaginiController;

import views.fragment.userPaziente.giochi.FineScenarioView;


public class EsercizioCoppiaImmaginiFragment extends AbstractFineScenarioFragment {

    private TextView textViewEsercizioCoppiaImmagini, textViewEsercizioPlaySuggestion;

    private SeekBar seekBarScorrimentoAudioEsercizioCoppiaImmagini;

    private ImageButton playButton;

    private ImageButton pauseButton;

    private ImageView imageButtonImmagine1, imageButtonImmagine2;

    private FineScenarioView fineScenarioView;

    private ConstraintLayout constraintLayoutEsercizioCoppiaImmagini;

    private FrameLayout fl1ImmagineEsercizioCoppiaImmagini, fl2ImmagineEsercizioCoppiaImmagini;

    private boolean isLongClick = false;

    private boolean firstClickReproduced = false;

    private int correctImageView;

    private AudioPlayerLink audioPlayerLink;

    private MediaPlayer mMediaPlayer;

    private PazienteViewsModels mPazienteViewModel;

    private CoppiaImmaginiController mController;

    private EsercizioCoppiaImmagini mEsercizioCoppiaImmagini;

    private ScenarioGioco scenarioGioco;

    private Bundle bundle;

    private int indiceTerapia;

    private int indiceScenario;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_esercizi_coppia_immagini, container, false);

        this.mPazienteViewModel = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        this.mController = mPazienteViewModel.getCoppiaImmaginiController();

        fineScenarioView = view.findViewById(R.id.fineEsercizioView);
        constraintLayoutEsercizioCoppiaImmagini = view.findViewById(R.id.constraintLayoutEsercizioCoppiaImmagini);
        fl1ImmagineEsercizioCoppiaImmagini = view.findViewById(R.id.fl1ImmagineEsercizioCoppiaImmagini);
        fl2ImmagineEsercizioCoppiaImmagini = view.findViewById(R.id.fl2ImmagineEsercizioCoppiaImmagini);
        textViewEsercizioCoppiaImmagini = view.findViewById(R.id.textViewEsercizioCoppiaImmagini);
        textViewEsercizioPlaySuggestion = view.findViewById(R.id.textViewEsercizioPlaySuggestion);
        seekBarScorrimentoAudioEsercizioCoppiaImmagini = view.findViewById(R.id.seekBarScorrimentoAudioEsercizioCoppiaImmagini);
        playButton = view.findViewById(R.id.playButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        imageButtonImmagine1=view.findViewById(R.id.imageView1ImmagineEsercizioCoppiaImmagini);
        imageButtonImmagine2=view.findViewById(R.id.imageView2ImmagineEsercizioCoppiaImmagini);


        this.correctImageView = mController.generaPosizioneImmagineCorretta();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        indiceScenario = bundle.getInt("indiceScenarioCorrente");
        indiceTerapia = bundle.getInt("indiceTerapia");
        mPazienteViewModel.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {
            List<Terapia> terapie = paziente.getTerapie();
            scenarioGioco = terapie.get(indiceTerapia).getListScenariGioco().get(bundle.getInt("indiceScenarioCorrente"));
            mEsercizioCoppiaImmagini = (EsercizioCoppiaImmagini) scenarioGioco.getlistEsercizioRealizzabile().get(bundle.getInt("indiceEsercizio"));


            this.mController.setEsercizioCoppiaImmagini(mEsercizioCoppiaImmagini);


            this.audioPlayerLink = new AudioPlayerLink(mEsercizioCoppiaImmagini.getAudioEsercizioCoppiaImmagini());
            this.mMediaPlayer = audioPlayerLink.getMediaPlayer();

            if (correctImageView == 1) {
                Picasso.get().load(mEsercizioCoppiaImmagini.getImmagineCorrettaEsercizioCoppiaImmagini()).into(imageButtonImmagine1);
                Picasso.get().load(mEsercizioCoppiaImmagini.getImmagineErrataEsercizioCoppiaImmagini()).into(imageButtonImmagine2);
            } else {
                Picasso.get().load(mEsercizioCoppiaImmagini.getImmagineCorrettaEsercizioCoppiaImmagini()).into(imageButtonImmagine2);
                Picasso.get().load(mEsercizioCoppiaImmagini.getImmagineErrataEsercizioCoppiaImmagini()).into(imageButtonImmagine1);
            }

            playButton.setOnClickListener(v -> avviaRiproduzioneAudio());
            pauseButton.setOnClickListener(v -> stoppaRiproduzioneAudio());

            imageButtonImmagine1.setOnClickListener(v -> mostraSuggerimento());
            imageButtonImmagine2.setOnClickListener(v -> mostraSuggerimento());
        });
    }

    private void avviaRiproduzioneAudio() {
        if (!firstClickReproduced) {
            abilitaCompletamento();
        }
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        inizializzaBarraAvanzamento();
        audioPlayerLink.playAudio();
    }

    public void stoppaRiproduzioneAudio() {
        playButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        audioPlayerLink.playAudio();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void abilitaCompletamento(){
        firstClickReproduced = true;
        textViewEsercizioPlaySuggestion.setVisibility(View.GONE);
        imageButtonImmagine1.setOnClickListener(v -> {
            v.setOnClickListener(null);
            imageButtonImmagine2.setOnClickListener(null);
            borderImageSelector(fl1ImmagineEsercizioCoppiaImmagini);
            new Handler().postDelayed(() -> completaEsercizio(1), 1000);
        });
        imageButtonImmagine2.setOnClickListener(v -> {
            v.setOnClickListener(null);
            imageButtonImmagine1.setOnClickListener(null);
            borderImageSelector(fl2ImmagineEsercizioCoppiaImmagini);
            new Handler().postDelayed(() -> completaEsercizio(2), 1000);
        });

        imageButtonImmagine1.setOnLongClickListener(v -> {
            borderImageSelector(fl1ImmagineEsercizioCoppiaImmagini);
            return true;
        });
        imageButtonImmagine1.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!isLongClick) {
                    fl1ImmagineEsercizioCoppiaImmagini.setBackground(null);
                }
                isLongClick = false;
            }
            return false;
        });

        imageButtonImmagine2.setOnLongClickListener(v -> {
            borderImageSelector(fl2ImmagineEsercizioCoppiaImmagini);
            return true;
        });
        imageButtonImmagine2.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!isLongClick) {
                    fl2ImmagineEsercizioCoppiaImmagini.setBackground(null);
                }
                isLongClick = false;
            }
            return false;
        });
    }

    public void completaEsercizio(int immagineScelta) {

        boolean esito;

        Bundle bundle = new Bundle();
        bundle.putInt("indiceScenarioGioco",indiceScenario);

        if (mController.verificaSceltaImmagine(immagineScelta, correctImageView)) {
            esito = true;
            mPazienteViewModel.getPazienteLiveData().getValue().incrementaValuta(mEsercizioCoppiaImmagini.getRicompensaCorretto());
            mPazienteViewModel.getPazienteLiveData().getValue().incrementaPunteggioTotale(mEsercizioCoppiaImmagini.getRicompensaCorretto());
            setEsitoEsercizio(esito);

            if(checkEndScenery(scenarioGioco)){
                bundle.putBoolean("checkFineScenario", true);
                addRicompensaScenario();
                fineScenarioView.setEsercizioCorretto(mEsercizioCoppiaImmagini.getRicompensaCorretto(), R.id.action_esercizioCoppiaImmagini_to_scenarioFragment, this, bundle);
            }
            else {
                fineScenarioView.setEsercizioCorretto(mEsercizioCoppiaImmagini.getRicompensaCorretto(), R.id.action_esercizioCoppiaImmagini_to_scenarioFragment, this, bundle);
            }

        } else {
            esito = false;
            mPazienteViewModel.getPazienteLiveData().getValue().incrementaValuta(mEsercizioCoppiaImmagini.getRicompensaErrato());
            mPazienteViewModel.getPazienteLiveData().getValue().incrementaPunteggioTotale(mEsercizioCoppiaImmagini.getRicompensaErrato());
            setEsitoEsercizio(esito);

            if(checkEndScenery(scenarioGioco)){
                bundle.putBoolean("checkFineScenario", true);
                addRicompensaScenario();
                fineScenarioView.setEsercizioSbagliato(mEsercizioCoppiaImmagini.getRicompensaErrato(), R.id.action_esercizioCoppiaImmagini_to_scenarioFragment, this, bundle);
            }
            else  fineScenarioView.setEsercizioSbagliato(mEsercizioCoppiaImmagini.getRicompensaErrato(), R.id.action_esercizioCoppiaImmagini_to_scenarioFragment, this, bundle);

        }

        constraintLayoutEsercizioCoppiaImmagini.setVisibility(View.GONE);

        Log.d("EsercizioCoppiaImmaginiFragment.completaEsercizio()", "Esercizio completato: " + mPazienteViewModel.getPazienteLiveData().getValue());
        Log.d("EsercizioCoppiaImmaginiFragment.completaEsercizio()", "Esercizio completato: " + mEsercizioCoppiaImmagini);
        Log.d("EsercizioCoppiaImmaginiFragment.completaEsercizio()", "Esercizio completato: " + mPazienteViewModel.getPazienteLiveData().getValue());

    }

    private void  addRicompensaScenario(){
        int ricompensaFinale = scenarioGioco.getRicompensaFinale();
        mPazienteViewModel.getPazienteLiveData().getValue().incrementaValuta(ricompensaFinale);
        mPazienteViewModel.getPazienteLiveData().getValue().incrementaPunteggioTotale(ricompensaFinale);
        mPazienteViewModel.aggiornaPazienteRemoto();
    }

    private void setEsitoEsercizio(Boolean esito){
        RisultatoEsercizioCoppiaImmagini risultatoEsercizioCoppiaImmagini = new RisultatoEsercizioCoppiaImmagini(esito);
        mPazienteViewModel.setRisultatoEsercizioCoppiaImmaginePaziente(bundle.getInt("indiceScenarioCorrente"),bundle.getInt("indiceEsercizio"),bundle.getInt("indiceTerapia"),risultatoEsercizioCoppiaImmagini);
        mPazienteViewModel.aggiornaPazienteRemoto();
    }

    private void borderImageSelector(FrameLayout immagine){
        immagine.setBackgroundResource(R.drawable.rettangolo_giallo);
    }

    private void mostraSuggerimento(){

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);

        fadeIn.setDuration(500);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewEsercizioPlaySuggestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

        });
        textViewEsercizioPlaySuggestion.startAnimation(fadeIn);
        new Handler().postDelayed(() -> {
            AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
            fadeOut.setDuration(500);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    textViewEsercizioPlaySuggestion.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}

            });
            textViewEsercizioPlaySuggestion.startAnimation(fadeOut);
        }, 10000);
    }

    private void inizializzaBarraAvanzamento() {

        mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
            Log.d("EsercizioCoppiaImmagini", "Audio completato");
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
        });

        seekBarScorrimentoAudioEsercizioCoppiaImmagini.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                seekBarScorrimentoAudioEsercizioCoppiaImmagini.setProgress((int) (mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration()));
            }
        });

        final int delay = 5; // Milliseconds
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying() && seekBarScorrimentoAudioEsercizioCoppiaImmagini != null) {
                    seekBarScorrimentoAudioEsercizioCoppiaImmagini.setProgress((int) (mMediaPlayer.getCurrentPosition() * 100 / mMediaPlayer.getDuration()));
                }
                handler.postDelayed(this, delay);
            }
        };

        mMediaPlayer.setOnPreparedListener(mp -> handler.postDelayed(runnable, delay));
    }

}

