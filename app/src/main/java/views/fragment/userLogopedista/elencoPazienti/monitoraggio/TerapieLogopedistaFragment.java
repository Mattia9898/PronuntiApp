package views.fragment.userLogopedista.elencoPazienti.monitoraggio;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import java.util.Comparator;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Logopedista;

import models.domain.profili.Paziente;

import models.domain.terapie.Terapia;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import views.fragment.AbstractNavigazioneFragment;

public class TerapieLogopedistaFragment extends AbstractNavigazioneFragment {

    private String idPaziente;

    private LogopedistaViewsModels mLogopedistaViewModel;

    private int indicePaziente;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_terapie, container, false);

        mLogopedistaViewModel = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        savedInstanceState = getArguments();
        if(savedInstanceState != null){
            this.idPaziente = savedInstanceState.getString("idPaziente");
            this.indicePaziente = savedInstanceState.getInt("indicePaziente");
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Logopedista logopedista = mLogopedistaViewModel.getLogopedistaLiveData().getValue();

        for (Paziente paziente: logopedista.getListaPazienti()) {
            if (paziente.getIdProfilo().equals(idPaziente)){
                paziente.getTerapie().sort(Comparator.comparing(Terapia::getDataInizioTerapia));
                int indiceTerapia = paziente.getTerapie().size() - 1;
                if(indiceTerapia != -1) {
                    cambiaFragmentMonitoraggioLogopedista(indiceTerapia);
                }
            }
        }
    }

    public void cambiaFragmentMonitoraggioLogopedista(int indiceTerapia){
        if(indiceTerapia != -1) {
            Bundle bundle = new Bundle();

            bundle.putString("idPaziente", idPaziente);
            bundle.putInt("indiceTerapia", indiceTerapia);
            bundle.putInt("indicePaziente",indicePaziente);

            NavigazioneTerapieLogopedistaFragment navigazioneTerapieLogopedistaFragment = new NavigazioneTerapieLogopedistaFragment();
            navigazioneTerapieLogopedistaFragment.setArguments(bundle);

            MonitoraggioLogopedistaFragment monitoraggioLogopedistaFragment = new MonitoraggioLogopedistaFragment();
            monitoraggioLogopedistaFragment.setArguments(bundle);

            getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewMonitoraggio, monitoraggioLogopedistaFragment).replace(R.id.fragmentContainerViewNavTerapie, navigazioneTerapieLogopedistaFragment).commit();
        }
    }

}

