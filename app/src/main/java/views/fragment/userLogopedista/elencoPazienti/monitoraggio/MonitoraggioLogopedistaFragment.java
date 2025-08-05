package views.fragment.userLogopedista.elencoPazienti.monitoraggio;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Paziente;

import models.domain.scenariGioco.ScenarioGioco;

import models.domain.terapie.Terapia;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import views.fragment.AbstractNavigazioneFragment;

import views.fragment.adapter.Navigation;

import views.fragment.adapter.ScenarioAdapter;


public class MonitoraggioLogopedistaFragment extends AbstractNavigazioneFragment implements Navigation {

    private RecyclerView recyclerViewScenari;

    private List<ScenarioGioco> listaScenari;

    private String idTerapiaScelta;

    private Terapia terapiaScelta;

    private String idPaziente;

    private int indiceTerapia;

    private LogopedistaViewsModels mLogopedistaViewModel;

    private int indicePaziente;

    private TextView textViewDataInizioTerapia;

    private TextView textViewDataFineTerapia;

    private Button buttonAddScenario;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monitoraggio, container, false);

        savedInstanceState = getArguments();

        Log.d("MonitoraggioLogopedistaFragmetn","savedInstanceState "+savedInstanceState.toString());

        this.idPaziente = savedInstanceState.getString("idPaziente");
        this.indiceTerapia = savedInstanceState.getInt("indiceTerapia");
        this.indicePaziente = savedInstanceState.getInt("indicePaziente");

        recyclerViewScenari = view.findViewById(R.id.recyclerViewScenari);
        recyclerViewScenari.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonAddScenario = view.findViewById(R.id.buttonAddScenario);

        textViewDataInizioTerapia = view.findViewById(R.id.textViewDataInizioTerapia);
        textViewDataFineTerapia = view.findViewById(R.id.textViewDataFineTerapia);


        mLogopedistaViewModel = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listaScenari = monitoraggioTerapie();
        setTextViewDataInizioTerapia();
        setTextViewDataFineTerapia();

        buttonAddScenario.setVisibility(View.VISIBLE);
        buttonAddScenario.setOnClickListener(v -> navigateToAddScenario());

        ScenarioAdapter adapter = new ScenarioAdapter(listaScenari, this,
                R.id.action_schedaPazienteFragment_to_risultatoEsercizioDenominazioneImmagineLogopedistaFragment,
                R.id.action_schedaPazienteFragment_to_risultatoEsercizioCoppiaImmaginiLogopedistaFragment,
                R.id.action_schedaPazienteFragment_to_risultatoEsercizioSequenzaParoleLogopedistaFragment
                , mLogopedistaViewModel.getModificaDataScenariLogopedistaController(), indiceTerapia,idPaziente,indicePaziente);

        recyclerViewScenari.setAdapter(adapter);
    }

    private void navigateToAddScenario() {

        Bundle bundle = new Bundle();
        bundle.putString("idPaziente", idPaziente);
        bundle.putInt("indiceTerapia", indiceTerapia);
        navigationId(R.id.action_schedaPazienteFragment_to_creazioneScenarioFragment, bundle);

    }

    @Override
    public void navigationId(int id, Bundle bundle){
        Log.d("MonitoraggioFragment", "navigateToEsercizio: ");
        setArguments(bundle);
        navigationId(id, bundle);
    }

    private List<ScenarioGioco> monitoraggioTerapie(){

        List<ScenarioGioco> listaScenari = new ArrayList<>();

        for (Paziente paziente: mLogopedistaViewModel.getLogopedistaLiveData().getValue().getListaPazienti()) {
            if (paziente.getIdProfilo().equals(idPaziente)) {
                listaScenari.addAll(paziente.getTerapie().get(indiceTerapia).getScenariGioco());
                break;
            }
        }
        return listaScenari;
    }

    private void setTextViewDataInizioTerapia() {
        if (mLogopedistaViewModel.getLogopedistaLiveData().getValue().getListaPazienti().get(indicePaziente).getTerapie() != null) {
            textViewDataInizioTerapia.setText(mLogopedistaViewModel.getLogopedistaLiveData().getValue().getListaPazienti().get(indicePaziente).getTerapie().get(indiceTerapia).getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

    }

    private void setTextViewDataFineTerapia(){
        if (mLogopedistaViewModel.getLogopedistaLiveData().getValue().getListaPazienti().get(indicePaziente).getTerapie() != null) {
            textViewDataFineTerapia.setText(mLogopedistaViewModel.getLogopedistaLiveData().getValue().getListaPazienti().get(indicePaziente).getTerapie().get(indiceTerapia).getDataFine().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

}
