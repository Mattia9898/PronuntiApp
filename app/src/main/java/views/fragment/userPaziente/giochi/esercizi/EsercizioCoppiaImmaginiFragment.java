package views.fragment.userPaziente.giochi.esercizi;


import it.uniba.dib.pronuntiapp.R;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import android.media.MediaPlayer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;

import viewsModels.pazienteViewsModels.controller.CoppiaImmaginiController;
import viewsModels.pazienteViewsModels.PazienteViewsModels;

import views.fragment.userPaziente.giochi.FineScenario;

import java.util.List;

import androidx.lifecycle.ViewModelProvider;
import androidx.constraintlayout.widget.ConstraintLayout;

import models.utils.audioPlayer.AudioPlayerLink;
import models.domain.esercizi.EsercizioCoppiaImmagini;
import models.domain.esercizi.risultati.RisultatoEsercizioCoppiaImmagini;
import models.domain.scenariGioco.ScenarioGioco;
import models.domain.terapie.Terapia;


public class EsercizioCoppiaImmaginiFragment extends AbstractFineScenarioFragment {

    private int correctImage;

    private int indexTherapy;

    private int indexScenery;

    private boolean firstClick = false;

    private boolean isProlongedClick = false;

    private TextView playSuggestion;

    private SeekBar seekBar;

    private ImageView buttonFirstImage, buttonSecondImage;

    private FineScenario fineScenario;

    private ConstraintLayout mainConstraintLayout;

    private FrameLayout firstImage, secondImage;

    private ImageButton buttonPlay;

    private ImageButton buttonPause;

    private AudioPlayerLink audioPlayerLink;

    private MediaPlayer mediaPlayer;

    private PazienteViewsModels pazienteViewModel;

    private CoppiaImmaginiController coppiaImmaginiController;

    private EsercizioCoppiaImmagini esercizioCoppiaImmagini;

    private ScenarioGioco scenarioGioco;

    private Bundle bundle;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_esercizi_coppia_immagini, container, false);

        this.pazienteViewModel = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        this.coppiaImmaginiController = pazienteViewModel.getCoppiaImmaginiController();

        fineScenario = view.findViewById(R.id.fineScenario);
        mainConstraintLayout = view.findViewById(R.id.mainConstraintLayout);

        firstImage = view.findViewById(R.id.firstImage);
        secondImage = view.findViewById(R.id.secondImage);

        playSuggestion = view.findViewById(R.id.playSuggestion);
        seekBar = view.findViewById(R.id.seekBar);

        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonPause = view.findViewById(R.id.buttonPause);

        buttonFirstImage = view.findViewById(R.id.buttonFirstImage);
        buttonSecondImage = view.findViewById(R.id.buttonSecondImage);

        this.correctImage = coppiaImmaginiController.generaPosizioneImmagineCorretta();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        indexScenery = bundle.getInt("actualIndexScenery");
        indexTherapy = bundle.getInt("indexTherapy");

        pazienteViewModel.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {

            List<Terapia> listTherapies = paziente.getTerapie();
            scenarioGioco = listTherapies.get(indexTherapy).getListScenariGioco().get(bundle.getInt("actualIndexTherapy"));
            esercizioCoppiaImmagini = (EsercizioCoppiaImmagini) scenarioGioco.getlistEsercizioRealizzabile().get(bundle.getInt("indiceEsercizio"));

            this.coppiaImmaginiController.setEsercizioCoppiaImmagini(esercizioCoppiaImmagini);
            this.audioPlayerLink = new AudioPlayerLink(esercizioCoppiaImmagini.getAudioEsercizioCoppiaImmagini());
            this.mediaPlayer = audioPlayerLink.getMediaPlayer();

            if (correctImage == 1) {
                Picasso.get().load(esercizioCoppiaImmagini.getImmagineCorrettaEsercizioCoppiaImmagini()).into(buttonFirstImage);
                Picasso.get().load(esercizioCoppiaImmagini.getImmagineErrataEsercizioCoppiaImmagini()).into(buttonSecondImage);
            } else {
                Picasso.get().load(esercizioCoppiaImmagini.getImmagineCorrettaEsercizioCoppiaImmagini()).into(buttonSecondImage);
                Picasso.get().load(esercizioCoppiaImmagini.getImmagineErrataEsercizioCoppiaImmagini()).into(buttonFirstImage);
            }

            buttonPlay.setOnClickListener(v -> startReproductionAudio());
            buttonPause.setOnClickListener(v -> stopReproductionAudio());

            buttonFirstImage.setOnClickListener(v -> showSuggestion());
            buttonSecondImage.setOnClickListener(v -> showSuggestion());
        });
    }

    private void setResultExercise(Boolean resultExercise){
        RisultatoEsercizioCoppiaImmagini risultatoEsercizioCoppiaImmagini = new RisultatoEsercizioCoppiaImmagini(resultExercise);
        pazienteViewModel.setRisultatoEsercizioCoppiaImmaginePaziente(
                bundle.getInt("indiceScenarioCorrente"),
                bundle.getInt("indiceEsercizio"),
                bundle.getInt("indiceTerapia"),
                risultatoEsercizioCoppiaImmagini);
        pazienteViewModel.aggiornaPazienteRemoto();
    }

    private void addSceneryReward(){
        int reward = scenarioGioco.getRicompensaFinale();
        pazienteViewModel.getPazienteLiveData().getValue().incrementaValuta(reward);
        pazienteViewModel.getPazienteLiveData().getValue().incrementaPunteggioTotale(reward);
        pazienteViewModel.aggiornaPazienteRemoto();
    }

    private void setBorderRettangoloGiallo(FrameLayout image){
        image.setBackgroundResource(R.drawable.rettangolo_giallo);
    }

    public void stopReproductionAudio() {
        buttonPlay.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.GONE);
        audioPlayerLink.playAudio();
    }

    private void startReproductionAudio() {
        if (!firstClick) {
            enableFinishing();
        }
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

        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                seekBar.setProgress((int) (mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration()));
            }
        });

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

    @SuppressLint("ClickableViewAccessibility")
    private void enableFinishing(){
        firstClick = true;
        playSuggestion.setVisibility(View.GONE);

        buttonFirstImage.setOnClickListener(v -> {
            v.setOnClickListener(null);
            buttonSecondImage.setOnClickListener(null);
            setBorderRettangoloGiallo(firstImage);
            new Handler().postDelayed(() -> completeExercise(1), 1000);
        });

        buttonSecondImage.setOnClickListener(v -> {
            v.setOnClickListener(null);
            buttonFirstImage.setOnClickListener(null);
            setBorderRettangoloGiallo(secondImage);
            new Handler().postDelayed(() -> completeExercise(2), 1000);
        });

        buttonFirstImage.setOnLongClickListener(v -> {
            setBorderRettangoloGiallo(firstImage);
            return true;
        });

        buttonFirstImage.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!isProlongedClick) {
                    firstImage.setBackground(null);
                }
                isProlongedClick = false;
            }
            return false;
        });

        buttonSecondImage.setOnLongClickListener(v -> {
            setBorderRettangoloGiallo(secondImage);
            return true;
        });

        buttonSecondImage.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!isProlongedClick) {
                    secondImage.setBackground(null);
                }
                isProlongedClick = false;
            }
            return false;
        });
    }

    public void completeExercise(int pickedImage) {

        boolean esito;
        Bundle bundle = new Bundle();
        bundle.putInt("indexSceneryGame",indexScenery);

        if (coppiaImmaginiController.verificaSceltaImmagine(pickedImage, correctImage)) {
            esito = true;
            pazienteViewModel.getPazienteLiveData().getValue().
                    incrementaValuta(esercizioCoppiaImmagini.getRicompensaCorretto());
            pazienteViewModel.getPazienteLiveData().getValue().
                    incrementaPunteggioTotale(esercizioCoppiaImmagini.getRicompensaCorretto());
            setResultExercise(esito);

            if(checkEndScenery(scenarioGioco)){
                bundle.putBoolean("checkFineScenario", true);
                addSceneryReward();
                fineScenario.setCorrectExercise(esercizioCoppiaImmagini.getRicompensaCorretto(),
                        R.id.action_esercizioCoppiaImmagini_to_scenarioFragment, this, bundle);
            }
            else {
                fineScenario.setCorrectExercise(esercizioCoppiaImmagini.getRicompensaCorretto(),
                        R.id.action_esercizioCoppiaImmagini_to_scenarioFragment, this, bundle);
            }

        } else {
            esito = false;
            pazienteViewModel.getPazienteLiveData().getValue().
                    incrementaValuta(esercizioCoppiaImmagini.getRicompensaErrato());
            pazienteViewModel.getPazienteLiveData().getValue().
                    incrementaPunteggioTotale(esercizioCoppiaImmagini.getRicompensaErrato());
            setResultExercise(esito);

            if(checkEndScenery(scenarioGioco)){
                bundle.putBoolean("checkFineScenario", true);
                addSceneryReward();
                fineScenario.setWrongExercise(esercizioCoppiaImmagini.getRicompensaErrato(),
                        R.id.action_esercizioCoppiaImmagini_to_scenarioFragment, this, bundle);
            }
            else  fineScenario.setWrongExercise(esercizioCoppiaImmagini.getRicompensaErrato(),
                    R.id.action_esercizioCoppiaImmagini_to_scenarioFragment, this, bundle);

        }

        mainConstraintLayout.setVisibility(View.GONE);

        Log.d("EsercizioCoppiaImmaginiFragment.completaEsercizio()",
                "Esercizio completato: " + pazienteViewModel.getPazienteLiveData().getValue());
        Log.d("EsercizioCoppiaImmaginiFragment.completaEsercizio()",
                "Esercizio completato: " + esercizioCoppiaImmagini);
        Log.d("EsercizioCoppiaImmaginiFragment.completaEsercizio()",
                "Esercizio completato: " + pazienteViewModel.getPazienteLiveData().getValue());

    }

    private void showSuggestion(){

        AlphaAnimation alphaAnimationFadeIn = new AlphaAnimation(0.0f, 1.0f);

        alphaAnimationFadeIn.setDuration(500);
        alphaAnimationFadeIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                playSuggestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

        });

        playSuggestion.startAnimation(alphaAnimationFadeIn);

        new Handler().postDelayed(() -> {
            AlphaAnimation alphaAnimationFadeOut = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimationFadeOut.setDuration(500);
            alphaAnimationFadeOut.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    playSuggestion.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}

            });

            playSuggestion.startAnimation(alphaAnimationFadeOut);

        }, 10000);
    }


}

