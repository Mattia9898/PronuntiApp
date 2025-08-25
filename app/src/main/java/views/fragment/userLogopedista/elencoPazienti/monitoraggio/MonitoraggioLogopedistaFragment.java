package views.fragment.userLogopedista.elencoPazienti.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import models.domain.scenariGioco.ScenarioGioco;
import models.domain.profili.Paziente;

import views.fragment.adapter.ScenarioAdapter;
import views.fragment.adapter.Navigation;
import views.fragment.AbstractNavigationFragment;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.ArrayList;

import java.time.format.DateTimeFormatter;


public class MonitoraggioLogopedistaFragment extends AbstractNavigationFragment implements Navigation {

    private RecyclerView scenery;

    private List<ScenarioGioco> listScenarioGioco;

    private String idPaziente;

    private int therapy;

    private LogopedistaViewsModels logopedistaViewsModels;

    private int indexPatient;

    private TextView startTherapy;

    private TextView endTherapy;

    private Button buttonAddScenery;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monitoraggio, container, false);
        savedInstanceState = getArguments();
        Log.d("MonitoraggioLogopedistaFragmetn","savedInstanceState "+savedInstanceState.toString());

        this.idPaziente = savedInstanceState.getString("idPaziente");
        this.therapy = savedInstanceState.getInt("therapy");
        this.indexPatient = savedInstanceState.getInt("indexPatient");

        scenery = view.findViewById(R.id.scenery);
        scenery.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonAddScenery = view.findViewById(R.id.buttonAddScenario);

        startTherapy = view.findViewById(R.id.startTherapy);
        endTherapy = view.findViewById(R.id.endTherapy);

        logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        listScenarioGioco = monitoringTherapy();
        startDateTherapy();
        endDateTherapy();

        buttonAddScenery.setVisibility(View.VISIBLE);
        buttonAddScenery.setOnClickListener(v -> navigateToAddScenery());

        ScenarioAdapter scenarioAdapter = new ScenarioAdapter(listScenarioGioco, this,
                R.id.action_schedaPazienteFragment_to_risultatoEsercizioDenominazioneImmagineLogopedistaFragment,
                R.id.action_schedaPazienteFragment_to_risultatoEsercizioCoppiaImmaginiLogopedistaFragment,
                R.id.action_schedaPazienteFragment_to_risultatoEsercizioSequenzaParoleLogopedistaFragment,
                logopedistaViewsModels.getModificaDataScenariLogopedistaController(), therapy, idPaziente, indexPatient);

        scenery.setAdapter(scenarioAdapter);
    }

    private void endDateTherapy(){
        if (logopedistaViewsModels.getLogopedistaLiveData().getValue().
                getListaPazienti().get(indexPatient).getTerapie() != null) {

            endTherapy.setText(logopedistaViewsModels.
                    getLogopedistaLiveData().getValue().getListaPazienti().
                    get(indexPatient).getTerapie().get(therapy).
                    getDataFineTerapia().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    private void startDateTherapy() {
        if (logopedistaViewsModels.getLogopedistaLiveData().getValue().
                getListaPazienti().get(indexPatient).getTerapie() != null) {

            startTherapy.setText(logopedistaViewsModels.
                    getLogopedistaLiveData().getValue().getListaPazienti().
                    get(indexPatient).getTerapie().get(therapy).
                    getDataInizioTerapia().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

    }

    private List<ScenarioGioco> monitoringTherapy(){

        List<ScenarioGioco> listScenery = new ArrayList<>();

        for (Paziente paziente: logopedistaViewsModels.getLogopedistaLiveData().getValue().getListaPazienti()) {
            if (paziente.getIdProfilo().equals(idPaziente)) {
                listScenery.addAll(paziente.getTerapie().get(therapy).getListScenariGioco());
                break;
            }
        }
        return listScenery;
    }

    @Override
    public void navigationId(int id, Bundle bundle){
        Log.d("MonitoraggioFragment", "navigateToEsercizio: ");
        setArguments(bundle);
        navigationId(id, bundle);
    }

    private void navigateToAddScenery() {

        Bundle bundle = new Bundle();
        bundle.putString("idPaziente", idPaziente);
        bundle.putInt("therapy", therapy);
        navigationId(R.id.action_schedaPazienteFragment_to_creazioneScenarioFragment, bundle);

    }


}
