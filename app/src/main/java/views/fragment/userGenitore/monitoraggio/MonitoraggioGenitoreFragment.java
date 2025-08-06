package views.fragment.userGenitore.monitoraggio;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.scenariGioco.ScenarioGioco;

import models.domain.terapie.Terapia;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import views.fragment.AbstractNavigazioneFragment;

import views.fragment.adapter.Navigation;

import views.fragment.adapter.ScenarioAdapter;


public class MonitoraggioGenitoreFragment extends AbstractNavigazioneFragment implements Navigation {

    private RecyclerView recyclerViewScenari;

    private List<ScenarioGioco> listaScenari;

    private int indiceTerapia;

    private GenitoreViewsModels mGenitoreViewModel;

    private TextView textViewDataInizioTerapia;

    private TextView textViewDataFineTerapia;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_monitoraggio, container, false);

        savedInstanceState = getArguments();

        if (savedInstanceState != null && savedInstanceState.containsKey("indiceTerapiaScelta")) {
            indiceTerapia = savedInstanceState.getInt("indiceTerapiaScelta");
        } else {
            indiceTerapia = 0;
        }

        mGenitoreViewModel = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);


        textViewDataInizioTerapia = view.findViewById(R.id.textViewDataInizioTerapia);
        textViewDataFineTerapia= view.findViewById(R.id.textViewDataFineTerapia);

        recyclerViewScenari = view.findViewById(R.id.recyclerViewScenari);
        recyclerViewScenari.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        listaScenari = monitoraggioTerapie();
        setTextViewDataInizioTerapia();
        setTextViewDataFineTerapia();
        Log.d("Monitoraggio genitore",""+ R.id.action_terapieFragment_to_risultatoEsercizioDenominazioneImmagineFragment);


        ScenarioAdapter adapter = new ScenarioAdapter(listaScenari, this,
                                        R.id.action_terapieFragment_to_risultatoEsercizioDenominazioneImmagineFragment,
                                        R.id.action_terapieFragment_to_risultatoEsercizioCoppiaImmaginiFragment,
                                        R.id.action_terapieFragment_to_risultatoEsercizioSequenzaParoleFragment,
                                        mGenitoreViewModel.getModificaDataScenariController(),
                                        indiceTerapia,"",0);
        recyclerViewScenari.setAdapter(adapter);
    }

    @Override
    public void navigationId(int id, Bundle bundle){
        navigateTo(id, bundle);
    }

    private List<ScenarioGioco> monitoraggioTerapie(){
        if(mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie() != null) {
            Terapia terapiaScelta = mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie().get(indiceTerapia);
            return new ArrayList<>(terapiaScelta.getListScenariGioco());
        }
        return new ArrayList<>();
    }

    private void setTextViewDataInizioTerapia() {
        if (mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie() != null) {
            textViewDataInizioTerapia.setText(mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie().get(indiceTerapia).getDataInizioTerapia().toString());
        }

    }

    private void setTextViewDataFineTerapia(){
        if (mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie() != null) {
            textViewDataFineTerapia.setText(mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie().get(indiceTerapia).getDataFineTerapia().toString());
        }
    }
}

