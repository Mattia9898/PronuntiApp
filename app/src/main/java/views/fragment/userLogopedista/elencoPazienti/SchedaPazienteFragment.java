package views.fragment.userLogopedista.elencoPazienti;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

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

import views.fragment.userGenitore.monitoraggio.MonitoraggioGenitoreFragment;

import views.fragment.userLogopedista.elencoPazienti.monitoraggio.MonitoraggioLogopedistaFragment;

import views.fragment.userLogopedista.elencoPazienti.monitoraggio.TerapieLogopedistaFragment;

public class SchedaPazienteFragment extends AbstractNavigazioneFragment {

    private Button addTerapiaButton;

    private String idPaziente;

    private String nomePaziente;

    private String cognomePaziente;

    private LogopedistaViewsModels mLogopedistaViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_scheda_paziente_logopedista, container, false);

        savedInstanceState = getArguments();

        if(savedInstanceState != null){
            idPaziente = savedInstanceState.getString("idPaziente");
        }

        this.mLogopedistaViewModel = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);



        addTerapiaButton = view.findViewById(R.id.buttonAddTerapia);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = new Bundle();

        int indicePaziente = 0;

        int indiceTerapia = -1;

        Logopedista logopedista = mLogopedistaViewModel.getLogopedistaLiveData().getValue();
        Paziente paziente1 = mLogopedistaViewModel.getPazienteById(idPaziente);

        setToolBar(view, paziente1.getNome()+" "+paziente1.getCognome());


        for (Paziente paziente: logopedista.getPazienti()) {
            if (paziente.getIdProfilo().equals(idPaziente)) {
                if (paziente.getTerapie() != null) {
                    indiceTerapia = paziente.getTerapie().size() - 1;
                    if (indiceTerapia != -1) {
                        bundle.putString("idPaziente", idPaziente);
                        bundle.putInt("indicePaziente", indicePaziente);
                        indicePaziente++;
                        bundle.putInt("indiceTerapia", indiceTerapia);
                        break;
                    }
                }
            }
            indicePaziente++;
        }

        if(indiceTerapia != -1) {
            TerapieLogopedistaFragment terapieLogopedistaFragment = new TerapieLogopedistaFragment();
            terapieLogopedistaFragment.setArguments(bundle);

            getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewTerapie, terapieLogopedistaFragment).commit();
        }

        addTerapiaButton.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            bundle1.putString("idPaziente",idPaziente);
            navigateTo(R.id.action_schedaPazienteFragment_to_creazioneTerapiaFragment, bundle1);
        });

    }
}
