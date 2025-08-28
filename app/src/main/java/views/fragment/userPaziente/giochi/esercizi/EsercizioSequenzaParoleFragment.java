package views.fragment.userPaziente.giochi.esercizi;


import it.uniba.dib.pronuntiapp.R;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.util.Log;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import models.API.AudioConverter;
import models.utils.audioPlayer.AudioPlayerLink;
import models.utils.audioRecorder.AudioRecorder;
import models.domain.esercizi.EsercizioSequenzaParole;
import models.domain.esercizi.risultati.RisultatoEsercizioSequenzaParole;
import models.domain.scenariGioco.ScenarioGioco;
import models.domain.terapie.Terapia;
import models.database.CommandsFirebaseStorage;

import views.fragment.userPaziente.giochi.FineScenario;
import views.dialog.InfoDialog;
import views.dialog.PermessiDialog;
import views.dialog.RequestConfirmDialog;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import viewsModels.pazienteViewsModels.PazienteViewsModels;
import viewsModels.pazienteViewsModels.controller.SequenzaParoleController;


public class EsercizioSequenzaParoleFragment extends AbstractFineScenarioFragment {


    private boolean firstClick = false;

    private int indexScenery;

    private int indexTherapy;

    private FineScenario fineScenario;

    private MediaPlayer mediaPlayer;

    private PazienteViewsModels pazienteViewsModels;

    private ScenarioGioco scenarioGioco;

    private ConstraintLayout mainConstraintLayout;

    private SeekBar seekBar;

    private ImageButton buttonPlay, buttonPause;

    private ImageButton buttonStartRegistration, buttonCompleteExercise;

    private View startMicrophone, confirmMicrophone, stopMicrophone;

    private View clickSuggestion;

    private AudioRecorder audioRecorder;

    private AudioPlayerLink audioPlayerLink;

    private SequenzaParoleController sequenzaParoleController;

    private EsercizioSequenzaParole esercizioSequenzaParole;

    private ImageView confirmRegistration;

    private ScaleAnimation scaleAnimation;

    private TextView exerciseMicrophoneSuggestion;

    private Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esercizio_sequenza_parole, container, false);

        this.pazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        this.sequenzaParoleController = pazienteViewsModels.getSequenzaParoleController();

        fineScenario = view.findViewById(R.id.fineScenarioView);

        exerciseMicrophoneSuggestion = view.findViewById(R.id.exerciseMicrophoneSuggestion);
        clickSuggestion = view.findViewById(R.id.clickSuggestion);

        mainConstraintLayout = view.findViewById(R.id.mainConstraintLayout);

        seekBar = view.findViewById(R.id.seekBar);

        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonPause = view.findViewById(R.id.buttonPause);

        startMicrophone = view.findViewById(R.id.startMicrophone);
        confirmMicrophone = view.findViewById(R.id.confirmMicrophone);

        stopMicrophone = view.findViewById(R.id.stopMicrophone);
        buttonStartRegistration = view.findViewById(R.id.buttonStartRegistration);
        buttonCompleteExercise = view.findViewById(R.id.buttonCompleteExercise);
        confirmRegistration = view.findViewById(R.id.confirmRegistration);

        setAnimation();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        bundle = getArguments();
        indexScenery = bundle.getInt("actualIndexScenery");
        indexTherapy = bundle.getInt("indexTherapy");

        pazienteViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {
            List<Terapia> listTherapies = paziente.getTerapie();
            scenarioGioco = listTherapies.get(indexTherapy).getListScenariGioco().get(bundle.getInt("actualIndexScenery"));
            esercizioSequenzaParole = (EsercizioSequenzaParole) scenarioGioco.
                    getlistEsercizioRealizzabile().get(bundle.getInt("indexExercise"));

            this.sequenzaParoleController.setEsercizioSequenzaParole(esercizioSequenzaParole);
            this.audioRecorder = audioRecorder();
            this.audioPlayerLink = new AudioPlayerLink(esercizioSequenzaParole.getAudioEsercizioSequenzaParole());
            this.mediaPlayer = audioPlayerLink.getMediaPlayer();

            buttonPlay.setOnClickListener(v -> startReproductionAudio());
            buttonPause.setOnClickListener(v -> stopReproductionAudio());

            buttonStartRegistration.setOnClickListener(v -> invalidRegistration());
            stopMicrophone.setOnClickListener(v -> stopRegistration());
            confirmMicrophone.setOnClickListener(v -> overrideAudio());

            clickSuggestion.setOnClickListener(v -> showSuggestions());

            buttonCompleteExercise.setOnClickListener(v -> invalidConfirm());
        });
    }

    private void startFirstRegistration(){
        if (requestPermissions()) {
            startRegistration();

            confirmRegistration.setVisibility(View.INVISIBLE);
            stopMicrophone.setVisibility(View.VISIBLE);
            confirmMicrophone.setVisibility(View.GONE);

            Toast.makeText(getContext(), getContext().getString(R.string.startedRecording), Toast.LENGTH_SHORT).show();
        }
    }

    public void stopReproductionAudio() {
        buttonPlay.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.GONE);
        audioPlayerLink.stopAudio();
    }

    private void stopRegistration(){
        startMicrophone.clearAnimation();
        buttonStartRegistration.setBackground(getContext().getDrawable(R.drawable.circle_button_microfono_background));
        confirmRegistration.setVisibility(View.VISIBLE);
        stopMicrophone.setVisibility(View.GONE);
        confirmMicrophone.setVisibility(View.VISIBLE);

        audioRecorder.stopRecording();

        Toast.makeText(getContext(), getContext().getString(R.string.stopedRecording), Toast.LENGTH_SHORT).show();

        buttonCompleteExercise.setOnClickListener(v -> completeExercise());
    }

    private void startRegistration() {
        buttonStartRegistration.setBackground(null);
        startMicrophone.startAnimation(scaleAnimation);
        confirmRegistration.setVisibility(View.INVISIBLE);
        stopMicrophone.setVisibility(View.VISIBLE);
        confirmMicrophone.setVisibility(View.GONE);

        audioRecorder.startRecording();

        buttonCompleteExercise.setOnClickListener(v -> completeExercise());
    }

    private AudioRecorder audioRecorder() {

        File appDirectory = getContext().getFilesDir();
        File audioRegistration = new File(appDirectory, "audioRegistrato");

        return new AudioRecorder(audioRegistration);
    }

    private void completeExercise(){
        uploadRegisteredFile().thenAccept(link -> {
            boolean result;
            Bundle bundle = new Bundle();
            bundle.putInt("indexSceneryGame", indexScenery);
            if (sequenzaParoleController.verificaAudio(audioRecorder.getAudioRecorder(), getContext())) {
                result = true;
                pazienteViewsModels.getPazienteLiveData().getValue().
                        incrementaValuta(esercizioSequenzaParole.getRicompensaCorretto());
                pazienteViewsModels.getPazienteLiveData().getValue().
                        incrementaPunteggioTotale(esercizioSequenzaParole.getRicompensaCorretto());
                setResultExercise(result, link);

                if (checkEndScenery(scenarioGioco)) {
                    bundle.putBoolean("checkFineScenario", true);
                    addSceneryReward();
                    fineScenario.setCorrectExercise(esercizioSequenzaParole.
                            getRicompensaCorretto(), R.id.action_esercizioSequenzaParole_to_scenarioFragment, this, bundle);
                } else
                    fineScenario.setCorrectExercise(esercizioSequenzaParole.
                            getRicompensaCorretto(), R.id.action_esercizioSequenzaParole_to_scenarioFragment, this, bundle);
            } else {
                result = false;
                pazienteViewsModels.getPazienteLiveData().getValue().
                        incrementaValuta(esercizioSequenzaParole.getRicompensaErrato());
                pazienteViewsModels.getPazienteLiveData().getValue().
                        incrementaPunteggioTotale(esercizioSequenzaParole.getRicompensaErrato());
                setResultExercise(result, link);

                if (checkEndScenery(scenarioGioco)) {
                    bundle.putBoolean("checkFineScenario", true);
                    addSceneryReward();
                    fineScenario.setWrongExercise(esercizioSequenzaParole.
                            getRicompensaErrato(), R.id.action_esercizioSequenzaParole_to_scenarioFragment, this, bundle);
                } else
                    fineScenario.setWrongExercise(esercizioSequenzaParole.
                            getRicompensaErrato(), R.id.action_esercizioSequenzaParole_to_scenarioFragment, this, bundle);
            }

            mainConstraintLayout.setVisibility(View.GONE);

            Log.d("EsercizioSequenzaParoleFragment.completaEsercizio()",
                    "Esercizio completato: " + pazienteViewsModels.getPazienteLiveData().getValue());
            Log.d("EsercizioSequenzaParoleFragment.completaEsercizio()",
                    "Esercizio completato: " + esercizioSequenzaParole);
            Log.d("EsercizioSequenzaParoleFragment.completaEsercizio()",
                    "Esercizio completato: " + pazienteViewsModels.getPazienteLiveData().getValue());


        });
    }

    private void setResultExercise(Boolean esito, String link){
        RisultatoEsercizioSequenzaParole risultatoEsercizioSequenzaParole = new RisultatoEsercizioSequenzaParole(esito, link);
        pazienteViewsModels.setRisultatoEsercizioSequenzaParolePaziente(bundle.getInt("indiceScenarioCorrente"), bundle.getInt("indiceEsercizio"),bundle.getInt("indiceTerapia"), risultatoEsercizioSequenzaParole);
        pazienteViewsModels.aggiornaPazienteRemoto();
    }

    private void invalidConfirm() {
        Toast.makeText(getContext(), getContext().getString(R.string.recordBeforeCheck), Toast.LENGTH_SHORT).show();
    }

    private void invalidRegistration() {
        Toast.makeText(getContext(), getContext().getString(R.string.ascoltaPrima), Toast.LENGTH_SHORT).show();
    }

    private void  addSceneryReward(){
        int ricompensaFinale = scenarioGioco.getRicompensaFinale();
        pazienteViewsModels.getPazienteLiveData().getValue().incrementaValuta(ricompensaFinale);
        pazienteViewsModels.getPazienteLiveData().getValue().incrementaPunteggioTotale(ricompensaFinale);
        pazienteViewsModels.aggiornaPazienteRemoto();
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

    private CompletableFuture<String> uploadRegisteredFile(){

        CompletableFuture<String> future = new CompletableFuture<>();

        File fileConvertito = new File(getContext().getFilesDir(), "tempAudioConvertito.mp3");
        AudioConverter.convertAudio(audioRecorder.getAudioRecorder(), fileConvertito);

        CommandsFirebaseStorage commandsFirebaseStorage = new CommandsFirebaseStorage();

        AtomicReference<String> audioRegistrato = new AtomicReference<>();

        String directoryCorrente = CommandsFirebaseStorage.AUDIO_SEQUENZA_PAROLE_EXERCISE;

        commandsFirebaseStorage.uploadFileAndGetLink(Uri.fromFile(fileConvertito), directoryCorrente)
                .thenAccept(value ->{
                    audioRegistrato.set(value);
                })
                .thenRun(() -> {
                    future.complete(audioRegistrato.get());
                });
        return future;
    }

    private void overrideAudio() {
        RequestConfirmDialog richiestaConfermaDialog = new RequestConfirmDialog(getContext(), getContext().getString(R.string.overwriteAudio), getContext().getString(R.string.overwriteAudioDescription));
        richiestaConfermaDialog.setOnConfirmButtonClickListener(this::startRegistration);
        richiestaConfermaDialog.setOnCancelButtonClickListener(() -> {});
        richiestaConfermaDialog.show();
    }

    private void setAnimation() {
        scaleAnimation = new ScaleAnimation(
                1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
    }

    private boolean requestPermissions() {
        if (!checkPermissions(requireActivity())) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.RECORD_AUDIO)) {
                setPermissionDialog();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
            return false;
        } else {
            return true;
        }
    }

    private void setPermissionDialog() {
        PermessiDialog permessiDialog = new PermessiDialog(getContext(), getString(R.string.permissionDeniedDescription));
        permessiDialog.show();
        permessiDialog.setOnConfirmButtonClickListener(() -> requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO));
        permessiDialog.setOnCancelButtonClickListener(() -> navigationTo(R.id.action_esercizioSequenzaParole_to_scenarioFragment));
    }

    private boolean checkPermissions(Activity actualActivity) {
        int recordAudioPermission = ContextCompat.checkSelfPermission(actualActivity, Manifest.permission.RECORD_AUDIO);
        return recordAudioPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void enableFinishing() {
        firstClick = true;
        buttonStartRegistration.setOnClickListener(v -> startFirstRegistration());
    }

    private void showSuggestions(){

        AlphaAnimation alphaAnimationFadeIn = new AlphaAnimation(0.0f, 1.0f);

        alphaAnimationFadeIn.setDuration(500);
        alphaAnimationFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                exerciseMicrophoneSuggestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        exerciseMicrophoneSuggestion.startAnimation(alphaAnimationFadeIn);

        new Handler().postDelayed(() -> {

            AlphaAnimation alphaAnimationFadeOut = new AlphaAnimation(1.0f, 0.0f);

            alphaAnimationFadeOut.setDuration(500);
            alphaAnimationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    exerciseMicrophoneSuggestion.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            exerciseMicrophoneSuggestion.startAnimation(alphaAnimationFadeOut);
        }, 10000);
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

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    this.audioRecorder = audioRecorder();
                    startFirstRegistration();
                } else {
                    InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.permissionDeniedInstructions), getString(R.string.infoOk));
                    infoDialog.show();
                    infoDialog.setOnConfirmButtonClickListener(() -> navigationTo(R.id.action_esercizioSequenzaParole_to_scenarioFragment));
                }
            });

}
