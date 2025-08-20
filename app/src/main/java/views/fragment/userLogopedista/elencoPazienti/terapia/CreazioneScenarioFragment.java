package views.fragment.userLogopedista.elencoPazienti.terapia;


import it.uniba.dib.pronuntiapp.R;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.time.ZoneId;
import java.util.List;
import java.time.LocalDate;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import views.dialog.InfoDialog;
import views.fragment.AbstractNavigazioneFragment;

import models.domain.scenariGioco.ScenarioGioco;
import models.domain.scenariGioco.TemplateScenarioGioco;
import models.domain.esercizi.EsercizioRealizzabile;
import models.domain.profili.Paziente;
import models.database.ComandiFirebaseStorage;
import models.database.TemplateScenarioGiocoDAO;


public class CreazioneScenarioFragment extends AbstractNavigazioneFragment {

    private TextInputEditText dateScenery;

    private TextInputEditText finalReward;

    private Button buttonUseTemplate;

    private Button buttonCreateScenery;

    private Button buttonPickBackground;

    private Button buttonPickFirstImage;

    private Button buttonPickSecondImage;

    private Button buttonPickThirdImage;

    private ImageButton buttonPreviousScenery;

    private ImageButton buttonNextScenery;

    private Button buttonSaveScenery;

    private ImageView firstImage;

    private ImageView secondImage;

    private ImageView thirdImage;

    private static final int PICK_FILE_1 = 1;

    private static final int PICK_FILE_2 = 2;

    private static final int PICK_FILE_3 = 3;

    private static final int PICK_FILE_4 = 4;

    private int currentIndexTemplateScenery = 0;

    private int indexTherapy;

    private int sizeTemplateScenery;

    private int reward;

    private String firstImageUri;

    private String secondImageUri;

    private String thirdImageUri;

    private String backgroundImageUri;

    private String startDate;

    private String idPaziente;

    private List<EsercizioRealizzabile> listEsercizioRealizzabile;

    private List<TemplateScenarioGioco> listTemplateScenery;

    private LogopedistaViewsModels logopedistaViewsModels;

    private LinearLayout pickTemplateOrCreateScenery;

    private LinearLayout createScenery;

    private ConstraintLayout creationImageScenery;

    private boolean pickScenery = false;

    private LocalDate startDateTherapy;

    private LocalDate endDateTherapy;

    private ScenarioGioco scenarioGioco;

    private SalvataggioScenario salvataggioScenario;

    private Bundle bundle;


    public CreazioneScenarioFragment(SalvataggioScenario mCallback) {
        this.salvataggioScenario = mCallback;
    }

    public CreazioneScenarioFragment() {
        super();
        salvataggioScenario = null;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_creazione_scenario, container, false);
        logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);
        view.findViewById(R.id.toolBar).setVisibility(View.GONE);

        bundle = getArguments();

        if(bundle!=null) {
            if (salvataggioScenario == null){
                setToolBar(view, getString(R.string.nuovoScenario));
                idPaziente = bundle.getString("idPaziente");
                indexTherapy = bundle.getInt("indexTherapy");

                view.findViewById(R.id.toolBar).setVisibility(View.VISIBLE);
                Paziente paziente = logopedistaViewsModels.getPazienteById(idPaziente);
                Log.d("CreazioneScenarioFragment", "onCreateView: "+paziente.getTerapie().get(indexTherapy));

                startDateTherapy = paziente.getTerapie().get(indexTherapy).getDataInizioTerapia();
                endDateTherapy = paziente.getTerapie().get(indexTherapy).getDataFineTerapia();
            }else {
                startDateTherapy = LocalDate.parse(bundle.getString("startDate"));
                endDateTherapy = LocalDate.parse(bundle.getString("endDate"));
            }
        }

        listEsercizioRealizzabile = new ArrayList<>();

        dateScenery = view.findViewById(R.id.dateScenery);
        finalReward = view.findViewById(R.id.finalReward);

        pickTemplateOrCreateScenery = view.findViewById(R.id.pickTemplateOrCreateScenery);

        buttonUseTemplate = view.findViewById(R.id.buttonUseTemplate);
        buttonCreateScenery = view.findViewById(R.id.buttonCreateScenery);

        createScenery = view.findViewById(R.id.createScenery);

        buttonPickBackground = view.findViewById(R.id.buttonPickBackground);

        buttonPickFirstImage = view.findViewById(R.id.buttonPickFirstImage);
        buttonPickSecondImage = view.findViewById(R.id.buttonPickSecondImage);
        buttonPickThirdImage = view.findViewById(R.id.buttonPickThirdImage);

        creationImageScenery = view.findViewById(R.id.creationImageScenery);

        buttonNextScenery = view.findViewById(R.id.buttonNextScenery);
        buttonPreviousScenery = view.findViewById(R.id.buttonPreviousScenery);

        firstImage = view.findViewById(R.id.firstImage);
        secondImage = view.findViewById(R.id.secondImage);
        thirdImage = view.findViewById(R.id.thirdImage);

        buttonSaveScenery = view.findViewById(R.id.buttonSaveScenery);

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        pickTemplateOrCreateScenery.setVisibility(View.VISIBLE);
        createScenery.setVisibility(View.GONE);

        buttonNextScenery.setVisibility(View.GONE);
        buttonPreviousScenery.setVisibility(View.GONE);

        buttonPickFirstImage.setVisibility(View.GONE);
        buttonPickSecondImage.setVisibility(View.GONE);
        buttonPickThirdImage.setVisibility(View.GONE);

        buttonPickFirstImage.setOnClickListener(v -> startFilePicker(PICK_FILE_1));
        buttonPickSecondImage.setOnClickListener(v -> startFilePicker(PICK_FILE_2));
        buttonPickThirdImage.setOnClickListener(v -> startFilePicker(PICK_FILE_3));

        creationImageScenery.setVisibility(View.GONE);

        buttonPickBackground.setOnClickListener(v -> {
                    startFilePicker(PICK_FILE_4);
                    creationImageScenery.setVisibility(View.VISIBLE);
                    buttonPickFirstImage.setVisibility(View.VISIBLE);
                    buttonPickSecondImage.setVisibility(View.VISIBLE);
                    buttonPickThirdImage.setVisibility(View.VISIBLE);
                }
        );

        dateScenery.setOnClickListener(v -> showDialogDatePicker(getContext(), dateScenery));

        buttonSaveScenery.setOnClickListener(v -> saveScenery());

        buttonUseTemplate.setOnClickListener(v -> useTemplate());
        buttonCreateScenery.setOnClickListener(v -> createSceneryFromStart());

        buttonPreviousScenery.setOnClickListener(v -> previousTemplateScenery());
        buttonNextScenery.setOnClickListener(v -> nextTemplateScenery());

        logopedistaViewsModels.getLogopedistaLiveData().observe(getViewLifecycleOwner(),logopedista -> {
            TemplateScenarioGiocoDAO templateScenarioGiocoDAO = new TemplateScenarioGiocoDAO();
            templateScenarioGiocoDAO.getAll().thenAccept(result -> {
                listTemplateScenery = result;
                sizeTemplateScenery = result.size();
            });
        });
    }

    public void showDialogDatePicker(Context context, TextView textField) {

        LocalDate localDate = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            String date = LocalDate.of(year, month + 1, dayOfMonth).toString();
            textField.setText(date);
        }, localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());

        //set min e max date
        ZoneId zoneId = ZoneId.systemDefault();
        long minDateMillis = startDateTherapy.atStartOfDay(zoneId).toInstant().toEpochMilli();
        long maxDateMillis = endDateTherapy.atStartOfDay(zoneId).toInstant().toEpochMilli();

        datePickerDialog.getDatePicker().setMinDate(minDateMillis);
        datePickerDialog.getDatePicker().setMaxDate(maxDateMillis);
        datePickerDialog.show();
    }

    private void showErrorDialog(){
        InfoDialog infoDialog = new InfoDialog(getContext(), getString(R.string.compilaPrimaTutto), getString(R.string.tastoRiprova));
        infoDialog.setOnConfirmButtonClickListener(null);
        infoDialog.show();
    }

    private void saveScenery(){

        //pick degli esercizi dai fragment figli
        EsercizioRealizzabile firstExercise = ((CreazioneEsercizioFragment) getChildFragmentManager().
                findFragmentById(R.id.containerViewFirstExercise)).getExercise();

        EsercizioRealizzabile secondExercise = ((CreazioneEsercizioFragment) getChildFragmentManager().
                findFragmentById(R.id.containerViewSecondExercise)).getExercise();

        EsercizioRealizzabile thirdExercise = ((CreazioneEsercizioFragment) getChildFragmentManager().
                findFragmentById(R.id.containerViewThirdExercise)).getExercise();

        if (pickScenery) {
            firstImageUri = listTemplateScenery.get(currentIndexTemplateScenery).getImmagine1();
            secondImageUri = listTemplateScenery.get(currentIndexTemplateScenery).getImmagine2();
            thirdImageUri = listTemplateScenery.get(currentIndexTemplateScenery).getImmagine3();
            backgroundImageUri = listTemplateScenery.get(currentIndexTemplateScenery).getSfondoImmagine();
        }

        if (dateScenery.getText().toString().isEmpty() ||
                finalReward.getText().toString().isEmpty() ||
                firstImageUri == null || secondImageUri == null ||
                thirdImageUri == null || backgroundImageUri == null ||
                firstExercise == null || secondExercise == null || thirdExercise == null){

            showErrorDialog();
        }
        else {
            startDate = dateScenery.getText().toString();
            reward = Integer.parseInt(finalReward.getText().toString());

            listEsercizioRealizzabile.add(firstExercise);
            listEsercizioRealizzabile.add(secondExercise);
            listEsercizioRealizzabile.add(thirdExercise);
            Log.d("CreazioneScenarioFragment", "saveScenario: " + listEsercizioRealizzabile.toString());

            String referenceTemplate = "0";

            scenarioGioco = new ScenarioGioco(backgroundImageUri, firstImageUri, secondImageUri, thirdImageUri,
                    LocalDate.parse(startDate), reward, listEsercizioRealizzabile, referenceTemplate);

            if(salvataggioScenario == null){
                Paziente paziente = logopedistaViewsModels.getPazienteById(idPaziente);
                paziente.getTerapie().get(indexTherapy).addListScenarioGioco(scenarioGioco);
                logopedistaViewsModels.aggiornaLogopedistaRemoto();
                navigateTo(R.id.action_creazioneScenarioFragment_to_schedaPazienteFragment, bundle);
            }
            else {
                salvataggioScenario.saveScenery(scenarioGioco);
                getParentFragmentManager().beginTransaction().remove(this).commit();
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

    private void previousTemplateScenery(){
        if (currentIndexTemplateScenery -1 >= 0){
            currentIndexTemplateScenery -= 1;
        }else{
            currentIndexTemplateScenery = sizeTemplateScenery-1;
        }
        editMakingSceneryTemplate();
    }

    private void nextTemplateScenery(){
        if (currentIndexTemplateScenery +1 < sizeTemplateScenery){
            currentIndexTemplateScenery += 1;
        }else{
            currentIndexTemplateScenery = 0;
        }
        editMakingSceneryTemplate();
    }

    private void createSceneryFromStart(){
        pickTemplateOrCreateScenery.setVisibility(View.GONE);
        createScenery.setVisibility(View.VISIBLE);
        buttonNextScenery.setVisibility(View.GONE);
        buttonPreviousScenery.setVisibility(View.GONE);
    }

    private void useTemplate(){
        pickScenery = true;
        pickTemplateOrCreateScenery.setVisibility(View.GONE);
        createScenery.setVisibility(View.GONE);
        buttonNextScenery.setVisibility(View.VISIBLE);
        buttonPreviousScenery.setVisibility(View.VISIBLE);
        creationImageScenery.setVisibility(View.VISIBLE);
        editMakingSceneryTemplate();
    }


    private void editMakingSceneryTemplate(){

        String firstImageExercise = listTemplateScenery.get(currentIndexTemplateScenery).getImmagine1();
        String secondImageExercise = listTemplateScenery.get(currentIndexTemplateScenery).getImmagine2();
        String thirdImageExercise = listTemplateScenery.get(currentIndexTemplateScenery).getImmagine3();
        String backgroundImageExercise = listTemplateScenery.get(currentIndexTemplateScenery).getSfondoImmagine();

        Glide.with(getContext()).load(firstImageExercise).into(firstImage);
        Glide.with(getContext()).load(secondImageExercise).into(secondImage);
        Glide.with(getContext()).load(thirdImageExercise).into(thirdImage);

        Glide.with(getContext())
                .asBitmap()
                .load(backgroundImageExercise)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        creationImageScenery.setBackground(new BitmapDrawable(getResources(), resource));
                    }

                    // metodo chiamato quando l'immagine non è più necessaria
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {}
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            ComandiFirebaseStorage comandiFirebaseStorage = new ComandiFirebaseStorage();
            comandiFirebaseStorage.uploadFileAndGetLink(uri, ComandiFirebaseStorage.SCENARI_GIOCO).thenAccept(link -> {

                switch (requestCode) {
                    case PICK_FILE_1:
                        firstImageUri = link;
                        Glide.with(getContext()).load(uri).into(firstImage);
                        break;
                    case PICK_FILE_2:
                        secondImageUri = link;
                        Glide.with(getContext()).load(uri).into(secondImage);
                        break;
                    case PICK_FILE_3:
                        thirdImageUri = link;
                        Glide.with(getContext()).load(uri).into(thirdImage);
                        break;
                    case PICK_FILE_4:
                        backgroundImageUri = link;
                        Glide.with(getContext())
                                .asBitmap()
                                .load(uri)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        creationImageScenery.setBackground(new BitmapDrawable(getResources(), resource));
                                    }

                                    // metodo chiamato quando l'immagine non è più necessaria
                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                        break;
                    default:
                        break;
                }
            });
        }
    }

}
