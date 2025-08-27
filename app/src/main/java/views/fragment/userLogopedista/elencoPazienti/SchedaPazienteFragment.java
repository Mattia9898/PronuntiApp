package views.fragment.userLogopedista.elencoPazienti;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;
import views.fragment.AbstractNavigationBetweenFragment;
import views.fragment.userLogopedista.elencoPazienti.monitoraggio.TerapieLogopedistaFragment;

import models.domain.profili.Logopedista;
import models.domain.profili.Paziente;


public class SchedaPazienteFragment extends AbstractNavigationBetweenFragment {

    private Button buttonAddTherapy;

    private String idPaziente;

    private LogopedistaViewsModels logopedistaViewsModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scheda_paziente_logopedista, container, false);
        savedInstanceState = getArguments();

        if(savedInstanceState != null){
            idPaziente = savedInstanceState.getString("idPaziente");
        }

        this.logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        buttonAddTherapy = view.findViewById(R.id.buttonAddTherapy);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = new Bundle();
        int indexTherapy = -1;
        int indexPatient = 0;
        Logopedista logopedista = logopedistaViewsModels.getLogopedistaLiveData().getValue();
        Paziente paziente = logopedistaViewsModels.getPazienteById(idPaziente);

        setToolBar(view, paziente.getNome() + " " + paziente.getCognome());

        for (Paziente pazienteCycle: logopedista.getListaPazienti()) {
            if (pazienteCycle.getIdProfilo().equals(idPaziente)) {
                if (pazienteCycle.getTerapie() != null) {
                    indexTherapy = pazienteCycle.getTerapie().size() - 1;
                    if (indexTherapy != -1) {
                        bundle.putString("idPaziente", idPaziente);
                        bundle.putInt("indexPatient", indexPatient);
                        indexPatient++;
                        bundle.putInt("indexTherapy", indexTherapy);
                        break;
                    }
                }
            }

            indexPatient++;
        }

        if(indexTherapy != -1) {
            TerapieLogopedistaFragment terapieLogopedistaFragment = new TerapieLogopedistaFragment();
            terapieLogopedistaFragment.setArguments(bundle);
            getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewTerapie, terapieLogopedistaFragment).commit();
        }

        buttonAddTherapy.setOnClickListener(v -> {
            Bundle bundleButtonAddTherapy = new Bundle();
            bundleButtonAddTherapy.putString("idPaziente", idPaziente);
            navigationTo(R.id.action_schedaPazienteFragment_to_creazioneTerapiaFragment, bundleButtonAddTherapy);
        });

    }
}
