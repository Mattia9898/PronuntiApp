package views.fragment.userGenitore.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import android.os.Bundle;

import views.fragment.adapter.ScenarioAdapter;
import views.fragment.adapter.Navigation;
import views.fragment.AbstractNavigationBetweenFragment;

import java.util.List;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.lifecycle.ViewModelProvider;

import models.domain.terapie.Terapia;
import models.domain.scenariGioco.ScenarioGioco;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import android.view.LayoutInflater;
import android.util.Log;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;


public class MonitoraggioGenitoreFragment extends AbstractNavigationBetweenFragment implements Navigation {

    private GenitoreViewsModels genitoreViewsModels;

    private int therapy;

    private RecyclerView scenery;

    private List<ScenarioGioco> listScenarGioco;

    private TextView startTherapy;

    private TextView endTherapy;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_monitoraggio, container, false);
        savedInstanceState = getArguments();

        if (savedInstanceState != null && savedInstanceState.containsKey("indiceTerapiaScelta")) {
            therapy = savedInstanceState.getInt("indiceTerapiaScelta");
        } else {
            therapy = 0;
        }

        genitoreViewsModels = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        startTherapy = view.findViewById(R.id.startTherapy);
        endTherapy= view.findViewById(R.id.endTherapy);

        scenery = view.findViewById(R.id.scenery);
        scenery.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        listScenarGioco = checkingTherapy();

        startDateTherapy();
        endDateTherapy();

        Log.d("Monitoraggio genitore",""+ R.id.action_terapieFragment_to_risultatoEsercizioDenominazioneImmagineFragment);

        ScenarioAdapter scenarioAdapter = new ScenarioAdapter(listScenarGioco, this,
                                        R.id.action_terapieFragment_to_risultatoEsercizioDenominazioneImmagineFragment,
                                        R.id.action_terapieFragment_to_risultatoEsercizioCoppiaImmaginiFragment,
                                        R.id.action_terapieFragment_to_risultatoEsercizioSequenzaParoleFragment,
                                        genitoreViewsModels.getModificaDataScenariController(),
                                        therapy,"",0);

        scenery.setAdapter(scenarioAdapter);
    }

    private void endDateTherapy(){
        if (genitoreViewsModels.getPazienteLiveData().getValue().getTerapie() != null) {
            endTherapy.setText(genitoreViewsModels.getPazienteLiveData().
                    getValue().getTerapie().get(therapy).getDataFineTerapia().toString());
        }
    }

    private void startDateTherapy() {
        if (genitoreViewsModels.getPazienteLiveData().getValue().getTerapie() != null) {
            startTherapy.setText(genitoreViewsModels.getPazienteLiveData().
                    getValue().getTerapie().get(therapy).getDataInizioTerapia().toString());
        }

    }

    private List<ScenarioGioco> checkingTherapy(){
        if(genitoreViewsModels.getPazienteLiveData().getValue().getTerapie() != null) {
            Terapia terapia = genitoreViewsModels.getPazienteLiveData().getValue().getTerapie().get(therapy);
            return new ArrayList<>(terapia.getListScenariGioco());
        }
        return new ArrayList<>();
    }

    @Override
    public void navigationId(int id, Bundle bundle){
        navigationTo(id, bundle);
    }

}

