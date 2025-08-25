package views.fragment.userLogopedista.elencoPazienti.terapia;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import it.uniba.dib.pronuntiapp.R;

import models.database.ComandiFirebaseStorage;
import models.database.TemplateEsercizioDAO;
import models.domain.esercizi.Esercizio;
import models.domain.esercizi.EsercizioCoppiaImmagini;
import models.domain.esercizi.EsercizioDenominazioneImmagini;
import models.domain.esercizi.EsercizioRealizzabile;
import models.domain.esercizi.EsercizioSequenzaParole;
import models.domain.esercizi.TemplateEsercizioCoppiaImmagini;
import models.domain.esercizi.TemplateEsercizioDenominazioneImmagini;
import models.domain.esercizi.TemplateEsercizioSequenzaParole;
import models.API.FFmpegKitAPI.AudioConverter;
import models.utils.audioRecorder.AudioRecorder;

import views.dialog.InfoDialog;
import views.dialog.PermessiDialog;
import views.fragment.AbstractNavigationFragment;


public class CreazioneEsercizioFragment extends AbstractNavigationFragment {

    private static final int PICK_FILE_1 = 1;

    private static final int PICK_FILE_2 = 2;

    private static final int PICK_FILE_3 = 3;

    private static int countExercise = 1;

    private int templateCoppiaImmagini = 0;

    private int templateDenominazioneImmagine = 0;

    private int templateSequenzaParole = 0;

    private int maxSizeTemplateCoppiaImmagini;

    private int maxSizeTemplateDenominazioneImmagine;

    private int maxSizeTemplateSequenzaParole;

    private LinearLayout typeExercise;

    private LinearLayout addExercise;

    private LinearLayout titleExercise;

    private LinearLayout creationExercise;

    private LinearLayout templateExercise;

    private LinearLayout pickTemplateExercise;

    private ScaleAnimation scaleAnimation;

    private Button buttonCoppiaImmagineExercise;

    private Button buttonConfirmCoppiaImmagineExercise;

    private Button buttonDenominazioneImmagineExercise;

    private Button buttonConfirmDenominazioneImmagineExercise;

    private Button buttonSequenzaParoleExercise;

    private Button buttonConfirmSequenzaParoleExercise;

    private Button buttonCreateExercise;

    private Button buttonTemplateExercise;

    private ImageButton buttonPreviousTemplateDenominazioneImmagine;

    private ImageButton buttonNextTemplateDenominazioneImmagine;

    private ImageButton buttonPreviousTemplateCoppiaImmagine;

    private ImageButton buttonNextTemplateCoppiaImmagine;

    private ImageButton buttonPreviousTemplateSequenzaParole;

    private ImageButton buttonNextTemplateSequenzaParole;

    private ImageButton buttonAudioDenominazioneImmagine;

    private ImageButton buttonAudioCoppiaImmagine;

    private ImageButton buttonAudioSequenzaParole;

    private AudioRecorder audioRecorder;

    private TextView textViewTitleExercise;

    private TextView wordsTemplateDenominazioneImmagine;

    private TextView firstWordTemplateSequenzaParole, secondWordTemplateSequenzaParole, thirdWordTemplateSequenzaParole;

    private ImageView arrowDown;

    private ImageView templateCoppiaImmagine;

    private ImageView openFileCoppiaImmagineCorrect, openFileCoppiaImmagineWrong;

    private ImageView openFileDenominazioneImmagineExercise;

    private CardView cardViewTemplateDenominazioneImmagineExercise;

    private CardView cardViewTemplateSequenzaParoleExercise;

    private CardView cardViewTemplateCoppiaImmagineExercise;

    private CardView cardViewCreateDenominazioneImmagineExercise;

    private CardView cardViewCreateSequenzaParoleExercise;

    private CardView cardViewCreateCoppiaImmagineExercise;

    private View startAudioDenominazioneImmagine, stopAudioDenominazioneImmagine;

    private View startAudioCoppiaImmagine, stopAudioCoppiaImmagine;

    private View startAudioSequenzaParole, stopAudioSequenzaParole;

    private TextInputEditText answerDenominazioneImmagineExercise;

    private TextInputEditText firstWord, secondWord, thirdWord;

    private TextInputEditText correctAnswerCoppiaImmagine, wrongAnswerCoppiaImmagine;

    private TextInputEditText correctRewardSequenzaParole, wrongRewardSequenzaParole;

    private TextInputEditText correctRewardDenominazioneImmagine, wrongRewardDenominazioneImmagine;

    private String imageDenominazioneImmagineExercise, audioDenominazioneImmagineExercise;

    private String audioSequenzaParoleExercise;

    private String correctImageCoppiaImmagine, wrongImageCoppiaImmagine, audioCoppiaImmagineExercise;

    private List<Esercizio> listTemplateExercise;

    private List<TemplateEsercizioCoppiaImmagini> listTemplateCoppiaImmagine;

    private List<TemplateEsercizioDenominazioneImmagini> listTemplateDenominazioneImmagine;

    private List<TemplateEsercizioSequenzaParole> listTemplateSequenzaParole;

    // exercise to create
    private EsercizioRealizzabile exerciseToCreate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_creazione_esercizio, container, false);

        listTemplateCoppiaImmagine = new ArrayList<>();
        listTemplateDenominazioneImmagine = new ArrayList<>();
        listTemplateSequenzaParole = new ArrayList<>();

        // title exercise
        textViewTitleExercise = view.findViewById(R.id.textViewTitleExercise);
        arrowDown = view.findViewById(R.id.arrowDown);
        titleExercise = view.findViewById(R.id.titleExercise);

        // add exercise
        addExercise = view.findViewById(R.id.addExercise);

        // type exercise
        typeExercise = view.findViewById(R.id.typeExercise);
        buttonDenominazioneImmagineExercise = view.findViewById(R.id.buttonDenominazioneImmagineExercise);
        buttonSequenzaParoleExercise = view.findViewById(R.id.buttonSequenzaParoleExercise);
        buttonCoppiaImmagineExercise = view.findViewById(R.id.buttonCoppiaImmagineExercise);

        // pick template or creation
        pickTemplateExercise = view.findViewById(R.id.pickTemplateExercise);
        buttonTemplateExercise = view.findViewById(R.id.buttonTemplateExercise);
        buttonCreateExercise = view.findViewById(R.id.buttonCreateExercise);

        // template exercise
        templateExercise = view.findViewById(R.id.templateExercise);

        // template exercise denominazione immagine
        cardViewTemplateDenominazioneImmagineExercise = view.findViewById(R.id.cardViewTemplateDenominazioneImmagineExercise);
        buttonPreviousTemplateDenominazioneImmagine = view.findViewById(R.id.buttonPreviousTemplateDenominazioneImmagine);
        wordsTemplateDenominazioneImmagine = view.findViewById(R.id.wordsTemplateDenominazioneImmagine);
        buttonNextTemplateDenominazioneImmagine = view.findViewById(R.id.buttonNextTemplateDenominazioneImmagine);

        // template exercise sequenza parole
        cardViewTemplateSequenzaParoleExercise = view.findViewById(R.id.cardViewTemplateSequenzaParoleExercise);
        buttonPreviousTemplateSequenzaParole = view.findViewById(R.id.buttonPreviousTemplateSequenzaParole);
        firstWordTemplateSequenzaParole = view.findViewById(R.id.firstWordTemplateSequenzaParole);
        secondWordTemplateSequenzaParole = view.findViewById(R.id.secondWordTemplateSequenzaParole);
        thirdWordTemplateSequenzaParole = view.findViewById(R.id.thirdWordTemplateSequenzaParole);
        buttonNextTemplateSequenzaParole = view.findViewById(R.id.buttonNextTemplateSequenzaParole);

        // template exercise coppia immagine
        cardViewTemplateCoppiaImmagineExercise = view.findViewById(R.id.cardViewTemplateCoppiaImmagineExercise);
        buttonPreviousTemplateCoppiaImmagine = view.findViewById(R.id.buttonPreviousTemplateCoppiaImmagine);
        templateCoppiaImmagine = view.findViewById(R.id.templateCoppiaImmagine);
        buttonNextTemplateCoppiaImmagine = view.findViewById(R.id.buttonNextTemplateCoppiaImmagine);

        // creation exercise
        creationExercise = view.findViewById(R.id.creationExercise);

        // creation exercise denominazione immagine
        cardViewCreateDenominazioneImmagineExercise = view.findViewById(R.id.cardViewCreateDenominazioneImmagineExercise);
        buttonAudioDenominazioneImmagine = view.findViewById(R.id.buttonAudioDenominazioneImmagine);
        startAudioDenominazioneImmagine = view.findViewById(R.id.startAudioDenominazioneImmagine);
        stopAudioDenominazioneImmagine = view.findViewById(R.id.stopAudioDenominazioneImmagine);
        answerDenominazioneImmagineExercise = view.findViewById(R.id.answerDenominazioneImmagineExercise);
        correctRewardDenominazioneImmagine = view.findViewById(R.id.correctRewardDenominazioneImmagine);
        wrongRewardDenominazioneImmagine = view.findViewById(R.id.wrongRewardDenominazioneImmagine);
        buttonConfirmDenominazioneImmagineExercise = view.findViewById(R.id.buttonConfirmDenominazioneImmagineExercise);
        openFileDenominazioneImmagineExercise = view.findViewById(R.id.openFileDenominazioneImmagineExercise);

        // creation exercise sequenza parole
        cardViewCreateSequenzaParoleExercise = view.findViewById(R.id.cardViewCreateSequenzaParoleExercise);
        buttonAudioSequenzaParole = view.findViewById(R.id.buttonAudioSequenzaParole);
        startAudioSequenzaParole = view.findViewById(R.id.startAudioSequenzaParole);
        stopAudioSequenzaParole = view.findViewById(R.id.stopAudioSequenzaParole);
        firstWord = view.findViewById(R.id.firstWord);
        secondWord = view.findViewById(R.id.secondWord);
        thirdWord = view.findViewById(R.id.thirdWord);
        correctRewardSequenzaParole = view.findViewById(R.id.correctRewardSequenzaParole);
        wrongRewardSequenzaParole = view.findViewById(R.id.wrongRewardSequenzaParole);
        buttonConfirmSequenzaParoleExercise = view.findViewById(R.id.buttonConfirmSequenzaParoleExercise);

        // creation exercise coppia immagine
        cardViewCreateCoppiaImmagineExercise = view.findViewById(R.id.cardViewCreateCoppiaImmagineExercise);
        buttonAudioCoppiaImmagine = view.findViewById(R.id.buttonAudioCoppiaImmagine);
        startAudioCoppiaImmagine = view.findViewById(R.id.startAudioCoppiaImmagine);
        stopAudioCoppiaImmagine = view.findViewById(R.id.stopAudioCoppiaImmagine);
        openFileCoppiaImmagineCorrect = view.findViewById(R.id.openFileCoppiaImmagineCorrect);
        openFileCoppiaImmagineWrong = view.findViewById(R.id.openFileCoppiaImmagineWrong);
        correctAnswerCoppiaImmagine = view.findViewById(R.id.correctAnswerCoppiaImmagine);
        wrongAnswerCoppiaImmagine = view.findViewById(R.id.wrongAnswerCoppiaImmagine);
        buttonConfirmCoppiaImmagineExercise = view.findViewById(R.id.buttonConfirmCoppiaImmagineExercise);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        TemplateEsercizioDAO templateEsercizioDAO = new TemplateEsercizioDAO();
        templateEsercizioDAO.getAll().thenAccept(result -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    listTemplateExercise = result;
                    setListTemplateExercise();
                    setTemplate();
                    textViewTitleExercise.setText(textViewTitleExercise.getText().toString() + " " + countExercise);

                    if (countExercise == 3)
                        countExercise = 1;
                    else countExercise++;

                    addExercise.setVisibility(View.GONE);
                    typeExercise.setVisibility(View.VISIBLE);

                    pickTemplateExercise.setVisibility(View.GONE);

                    stopAudioDenominazioneImmagine.setVisibility(View.GONE);
                    stopAudioSequenzaParole.setVisibility(View.GONE);
                    stopAudioCoppiaImmagine.setVisibility(View.GONE);

                    startAudioDenominazioneImmagine.setVisibility(View.GONE);
                    startAudioSequenzaParole.setVisibility(View.GONE);
                    startAudioCoppiaImmagine.setVisibility(View.GONE);

                    // set onclick button file picker image
                    openFileDenominazioneImmagineExercise.setOnClickListener(v -> startFilePicker(PICK_FILE_1));
                    openFileCoppiaImmagineCorrect.setOnClickListener(v -> startFilePicker(PICK_FILE_2));
                    openFileCoppiaImmagineWrong.setOnClickListener(v -> startFilePicker(PICK_FILE_3));

                    // set onclik button confirm exercise
                    buttonConfirmDenominazioneImmagineExercise.setOnClickListener(v -> createDenominazioneImmagineExercise());
                    buttonConfirmSequenzaParoleExercise.setOnClickListener(v -> createSequenzaParoleExercise());
                    buttonConfirmCoppiaImmagineExercise.setOnClickListener(v -> createCoppiaImmagineExercise());

                    // set onclick microphone
                    this.audioRecorder = audioRecorder();
                    setScaleAnimation();

                    buttonAudioDenominazioneImmagine.setOnClickListener(v ->
                            startFirstRegistration(buttonAudioDenominazioneImmagine, stopAudioDenominazioneImmagine, startAudioDenominazioneImmagine));

                    stopAudioDenominazioneImmagine.setOnClickListener(v ->
                            stopRegistration(buttonAudioDenominazioneImmagine, stopAudioDenominazioneImmagine, startAudioDenominazioneImmagine, 1).
                                    thenAccept(value -> audioDenominazioneImmagineExercise = value));


                    buttonAudioSequenzaParole.setOnClickListener(v ->
                            startFirstRegistration(buttonAudioSequenzaParole, stopAudioSequenzaParole, startAudioSequenzaParole));

                    stopAudioSequenzaParole.setOnClickListener(v ->
                            stopRegistration(buttonAudioSequenzaParole, stopAudioSequenzaParole, startAudioSequenzaParole, 2).
                                    thenAccept(value -> audioSequenzaParoleExercise = value));


                    buttonAudioCoppiaImmagine.setOnClickListener(v ->
                            startFirstRegistration(buttonAudioCoppiaImmagine, stopAudioCoppiaImmagine, startAudioCoppiaImmagine));

                    stopAudioCoppiaImmagine.setOnClickListener(v ->
                            stopRegistration(buttonAudioCoppiaImmagine, stopAudioCoppiaImmagine, startAudioCoppiaImmagine, 3).
                                    thenAccept(value -> audioCoppiaImmagineExercise = value));

                    // button previous template denominazione immagine exercise
                    buttonPreviousTemplateDenominazioneImmagine.setOnClickListener(v -> {
                        templateDenominazioneImmagine--;
                        if (templateDenominazioneImmagine < 0)
                            templateDenominazioneImmagine = maxSizeTemplateDenominazioneImmagine;
                        showTemplateDenominazioneImmagine();
                    });

                    // button next template denominazione immagine exercise
                    buttonNextTemplateDenominazioneImmagine.setOnClickListener(v -> {
                        templateDenominazioneImmagine++;
                        if (templateDenominazioneImmagine > maxSizeTemplateDenominazioneImmagine)
                            templateDenominazioneImmagine = 0;
                        showTemplateDenominazioneImmagine();
                    });

                    // button previous template sequenza parole exercise
                    buttonPreviousTemplateSequenzaParole.setOnClickListener(v -> {
                        templateSequenzaParole--;
                        if (templateSequenzaParole < 0)
                            templateSequenzaParole = maxSizeTemplateSequenzaParole;
                        showTemplateSequenzaParole();
                    });

                    // button next template sequenza parole exercise
                    buttonNextTemplateSequenzaParole.setOnClickListener(v -> {
                        templateSequenzaParole++;
                        if (templateSequenzaParole > maxSizeTemplateSequenzaParole)
                            templateSequenzaParole = 0;
                        showTemplateSequenzaParole();
                    });

                    // button previous template coppia immagine exercise
                    buttonPreviousTemplateCoppiaImmagine.setOnClickListener(v -> {
                        templateCoppiaImmagini--;
                        if (templateCoppiaImmagini < 0)
                            templateCoppiaImmagini = maxSizeTemplateCoppiaImmagini;
                        showTemplateCoppiaImmagini();
                    });

                    // button next template coppia immagine exercise
                    buttonNextTemplateCoppiaImmagine.setOnClickListener(v -> {
                        templateCoppiaImmagini++;
                        if (templateCoppiaImmagini > maxSizeTemplateCoppiaImmagini)
                            templateCoppiaImmagini = 0;
                        showTemplateCoppiaImmagini();
                    });

                    // show hide exercise
                    titleExercise.setOnClickListener(v -> {
                        if (addExercise.getVisibility() == View.VISIBLE) {
                            addExercise.setVisibility(View.GONE);
                            arrowDown.setRotation(0);
                        } else {
                            addExercise.setVisibility(View.VISIBLE);
                            arrowDown.setRotation(180);
                        }
                    });

                    buttonDenominazioneImmagineExercise.setOnClickListener(v -> pickTypeExercise(1));
                    buttonSequenzaParoleExercise.setOnClickListener(v -> pickTypeExercise(2));
                    buttonCoppiaImmagineExercise.setOnClickListener(v -> pickTypeExercise(3));
                });
            }
        });
    }

    private void setListTemplateExercise() {
        for (Esercizio esercizio : listTemplateExercise) {
            if (esercizio instanceof TemplateEsercizioCoppiaImmagini) {
                listTemplateCoppiaImmagine.add((TemplateEsercizioCoppiaImmagini) esercizio);
            } else if (esercizio instanceof TemplateEsercizioDenominazioneImmagini) {
                listTemplateDenominazioneImmagine.add((TemplateEsercizioDenominazioneImmagini) esercizio);
            } else {
                listTemplateSequenzaParole.add((TemplateEsercizioSequenzaParole) esercizio);
            }
        }
    }

    private void startFilePicker(int requestCode) {

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*"});

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            ComandiFirebaseStorage comandiFirebaseStorage = new ComandiFirebaseStorage();
            comandiFirebaseStorage.uploadFileAndGetLink(uri, "TEST/THERAPY_TEST").thenAccept(link -> {

                switch (requestCode) {
                    case PICK_FILE_1:
                        imageDenominazioneImmagineExercise = link;
                        Glide.with(getContext()).load(uri).into(openFileDenominazioneImmagineExercise);
                        break;
                    case PICK_FILE_2:
                        correctImageCoppiaImmagine = link;
                        Glide.with(getContext()).load(uri).into(openFileCoppiaImmagineCorrect);
                        break;
                    case PICK_FILE_3:
                        wrongImageCoppiaImmagine = link;
                        Glide.with(getContext()).load(uri).into(openFileCoppiaImmagineWrong);
                        break;
                    default:
                        break;
                }
            });
        }
    }

    private void setTemplate() {
        maxSizeTemplateCoppiaImmagini = listTemplateCoppiaImmagine.size() -1 ;
        maxSizeTemplateDenominazioneImmagine = listTemplateDenominazioneImmagine.size() -1 ;
        maxSizeTemplateSequenzaParole = listTemplateSequenzaParole.size() -1 ;
    }

    private void pickTypeExercise(int exercise) {

        typeExercise.setVisibility(View.GONE);
        pickTemplateExercise.setVisibility(View.VISIBLE);

        if(exercise == 1) {
            buttonTemplateExercise.setOnClickListener(v -> showTemplateExercise(1));
            buttonCreateExercise.setOnClickListener(v -> showCreationExercise(1));
        } else if(exercise == 2) {
            buttonTemplateExercise.setOnClickListener(v -> showTemplateExercise(2));
            buttonCreateExercise.setOnClickListener(v -> showCreationExercise(2));
        } else if(exercise == 3) {
            buttonTemplateExercise.setOnClickListener(v -> showTemplateExercise(3));
            buttonCreateExercise.setOnClickListener(v -> showCreationExercise(3));
        }

    }

    private void showCreationExercise(int exercise){

        pickTemplateExercise.setVisibility(View.GONE);
        creationExercise.setVisibility(View.VISIBLE);

        if(exercise == 1){
            cardViewCreateDenominazioneImmagineExercise.setVisibility(View.VISIBLE);
            cardViewCreateSequenzaParoleExercise.setVisibility(View.GONE);
            cardViewCreateCoppiaImmagineExercise.setVisibility(View.GONE);
        } else if(exercise == 2){
            cardViewCreateDenominazioneImmagineExercise.setVisibility(View.GONE);
            cardViewCreateSequenzaParoleExercise.setVisibility(View.VISIBLE);
            cardViewCreateCoppiaImmagineExercise.setVisibility(View.GONE);
        } else if(exercise == 3){
            cardViewCreateDenominazioneImmagineExercise.setVisibility(View.GONE);
            cardViewCreateSequenzaParoleExercise.setVisibility(View.GONE);
            cardViewCreateCoppiaImmagineExercise.setVisibility(View.VISIBLE);
        }
    }

    private void showTemplateExercise(int exercise) {

        pickTemplateExercise.setVisibility(View.GONE);
        templateExercise.setVisibility(View.VISIBLE);

        switch (exercise) {
            case 1:
                cardViewTemplateDenominazioneImmagineExercise.setVisibility(View.VISIBLE);
                cardViewTemplateSequenzaParoleExercise.setVisibility(View.GONE);
                cardViewTemplateCoppiaImmagineExercise.setVisibility(View.GONE);
                showTemplateDenominazioneImmagine();
                break;
            case 2:
                cardViewTemplateDenominazioneImmagineExercise.setVisibility(View.GONE);
                cardViewTemplateSequenzaParoleExercise.setVisibility(View.VISIBLE);
                cardViewTemplateCoppiaImmagineExercise.setVisibility(View.GONE);
                showTemplateSequenzaParole();
                break;
            case 3:
                cardViewTemplateDenominazioneImmagineExercise.setVisibility(View.GONE);
                cardViewTemplateSequenzaParoleExercise.setVisibility(View.GONE);
                cardViewTemplateCoppiaImmagineExercise.setVisibility(View.VISIBLE);
                showTemplateCoppiaImmagini();
                break;
        }
    }

    private void showTemplateDenominazioneImmagine() {

        TemplateEsercizioDenominazioneImmagini templateEsercizioDenominazioneImmagini =
                listTemplateDenominazioneImmagine.get(templateDenominazioneImmagine);

        String idExercise = templateEsercizioDenominazioneImmagini.getIdEsercizio();
        String imageExercise = templateEsercizioDenominazioneImmagini.getImmagineEsercizioDenominazioneImmagini();
        String wordExercise = templateEsercizioDenominazioneImmagini.getParolaEsercizioDenominazioneImmagini();
        String aidAudio = templateEsercizioDenominazioneImmagini.getAudioAiuto();
        String referenceTemplate = "0";

        int correctReward = templateEsercizioDenominazioneImmagini.getRicompensaCorretto();
        int wrongReward = templateEsercizioDenominazioneImmagini.getRicompensaErrato();

        exerciseToCreate = new EsercizioDenominazioneImmagini(idExercise, correctReward, wrongReward,
                imageExercise, wordExercise, aidAudio, referenceTemplate);

        wordsTemplateDenominazioneImmagine.setText(wordExercise);
    }

    private void showTemplateSequenzaParole() {

        TemplateEsercizioSequenzaParole templateEsercizioSequenzaParole =
                listTemplateSequenzaParole.get(templateSequenzaParole);

        String audioExercise = templateEsercizioSequenzaParole.getAudioEsercizioSequenzaParole();
        String firstWord = templateEsercizioSequenzaParole.getParolaDaIndovinare1();
        String secondWord = templateEsercizioSequenzaParole.getParolaDaIndovinare2();
        String thirdWord = templateEsercizioSequenzaParole.getParolaDaIndovinare3();
        String idExercise = templateEsercizioSequenzaParole.getIdEsercizio();
        String referenceTemplate = "0";

        int correctReward = templateEsercizioSequenzaParole.getRicompensaCorretto();
        int wrongReward = templateEsercizioSequenzaParole.getRicompensaErrato();

        exerciseToCreate = new EsercizioSequenzaParole(idExercise, correctReward, wrongReward,
                audioExercise, firstWord, secondWord, thirdWord, referenceTemplate);

        firstWordTemplateSequenzaParole.setText(firstWord);
        secondWordTemplateSequenzaParole.setText(secondWord);
        thirdWordTemplateSequenzaParole.setText(thirdWord);
    }

    private void showTemplateCoppiaImmagini() {

        TemplateEsercizioCoppiaImmagini templateEsercizioCoppiaImmagini =
                listTemplateCoppiaImmagine.get(templateCoppiaImmagini);

        String idExercise = templateEsercizioCoppiaImmagini.getIdEsercizio();
        String audioExercise =  templateEsercizioCoppiaImmagini.getAudioEsercizioCoppiaImmagini();
        String correctImage = templateEsercizioCoppiaImmagini.getImmagineCorrettaEsercizioCoppiaImmagini();
        String wrongImage = templateEsercizioCoppiaImmagini.getImmagineErrataEsercizioCoppiaImmagini();
        String referenceTemplate = "0";

        int correctReward = templateEsercizioCoppiaImmagini.getRicompensaCorretto();
        int wrongReward = templateEsercizioCoppiaImmagini.getRicompensaErrato();

        exerciseToCreate = new EsercizioCoppiaImmagini(idExercise, correctReward, wrongReward,
                audioExercise, correctImage, wrongImage, referenceTemplate);

        Glide.with(this).load(correctImage).into(templateCoppiaImmagine);
    }

    private void startFirstRegistration(ImageButton buttonStartRegistration, View stopMicrophone, View startMicrophone) {

        if (requestPermissions()) {

            startRegistration(buttonStartRegistration, stopMicrophone, startMicrophone);
            stopMicrophone.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), getContext().getString(R.string.startedRecording), Toast.LENGTH_SHORT).show();
        }
    }

    public CompletableFuture<String> stopRegistration(ImageButton buttonStartRegistration, View stopMicrophone,
                                                      View startMicrophone, int exercise) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        startMicrophone.clearAnimation();
        buttonStartRegistration.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_background_verde));
        stopMicrophone.setVisibility(View.GONE);
        startMicrophone.setVisibility(View.GONE);

        audioRecorder.stopRecording();
        Toast.makeText(getContext(), getContext().getString(R.string.stopedRecording), Toast.LENGTH_SHORT).show();

        File convertedFile = new File(getContext().getFilesDir(), "convertedFile.mp3");
        AudioConverter.convertiAudio(audioRecorder.getAudioRecorder(), convertedFile);
        ComandiFirebaseStorage comandiFirebaseStorage = new ComandiFirebaseStorage();
        AtomicReference<String> atomicReference = new AtomicReference<>();

        String actualDirectory = "";

        switch(exercise) {
            case 1:
                actualDirectory = ComandiFirebaseStorage.AUDIO_DENOMINAZIONE_IMMAGINE_EXERCISE;
                break;
            case 2:
                actualDirectory = ComandiFirebaseStorage.AUDIO_SEQUENZA_PAROLE_EXERCISE;
                break;
            case 3:
                actualDirectory = ComandiFirebaseStorage.AUDIO_COPPIA_IMMAGINI_EXERCISE;
                break;
        }

        comandiFirebaseStorage.uploadFileAndGetLink(Uri.fromFile(convertedFile), actualDirectory)
                .thenAccept(value ->{
                    atomicReference.set(value);
                })
                .thenRun(() -> {
                    completableFuture.complete(atomicReference.get());
                });

        return completableFuture;
    }

    private void startRegistration(ImageButton buttonStartRegistration, View stopMicrophone, View startMicrophone) {

        buttonStartRegistration.setBackground(null);
        startMicrophone.setVisibility(View.VISIBLE);
        startMicrophone.startAnimation(scaleAnimation);
        stopMicrophone.setVisibility(View.VISIBLE);

        audioRecorder.startRecording();
    }

    EsercizioRealizzabile getExercise() {
        return exerciseToCreate;
    }

    private AudioRecorder audioRecorder() {

        File appDirectory = requireActivity().getFilesDir();
        File audioRegistration = new File(appDirectory, "audioRegistrato");

        return new AudioRecorder(audioRegistration);
    }

    private void setScaleAnimation() {
        scaleAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
    }

    private boolean requestPermissions() {
        if (!checkPermissions(requireActivity())) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.RECORD_AUDIO)) {
                setDialogPermissions();
            } else {
                requestPermissionsLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
            return false;
        } else {
            return true;
        }
    }

    private void setDialogPermissions() {
        PermessiDialog permessiDialog = new PermessiDialog(getContext(), getString(R.string.permissionDeniedDescription));
        permessiDialog.show();
        permessiDialog.setOnConfirmButtonClickListener(() -> requestPermissionsLauncher.launch(Manifest.permission.RECORD_AUDIO));
        permessiDialog.setOnCancelButtonClickListener(() -> navigateTo(R.id.action_esercizioDenominazioneImmagineFragment_to_scenarioFragment));
    }

    private boolean checkPermissions(Activity activity) {
        int audioPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        return audioPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void showErrorDialog(){
        InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.compilaPrimaTutto), getString(R.string.tastoRiprova));
        infoDialog.setOnConfirmButtonClickListener(null);
        infoDialog.show();
    }

    private final ActivityResultLauncher<String> requestPermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    this.audioRecorder = audioRecorder();
                } else {
                    InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.permissionDeniedInstructions), getString(R.string.infoOk));
                    infoDialog.show();
                    infoDialog.setOnConfirmButtonClickListener(() -> navigateTo(R.id.action_esercizioDenominazioneImmagineFragment_to_scenarioFragment));
                }
            });


    private void createDenominazioneImmagineExercise() {

        if (imageDenominazioneImmagineExercise==null || audioDenominazioneImmagineExercise==null ||
                correctRewardDenominazioneImmagine.getText().toString().isEmpty() ||
                wrongRewardDenominazioneImmagine.getText().toString().isEmpty() ||
                answerDenominazioneImmagineExercise.getText().toString().isEmpty()) {

            showErrorDialog();

        }else {
            exerciseToCreate = new EsercizioDenominazioneImmagini(Integer.parseInt(correctRewardDenominazioneImmagine.
                    getText().toString()), Integer.parseInt(wrongRewardDenominazioneImmagine.
                    getText().toString()), audioDenominazioneImmagineExercise, imageDenominazioneImmagineExercise, answerDenominazioneImmagineExercise.
                    getText().toString());

            Log.d("exerciseToCreate", exerciseToCreate.toString());
            addExercise.setVisibility(View.GONE);
            arrowDown.setRotation(0);
            Toast.makeText(getContext(), getString(R.string.esercizio) + " " + getString(R.string.confermato), Toast.LENGTH_SHORT).show();
        }
    }

    private void createSequenzaParoleExercise() {

        if (audioSequenzaParoleExercise==null || correctRewardSequenzaParole.getText().toString().isEmpty() ||
                wrongRewardSequenzaParole.getText().toString().isEmpty() ||
                firstWord.getText().toString().isEmpty() ||
                secondWord.getText().toString().isEmpty() ||
                thirdWord.getText().toString().isEmpty()) {

            showErrorDialog();

        }else {
            exerciseToCreate = new EsercizioSequenzaParole(Integer.parseInt(correctRewardSequenzaParole.
                    getText().toString()), Integer.parseInt(wrongRewardSequenzaParole.
                    getText().toString()), audioSequenzaParoleExercise, firstWord.
                    getText().toString(), secondWord.
                    getText().toString(), thirdWord.
                    getText().toString());

            Log.d("exerciseToCreate", exerciseToCreate.toString());
            addExercise.setVisibility(View.GONE);
            arrowDown.setRotation(0);
            Toast.makeText(getContext(), getString(R.string.esercizio) + " " + getString(R.string.confermato), Toast.LENGTH_SHORT).show();
        }
    }

    private void createCoppiaImmagineExercise() {
        if (correctImageCoppiaImmagine == null || audioCoppiaImmagineExercise == null || audioCoppiaImmagineExercise == null ||
                correctAnswerCoppiaImmagine.getText().toString().isEmpty() ||
                wrongAnswerCoppiaImmagine.getText().toString().isEmpty()){

            Log.d("exerciseToCreate", (correctImageCoppiaImmagine == null) + " "
                    + (wrongImageCoppiaImmagine == null) + " " + (audioCoppiaImmagineExercise == null) + " "
                    + (correctAnswerCoppiaImmagine.getText().toString().isEmpty()) + " "
                    + (wrongAnswerCoppiaImmagine.getText().toString().isEmpty()));

            showErrorDialog();

        }else {
            exerciseToCreate = new EsercizioCoppiaImmagini(Integer.parseInt(correctAnswerCoppiaImmagine.
                    getText().toString()), Integer.parseInt(wrongAnswerCoppiaImmagine.
                    getText().toString()), audioCoppiaImmagineExercise, correctImageCoppiaImmagine, wrongImageCoppiaImmagine);

            Log.d("exerciseToCreate", exerciseToCreate.toString());
            addExercise.setVisibility(View.GONE);
            arrowDown.setRotation(0);
            Toast.makeText(getContext(), getString(R.string.esercizio) + " " + getString(R.string.confermato), Toast.LENGTH_SHORT).show();
        }
    }


}