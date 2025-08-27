package views.fragment.userPaziente.giochi.esercizi;


import it.uniba.dib.pronuntiapp.R;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import com.squareup.picasso.Picasso;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.os.Bundle;
import android.os.Handler;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import models.database.ComandiFirebaseStorage;
import models.utils.audioPlayer.AudioPlayerLink;
import models.utils.audioRecorder.AudioRecorder;
import views.dialog.InfoDialog;
import views.dialog.PermessiDialog;
import views.dialog.RequestConfirmDialog;
import models.API.FFmpegKitAPI.AudioConverter;
import models.domain.esercizi.EsercizioDenominazioneImmagini;
import models.domain.esercizi.risultati.RisultatoEsercizioDenominazioneImmagini;
import models.domain.scenariGioco.ScenarioGioco;
import models.domain.terapie.Terapia;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import views.fragment.userPaziente.giochi.FineScenario;
import viewsModels.pazienteViewsModels.PazienteViewsModels;
import viewsModels.pazienteViewsModels.controller.SceltaImmaginiController;


public class EsercizioDenominazioneImmaginiFragment extends AbstractFineScenarioFragment {


    private int indexScenery;

    private int indexTherapy;

    private int aidsCount = 3;

    private int aidsAvailable = 3;

    private View suggestionMicrophone;

    private View startMicrophone, stopMicrophone, confirmMicrophone;

    private View overlayBackground;

    private ImageView mainImage;

    private ImageView confirmRegistration;

    private ScaleAnimation scaleAnimation;

    private ConstraintLayout mainConstraintLayout;

    private ImageButton buttonAids;

    private ImageButton buttonCompleteExercise;

    private ImageButton buttonStartRegistration;

    private PazienteViewsModels pazienteViewsModels;

    private SceltaImmaginiController sceltaImmaginiController;

    private EsercizioDenominazioneImmagini esercizioDenominazioneImmagini;

    private ScenarioGioco scenarioGioco;

    private FineScenario fineScenario;

    private AudioRecorder audioRecorder;

    private Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esercizio_denominazione_immagini, container, false);

        this.pazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        this.sceltaImmaginiController = pazienteViewsModels.getSceltaImmaginiController();

        mainConstraintLayout = view.findViewById(R.id.mainConstraintLayout);
        mainConstraintLayout.setVisibility(View.VISIBLE);

        fineScenario = view.findViewById(R.id.fineScenarioView);

        buttonAids = view.findViewById(R.id.buttonAids);
        buttonCompleteExercise = view.findViewById(R.id.buttonCompleteExercise);
        buttonStartRegistration = view.findViewById(R.id.buttonStartRegistration);

        mainImage = view.findViewById(R.id.mainImage);
        overlayBackground = view.findViewById(R.id.overlayBackground);
        suggestionMicrophone = view.findViewById(R.id.suggestionMicrophone);

        startMicrophone = view.findViewById(R.id.startMicrophone);
        confirmMicrophone = view.findViewById(R.id.confirmMicrophone);
        stopMicrophone = view.findViewById(R.id.stopMicrophone);

        confirmRegistration = view.findViewById(R.id.confirmRegistration);
        confirmRegistration.setVisibility(View.INVISIBLE);

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
            scenarioGioco = listTherapies.get(indexTherapy).getListScenariGioco().get(indexScenery);
            esercizioDenominazioneImmagini = (EsercizioDenominazioneImmagini) scenarioGioco.
                    getlistEsercizioRealizzabile().get(bundle.getInt("indexExercise"));

            this.sceltaImmaginiController.setEsercizioDenominazioneImmagini(esercizioDenominazioneImmagini);

            this.audioRecorder = audioRecorder();
            Picasso.get().load(esercizioDenominazioneImmagini.getImmagineEsercizioDenominazioneImmagini()).into(mainImage);

            buttonStartRegistration.setOnClickListener(v -> startFirstRegistration());
            stopMicrophone.setOnClickListener(v -> stopRegistration());
            confirmMicrophone.setOnClickListener(v -> overrideAudio());

            buttonAids.setOnClickListener(v -> reproductionAids(esercizioDenominazioneImmagini.getAudioAiuto()));
            overlayBackground.setOnClickListener(v -> showSuggestions());

            buttonCompleteExercise.setOnClickListener(v -> invalidConfirm());
        });
    }

    private void startFirstRegistration() {
        if (requestPermissions()) {
            startRegistration();

            confirmRegistration.setVisibility(View.INVISIBLE);
            stopMicrophone.setVisibility(View.VISIBLE);
            confirmMicrophone.setVisibility(View.GONE);

            Toast.makeText(getContext(), getContext().getString(R.string.startedRecording), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRegistration() {
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
        buttonCompleteExercise.setOnClickListener(v -> invalidConfirm());
    }

    private AudioRecorder audioRecorder() {

        File appDirectory = getContext().getFilesDir();
        File audioRegistration = new File(appDirectory, "audioRegistrato");

        return new AudioRecorder(audioRegistration);
    }

    private void reproductionAids(String audioAid) {
        String remainedAids = null;
        AudioPlayerLink audioPlayerLink = new AudioPlayerLink(audioAid);

        switch (aidsCount) {
            case 3:
                audioPlayerLink.playAudio();
                remainedAids = String.valueOf(--aidsCount);
                Toast.makeText(getContext(), (remainedAids + " " + getContext().getString(R.string.aidRemaining)), Toast.LENGTH_SHORT).show();
                break;
            case 2:
                audioPlayerLink.playAudio();
                remainedAids = String.valueOf(--aidsCount);
                Toast.makeText(getContext(), (remainedAids + " " + getContext().getString(R.string.aidRemaining)), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                audioPlayerLink.playAudio();
                --aidsCount;
                Toast.makeText(getContext(), getContext().getString(R.string.noAidRemaining), Toast.LENGTH_SHORT).show();
                break;
            case 0:
                Toast.makeText(getContext(), getContext().getString(R.string.noAidRemaining), Toast.LENGTH_SHORT).show();
        }
    }

    private void completeExercise() {
        uploadRegisteredFile().thenAccept(link -> {
            boolean result;
            Bundle bundle = new Bundle();
            bundle.putInt("indexGameScenery", indexScenery);
            if (sceltaImmaginiController.verificaAudio(audioRecorder.getAudioRecorder(), getContext())) {
                result = true;
                pazienteViewsModels.getPazienteLiveData().getValue().
                        incrementaValuta(esercizioDenominazioneImmagini.getRicompensaCorretto());
                pazienteViewsModels.getPazienteLiveData().getValue().
                        incrementaPunteggioTotale(esercizioDenominazioneImmagini.getRicompensaCorretto());
                setResultExercise(result, link);

                if (checkEndScenery(scenarioGioco)) {
                    bundle.putBoolean("checkEndScenery", true);
                    addSceneryReward();
                    fineScenario.setCorrectExercise(esercizioDenominazioneImmagini.getRicompensaCorretto(),
                            R.id.action_esercizioDenominazioneImmagineFragment_to_scenarioFragment, this, bundle);
                } else
                    fineScenario.setCorrectExercise(esercizioDenominazioneImmagini.getRicompensaCorretto(),
                            R.id.action_esercizioDenominazioneImmagineFragment_to_scenarioFragment, this, bundle);
            } else {
                result = false;
                pazienteViewsModels.getPazienteLiveData().getValue().
                        incrementaValuta(esercizioDenominazioneImmagini.getRicompensaErrato());
                pazienteViewsModels.getPazienteLiveData().getValue().
                        incrementaPunteggioTotale(esercizioDenominazioneImmagini.getRicompensaErrato());
                setResultExercise(result, link);

                if (checkEndScenery(scenarioGioco)) {
                    bundle.putBoolean("checkEndScenery", true);
                    addSceneryReward();
                    fineScenario.setWrongExercise(esercizioDenominazioneImmagini.getRicompensaErrato(),
                            R.id.action_esercizioDenominazioneImmagineFragment_to_scenarioFragment, this, bundle);
                } else
                    fineScenario.setWrongExercise(esercizioDenominazioneImmagini.getRicompensaErrato(),
                            R.id.action_esercizioDenominazioneImmagineFragment_to_scenarioFragment, this, bundle);
            }

            mainConstraintLayout.setVisibility(View.GONE);

            Log.d("EsercizioDenominazioneImmagineFragment.completaEsercizio()",
                    "Esercizio completato: " + pazienteViewsModels.getPazienteLiveData().getValue());
            Log.d("EsercizioDenominazioneImmagineFragment.completaEsercizio()",
                    "Esercizio completato: " + esercizioDenominazioneImmagini);
            Log.d("EsercizioDenominazioneImmagineFragment.completaEsercizio()",
                    "Esercizio completato: " + pazienteViewsModels.getPazienteLiveData().getValue());

        });
    }

    private void setResultExercise(boolean result, String link){
        RisultatoEsercizioDenominazioneImmagini risultatoEsercizioDenominazioneImmagini =
                new RisultatoEsercizioDenominazioneImmagini(result, link, aidsAvailable - aidsCount);
        pazienteViewsModels.setRisultatoEsercizioDenominazioneImmaginiPaziente(
                bundle.getInt("actualIndexScenery"),
                bundle.getInt("indexExercise"),
                bundle.getInt("indexTherapy"),
                risultatoEsercizioDenominazioneImmagini);
        pazienteViewsModels.aggiornaPazienteRemoto();
    }

    private void invalidConfirm() {
        Toast.makeText(getContext(), getContext().getString(R.string.recordBeforeCheck), Toast.LENGTH_SHORT).show();
    }

    private void  addSceneryReward(){
        int reward = scenarioGioco.getRicompensaFinale();
        pazienteViewsModels.getPazienteLiveData().getValue().incrementaValuta(reward);
        pazienteViewsModels.getPazienteLiveData().getValue().incrementaPunteggioTotale(reward);
        pazienteViewsModels.aggiornaPazienteRemoto();
    }

    private CompletableFuture<String> uploadRegisteredFile(){

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        File convertedFile = new File(getContext().getFilesDir(), "tempAudioConvertitoDenominazione.mp3");
        AudioConverter.convertiAudio(audioRecorder.getAudioRecorder(), convertedFile);

        ComandiFirebaseStorage comandiFirebaseStorage = new ComandiFirebaseStorage();

        AtomicReference<String> registeredAudio = new AtomicReference<>();

        String actualDirectory = ComandiFirebaseStorage.AUDIO_DENOMINAZIONE_IMMAGINE_EXERCISE;

        comandiFirebaseStorage.uploadFileAndGetLink(Uri.fromFile(convertedFile), actualDirectory)
                .thenAccept(value ->{
                    registeredAudio.set(value);
                })
                .thenRun(() -> {
                    completableFuture.complete(registeredAudio.get());
                });
        return completableFuture;
    }

    private void overrideAudio(){
        RequestConfirmDialog requestConfirmDialog =
                new RequestConfirmDialog(getContext(), getContext().getString(R.string.overwriteAudio), getContext().getString(R.string.overwriteAudioDescription));
        requestConfirmDialog.setOnConfirmButtonClickListener(this::startRegistration);
        requestConfirmDialog.setOnCancelButtonClickListener(() -> {});
        requestConfirmDialog.show();
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
        permessiDialog.setOnCancelButtonClickListener(() -> navigationTo(R.id.action_esercizioDenominazioneImmagineFragment_to_scenarioFragment));
    }

    private boolean checkPermissions(Activity actualActivity) {
        int audioPermission = ContextCompat.checkSelfPermission(actualActivity, Manifest.permission.RECORD_AUDIO);
        return audioPermission == PackageManager.PERMISSION_GRANTED;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    this.audioRecorder = audioRecorder();
                    startFirstRegistration();
                } else {
                    InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.permissionDeniedInstructions), getString(R.string.infoOk));
                    infoDialog.show();
                    infoDialog.setOnConfirmButtonClickListener(() -> navigationTo(R.id.action_esercizioDenominazioneImmagineFragment_to_scenarioFragment));
                }
            });

    private void showSuggestions() {

        AlphaAnimation alphaAnimationFadeIn = new AlphaAnimation(0.0f, 1.0f);

        alphaAnimationFadeIn.setDuration(500);
        alphaAnimationFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                suggestionMicrophone.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        suggestionMicrophone.startAnimation(alphaAnimationFadeIn);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                AlphaAnimation alphaAnimationFadeOut = new AlphaAnimation(1.0f, 0.0f);

                alphaAnimationFadeOut.setDuration(500);
                alphaAnimationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        suggestionMicrophone.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                suggestionMicrophone.startAnimation(alphaAnimationFadeOut);
            }
        }, 10000);
    }


}
