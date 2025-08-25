package views.fragment.userLogopedista.elencoPazienti.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import android.os.Bundle;

import java.util.Comparator;

import androidx.lifecycle.ViewModelProvider;

import views.fragment.AbstractNavigationFragment;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;

import models.domain.terapie.Terapia;
import models.domain.profili.Paziente;
import models.domain.profili.Logopedista;


public class TerapieLogopedistaFragment extends AbstractNavigationFragment {

    private String idPaziente;

    private LogopedistaViewsModels logopedistaViewsModels;

    private int indexPatient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_terapie, container, false);
        logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        savedInstanceState = getArguments();

        if(savedInstanceState != null){
            this.idPaziente = savedInstanceState.getString("idPaziente");
            this.indexPatient = savedInstanceState.getInt("indexPatient");
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        Logopedista logopedista = logopedistaViewsModels.getLogopedistaLiveData().getValue();

        for (Paziente paziente: logopedista.getListaPazienti()) {
            if (paziente.getIdProfilo().equals(idPaziente)){
                paziente.getTerapie().sort(Comparator.comparing(Terapia::getDataInizioTerapia));
                int indexTherapy = paziente.getTerapie().size() - 1;
                if(indexTherapy != -1) {
                    switchFragmentMonitoring(indexTherapy);
                }
            }
        }
    }

    public void switchFragmentMonitoring(int indexTherapy){

        if(indexTherapy != -1) {

            Bundle bundle = new Bundle();

            bundle.putString("idPaziente", idPaziente);
            bundle.putInt("indexTherapy", indexTherapy);
            bundle.putInt("indexPatient",indexPatient);

            NavigationTerapieLogopedistaFragment navigationTerapieLogopedistaFragment = new NavigationTerapieLogopedistaFragment();
            navigationTerapieLogopedistaFragment.setArguments(bundle);

            MonitoraggioLogopedistaFragment monitoraggioLogopedistaFragment = new MonitoraggioLogopedistaFragment();
            monitoraggioLogopedistaFragment.setArguments(bundle);

            getChildFragmentManager().beginTransaction().
                    replace(R.id.monitoring, monitoraggioLogopedistaFragment).
                    replace(R.id.navigationTherapies, navigationTerapieLogopedistaFragment).commit();
        }
    }

}

