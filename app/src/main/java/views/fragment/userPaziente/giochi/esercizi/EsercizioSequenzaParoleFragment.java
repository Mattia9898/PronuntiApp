package views.fragment.userPaziente.giochi.esercizi;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import android.net.Uri;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import models.database.ComandiFirebaseStorage;
import it.uniba.dib.pronuntiapp.R;
import models.domain.esercizi.EsercizioSequenzaParole;
import models.domain.esercizi.risultati.RisultatoEsercizioSequenzaParole;
import models.domain.scenariGioco.ScenarioGioco;
import models.domain.terapie.Terapia;
import models.API.FFmpegKitAPI.AudioConverter;
import models.utils.audioPlayer.AudioPlayerLink;
import models.utils.audioRecorder.AudioRecorder;
import viewsModels.pazienteViewsModels.PazienteViewsModels;
import viewsModels.pazienteViewsModels.controller.SequenzaParoleController;
import views.dialog.InfoDialog;
import views.dialog.PermessiDialog;
import views.dialog.RichiestaConfermaDialog;
import views.fragment.userPaziente.giochi.FineScenarioEsercizioView;

public class EsercizioSequenzaParoleFragment extends AbstractFineScenarioEsercizioFragment {

    private FineScenarioEsercizioView fineScenarioEsercizioView;
    private ConstraintLayout constraintLayoutEsercizioSequenzaParole;
    private SeekBar seekBarEsercizioSequenzaParole;
    private ImageButton imageButtonPlay, imageButtonPause;
    private View viewAnimazioneMic, viewConfirmMic, viewStopMic;
    private ImageButton imageButtonAvviaRegistrazione, buttonCompletaEsercizio;
    private ImageView imageViewConfermaRegistrazione;
    private ScaleAnimation animazioneButtonMic;
    private TextView textViewEsercizioMicSuggestion;
    private View viewClickForSuggestion;
    private boolean firstClickReproduced = false;
    private AudioRecorder audioRecorder;
    private AudioPlayerLink audioPlayerLink;
    private MediaPlayer mMediaPlayer;
    private PazienteViewsModels mPazienteViewsModels;
    private SequenzaParoleController mSequenzaParoleController;
    private EsercizioSequenzaParole mEsercizioSequenzaParole;
    private ScenarioGioco scenarioGioco;
    private Bundle bundle;
    private int indiceScenario;
    private int indiceTerapia;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esercizio_sequenza_parole, container, false);

        this.mPazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        this.mSequenzaParoleController = mPazienteViewsModels.getSequenzaParoleController();

        fineScenarioEsercizioView = view.findViewById(R.id.fineEsercizioView);
        textViewEsercizioMicSuggestion = view.findViewById(R.id.textViewEsercizioMicSuggestion);
        viewClickForSuggestion = view.findViewById(R.id.viewClickForSuggestion);
        constraintLayoutEsercizioSequenzaParole = view.findViewById(R.id.constraintLayoutEsercizioSequenzaParole);
        seekBarEsercizioSequenzaParole = view.findViewById(R.id.seekBarScorrimentoAudioEsercizioSequenzaParole);
        imageButtonPlay = view.findViewById(R.id.playButton);
        imageButtonPause = view.findViewById(R.id.pauseButton);
        viewAnimazioneMic = view.findViewById(R.id.viewAnimationMic);
        viewConfirmMic = view.findViewById(R.id.viewConfirmMic);
        viewStopMic = view.findViewById(R.id.viewStopRegMic);
        imageButtonAvviaRegistrazione = view.findViewById(R.id.buttonAvviaRegistrazione);
        buttonCompletaEsercizio = view.findViewById(R.id.buttonCompletaEsercizio);
        imageViewConfermaRegistrazione = view.findViewById(R.id.confermaRegistrazioneImageView);

        setAnimazioneRegistrazione();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        indiceScenario = bundle.getInt("indiceScenarioCorrente");
        indiceTerapia = bundle.getInt("indiceTerapia");
        mPazienteViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {
            List<Terapia> terapie = paziente.getTerapie();
            scenarioGioco = terapie.get(indiceTerapia).getScenariGioco().get(bundle.getInt("indiceScenarioCorrente"));
            mEsercizioSequenzaParole = (EsercizioSequenzaParole) scenarioGioco.getEsercizi().get(bundle.getInt("indiceEsercizio"));
            this.mSequenzaParoleController.setEsercizioSequenzaParole(mEsercizioSequenzaParole);


            this.audioRecorder = initAudioRecorder();
            this.audioPlayerLink = new AudioPlayerLink(mEsercizioSequenzaParole.getAudioEsercizioSequenzaParole());
            this.mMediaPlayer = audioPlayerLink.getMediaPlayer();

            imageButtonPlay.setOnClickListener(v -> avviaRiproduzioneAudio());
            imageButtonPause.setOnClickListener(v -> stoppaRiproduzioneAudio());

            imageButtonAvviaRegistrazione.setOnClickListener(v -> invalidRegistrazione());
            viewStopMic.setOnClickListener(v -> stoppaRegistrazione());
            viewConfirmMic.setOnClickListener(v -> sovrascriviAudio());

            viewClickForSuggestion.setOnClickListener(v -> mostraSuggerimento());

            buttonCompletaEsercizio.setOnClickListener(v -> invalidConferma());
        });
    }

    private void avviaPrimaRegistrazione(){
        if (richiestaPermessi()) {
            avviaRegistrazione();

            imageViewConfermaRegistrazione.setVisibility(View.INVISIBLE);
            viewStopMic.setVisibility(View.VISIBLE);
            viewConfirmMic.setVisibility(View.GONE);

            Toast.makeText(getContext(), getContext().getString(R.string.startedRecording), Toast.LENGTH_SHORT).show();
        }
    }

    private void avviaRegistrazione() {
        imageButtonAvviaRegistrazione.setBackground(null);
        viewAnimazioneMic.startAnimation(animazioneButtonMic);
        imageViewConfermaRegistrazione.setVisibility(View.INVISIBLE);
        viewStopMic.setVisibility(View.VISIBLE);
        viewConfirmMic.setVisibility(View.GONE);

        audioRecorder.startRecording();

        buttonCompletaEsercizio.setOnClickListener(v -> completaEsercizio());
    }

    private void stoppaRegistrazione(){
        viewAnimazioneMic.clearAnimation();
        imageButtonAvviaRegistrazione.setBackground(getContext().getDrawable(R.drawable.circle_button_microfono_background));
        imageViewConfermaRegistrazione.setVisibility(View.VISIBLE);
        viewStopMic.setVisibility(View.GONE);
        viewConfirmMic.setVisibility(View.VISIBLE);

        audioRecorder.stopRecording();

        Toast.makeText(getContext(), getContext().getString(R.string.stopedRecording), Toast.LENGTH_SHORT).show();

        buttonCompletaEsercizio.setOnClickListener(v -> completaEsercizio());
    }

    private void invalidConferma() {
        Toast.makeText(getContext(), getContext().getString(R.string.recordBeforeCheck), Toast.LENGTH_SHORT).show();
    }

    private void invalidRegistrazione() {
        Toast.makeText(getContext(), getContext().getString(R.string.ascoltaPrima), Toast.LENGTH_SHORT).show();
    }

    private void avviaRiproduzioneAudio() {
        if (!firstClickReproduced) {
            abilitaCompletamento();
        }
        imageButtonPlay.setVisibility(View.GONE);
        imageButtonPause.setVisibility(View.VISIBLE);
        inizializzaBarraAvanzamento();
        audioPlayerLink.playAudio();
    }

    private void abilitaCompletamento() {
        firstClickReproduced = true;
        imageButtonAvviaRegistrazione.setOnClickListener(v -> avviaPrimaRegistrazione());
    }

    public void stoppaRiproduzioneAudio() {
        imageButtonPlay.setVisibility(View.VISIBLE);
        imageButtonPause.setVisibility(View.GONE);
        audioPlayerLink.stopAudio();
    }

    private void completaEsercizio(){
        uploadFileRegistrato().thenAccept(link -> {
            boolean esito;
            Bundle bundle = new Bundle();
            bundle.putInt("indiceScenarioGioco",indiceScenario);
            if (mSequenzaParoleController.verificaAudio(audioRecorder.getAudioFile(), getContext())) {
                esito = true;
                mPazienteViewsModels.getPazienteLiveData().getValue().incrementaValuta(mEsercizioSequenzaParole.getRicompensaCorretto());
                mPazienteViewsModels.getPazienteLiveData().getValue().incrementaPunteggioTot(mEsercizioSequenzaParole.getRicompensaCorretto());
                setEsitoEsercizio(esito, link);
                if (checkFineScenario(scenarioGioco)) {
                    bundle.putBoolean("checkFineScenario", true);
                    addRicompensaScenario();
                    fineScenarioEsercizioView.setEsercizioCorretto(mEsercizioSequenzaParole.getRicompensaCorretto(), R.id.action_esercizioSequenzaParole_to_scenarioFragment, this, bundle);
                } else
                    fineScenarioEsercizioView.setEsercizioCorretto(mEsercizioSequenzaParole.getRicompensaCorretto(), R.id.action_esercizioSequenzaParole_to_scenarioFragment, this, bundle);
            } else {
                esito = false;
                mPazienteViewsModels.getPazienteLiveData().getValue().incrementaValuta(mEsercizioSequenzaParole.getRicompensaErrato());
                mPazienteViewsModels.getPazienteLiveData().getValue().incrementaPunteggioTot(mEsercizioSequenzaParole.getRicompensaErrato());
                setEsitoEsercizio(esito, link);
                if (checkFineScenario(scenarioGioco)) {
                    bundle.putBoolean("checkFineScenario", true);
                    addRicompensaScenario();
                    fineScenarioEsercizioView.setEsercizioSbagliato(mEsercizioSequenzaParole.getRicompensaErrato(), R.id.action_esercizioSequenzaParole_to_scenarioFragment, this, bundle);
                } else
                    fineScenarioEsercizioView.setEsercizioSbagliato(mEsercizioSequenzaParole.getRicompensaErrato(), R.id.action_esercizioSequenzaParole_to_scenarioFragment, this, bundle);
            }

            constraintLayoutEsercizioSequenzaParole.setVisibility(View.GONE);


            Log.d("EsercizioSequenzaParoleFragment.completaEsercizio()", "Esercizio completato: " + mPazienteViewsModels.getPazienteLiveData().getValue());
            //mEsercizioSequenzaParole.setRisultatoEsercizio(new RisultatoEsercizioSequenzaParole(esito, audioRegistrato));
            Log.d("EsercizioSequenzaParoleFragment.completaEsercizio()", "Esercizio completato: " + mEsercizioSequenzaParole);
            Log.d("EsercizioSequenzaParoleFragment.completaEsercizio()", "Esercizio completato: " + mPazienteViewsModels.getPazienteLiveData().getValue());


        });
    }

    private void  addRicompensaScenario(){
        int ricompensaFinale = scenarioGioco.getRicompensaFinale();
        mPazienteViewsModels.getPazienteLiveData().getValue().incrementaValuta(ricompensaFinale);
        mPazienteViewsModels.getPazienteLiveData().getValue().incrementaPunteggioTot(ricompensaFinale);
        mPazienteViewsModels.aggiornaPazienteRemoto();
    }

    private void setEsitoEsercizio(Boolean esito, String link){
        RisultatoEsercizioSequenzaParole risultatoEsercizioSequenzaParole = new RisultatoEsercizioSequenzaParole(esito, link);
        mPazienteViewsModels.setRisultatoEsercizioSequenzaParolePaziente(bundle.getInt("indiceScenarioCorrente"), bundle.getInt("indiceEsercizio"),bundle.getInt("indiceTerapia"), risultatoEsercizioSequenzaParole);
        mPazienteViewsModels.aggiornaPazienteRemoto();
    }

    private CompletableFuture<String> uploadFileRegistrato(){
        CompletableFuture<String> future = new CompletableFuture<>();

        File fileConvertito = new File(getContext().getFilesDir(), "tempAudioConvertito.mp3");
        AudioConverter.convertiAudio(audioRecorder.getAudioFile(), fileConvertito);
        ComandiFirebaseStorage comandiFirebaseStorage = new ComandiFirebaseStorage();
        AtomicReference<String> audioRegistrato = new AtomicReference<>();
        String directoryCorrente = ComandiFirebaseStorage.AUDIO_REGISTRATI_SEQUENZA_PAROLE;

        comandiFirebaseStorage.uploadFileAndGetLink(Uri.fromFile(fileConvertito), directoryCorrente)
                .thenAccept(value ->{
                    audioRegistrato.set(value);
                })
                .thenRun(() -> {
                    future.complete(audioRegistrato.get());
                });
        return future;
    }

    private void sovrascriviAudio() {
        RichiestaConfermaDialog richiestaConfermaDialog = new RichiestaConfermaDialog(getContext(), getContext().getString(R.string.overwriteAudio), getContext().getString(R.string.overwriteAudioDescription));
        richiestaConfermaDialog.setOnConfermaButtonClickListener(this::avviaRegistrazione);
        richiestaConfermaDialog.setOnAnnullaButtonClickListener(() -> {});
        richiestaConfermaDialog.show();
    }

    private AudioRecorder initAudioRecorder() {
        File cartellaApp = getContext().getFilesDir();
        File audioRegistrazione = new File(cartellaApp, "tempAudioRegistrato");

        return new AudioRecorder(audioRegistrazione);
    }

    private void setAnimazioneRegistrazione() {
        animazioneButtonMic = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animazioneButtonMic.setDuration(500);
        animazioneButtonMic.setRepeatMode(Animation.REVERSE);
        animazioneButtonMic.setRepeatCount(Animation.INFINITE);
    }

    private void mostraSuggerimento(){
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(500);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewEsercizioMicSuggestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        textViewEsercizioMicSuggestion.startAnimation(fadeIn);
        new Handler().postDelayed(() -> {
            AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
            fadeOut.setDuration(500);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    textViewEsercizioMicSuggestion.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            textViewEsercizioMicSuggestion.startAnimation(fadeOut);
        }, 10000);
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


    private boolean richiestaPermessi() {
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

    private boolean checkPermissions(Activity currentactivity) {
        int recordAudioPermission = ContextCompat.checkSelfPermission(currentactivity, Manifest.permission.RECORD_AUDIO);
        return recordAudioPermission == PackageManager.PERMISSION_GRANTED;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    this.audioRecorder = initAudioRecorder();
                    avviaPrimaRegistrazione();
                } else {
                    InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.permissionDeniedInstructions), getString(R.string.infoOk));
                    infoDialog.show();
                    infoDialog.setOnConfermaButtonClickListener(() -> navigateTo(R.id.action_esercizioSequenzaParole_to_scenarioFragment));
                }
            });

    private void setPermissionDialog() {
        PermessiDialog permessiDialog = new PermessiDialog(getContext(), getString(R.string.permissionDeniedDescription));
        permessiDialog.show();
        permessiDialog.setOnConfermaButtonClickListener(() -> requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO));
        permessiDialog.setOnAnnullaButtonClickListener(() -> navigateTo(R.id.action_esercizioSequenzaParole_to_scenarioFragment));
    }

}
