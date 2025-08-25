package views.fragment.userLogopedista.elencoPazienti.terapia;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

import models.domain.terapie.Terapia;
import models.domain.scenariGioco.ScenarioGioco;

import views.fragment.DataCustomizzata;
import views.dialog.InfoDialog;
import views.fragment.AbstractNavigationFragment;

import java.util.List;
import java.time.LocalDate;

import models.domain.profili.Paziente;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import androidx.lifecycle.ViewModelProvider;

import android.view.ViewGroup;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.util.Log;


public class CreazioneTerapiaFragment extends AbstractNavigationFragment implements SalvataggioScenario {

    private boolean isFirstScenery = true;

    private TextInputEditText endDate;

    private TextInputEditText startDate;

    private Button saveTherapy;

    private Terapia therapy;

    private Button addScenery;

    private Bundle bundle;

    private String idPaziente;

    private LogopedistaViewsModels logopedistaViewsModels;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_creazione_terapia, container, false);
        logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);

        addScenery = view.findViewById(R.id.addScenery);
        saveTherapy = view.findViewById(R.id.saveTherapy);

        bundle = getArguments();
        idPaziente = bundle.getString("idPaziente");
        saveTherapy.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        logopedistaViewsModels.getLogopedistaLiveData().observe(getViewLifecycleOwner(), Void -> {
            Log.d("idPaziente",idPaziente);
            Paziente paziente = logopedistaViewsModels.getPazienteById(idPaziente);
            String namePaziente = paziente.getNome();
            String surnamePaziente = paziente.getCognome();

            setToolBar(view,getString(R.string.creaTerapia) + " " + namePaziente + " " + surnamePaziente);

            startDate.setOnClickListener(v -> DataCustomizzata.showDatePickerDialog(getContext(), startDate));
            endDate.setOnClickListener(v -> DataCustomizzata.showDatePickerDialog(getContext(), endDate));

            addScenery.setOnClickListener(v -> addScenery());
            saveTherapy.setOnClickListener(v-> saveTherapy(idPaziente, namePaziente, surnamePaziente));

        });


    }

    @Override
    public void saveScenery(ScenarioGioco scenarioGioco) {
        Log.d("therapy","terapia in saveScenario: " + therapy.toString());
        therapy.addListScenarioGioco(scenarioGioco);
        addScenery.setVisibility(View.VISIBLE);
        saveTherapy.setVisibility(View.VISIBLE);

    }

    private void saveTherapy(String idPaziente, String name, String surname){

        logopedistaViewsModels.addTerapiaInPaziente(therapy, idPaziente);
        logopedistaViewsModels.aggiornaLogopedistaRemoto();
        Bundle bundle = new Bundle();

        bundle.putString("idPaziente", idPaziente);
        bundle.putString("nomePaziente", name);
        bundle.putString("cognomePaziente", surname);
        navigateTo(R.id.action_creazioneTerapiaFragment_to_schedaPazienteFragment, bundle);
    }

    private void addScenery(){
        if(startDate.getText().toString().isEmpty() || endDate.getText().toString().isEmpty()){
            showDialogError(R.string.compilaPrimaTutto);
        }else if(LocalDate.parse(startDate.getText().toString()).isAfter(LocalDate.parse(endDate.getText().toString()))){
            showDialogError(R.string.insertDate);
        }else if(checkDate(logopedistaViewsModels)){
            showDialogError(R.string.checkDate);
        }
        else {
            if(isFirstScenery){
                Log.d("therapy","isFirstScenario");
                therapy = new Terapia(LocalDate.parse(startDate.getText().toString()), LocalDate.parse(endDate.getText().toString()));
                isFirstScenery = false;
            }
            Log.d("therapy", therapy.toString());
            addScenery.setVisibility(View.GONE);

            // passare min e max date per datePicker
            bundle = new Bundle();
            bundle.putString("startDate", startDate.getText().toString());
            bundle.putString("endDate", endDate.getText().toString());

            CreazioneScenarioFragment creazioneScenarioFragment = new CreazioneScenarioFragment(this);
            creazioneScenarioFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewNuovoScenario, creazioneScenarioFragment).commit();
            saveTherapy.setVisibility(View.GONE);
        }
    }

    private void showDialogError(int inputMessage){
        InfoDialog infoDialog = new InfoDialog(getContext(), getString(inputMessage), getString(R.string.tastoRiprova));
        infoDialog.setOnConfirmButtonClickListener(null);
        infoDialog.show();
    }

    private boolean checkDate(LogopedistaViewsModels logopedistaViewModel){
        Paziente paziente = logopedistaViewModel.getPazienteById(idPaziente);
        List<Terapia> listTherapies = paziente.getTerapie();
        LocalDate startDateNewTherapy = LocalDate.parse(startDate.getText().toString());
        LocalDate endDateNewTherapy = LocalDate.parse(endDate.getText().toString());
        if(listTherapies !=null) {
            for (Terapia terapia : listTherapies) {
                LocalDate startDateActualTherapy = terapia.getDataInizioTerapia();
                LocalDate endDateActualTherapy = terapia.getDataFineTerapia();
                if ((startDateNewTherapy.isEqual(startDateActualTherapy) ||
                        startDateNewTherapy.isBefore(startDateActualTherapy)) ||
                        (endDateNewTherapy.isEqual(endDateActualTherapy) ||
                                endDateNewTherapy.isBefore(endDateActualTherapy))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }


}
