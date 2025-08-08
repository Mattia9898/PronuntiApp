package views.fragment.userGenitore.monitoraggio;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageButton;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import it.uniba.dib.pronuntiapp.R;

import models.domain.terapie.Terapia;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import views.dialog.InfoDialog;

import views.fragment.AbstractNavigazioneFragment;

public class NavigazioneTerapieGenitoreFragment extends AbstractNavigazioneFragment {

    private int indiceTerapia;
    private ImageButton imageButtonProssimaTerapia;
    private ImageButton imageButtonTerapiaPrecedente;
    private GenitoreViewsModels mGenitoreViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_navigazione_terapie, container, false);

        imageButtonProssimaTerapia = view.findViewById(R.id.buttonAvantiTerapia);
        imageButtonTerapiaPrecedente = view.findViewById(R.id.buttonIndietroTerapia);

        mGenitoreViewModel = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        mGenitoreViewModel.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {
            if(mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie()!=null) {
                this.indiceTerapia = mGenitoreViewModel.getIndiceUltimaTerapia();
            }else {
                this.indiceTerapia = -1;
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = new Bundle();

        imageButtonProssimaTerapia.setOnClickListener(v -> {
            if (indiceTerapia != mGenitoreViewModel.getIndiceUltimaTerapia() && indiceTerapia != -1) {
                indiceTerapia++;
                Log.d("NavTerapieGenitore",""+indiceTerapia);
                bundle.putInt("indiceTerapiaScelta",indiceTerapia);
                MonitoraggioGenitoreFragment nuovoFragmentMonitoraggio = new MonitoraggioGenitoreFragment();
                nuovoFragmentMonitoraggio.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewMonitoraggio,nuovoFragmentMonitoraggio).commit();
            }else{
                creaDialogErroreCampi(1);
            }
        });

        imageButtonTerapiaPrecedente.setOnClickListener(v -> {
            if (indiceTerapia != 0 && indiceTerapia != -1) {
                indiceTerapia--;
                Log.d("NavTerapieGenitore",""+indiceTerapia);
                bundle.putInt("indiceTerapiaScelta",indiceTerapia);
                MonitoraggioGenitoreFragment nuovoFragmentMonitoraggio = new MonitoraggioGenitoreFragment();
                nuovoFragmentMonitoraggio.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewMonitoraggio,nuovoFragmentMonitoraggio).commit();
            }else{
                creaDialogErroreCampi(2);
            }

        });

    }

    public void creaDialogErroreCampi(int tipoErrore) {

        String messaggioErrore = "";

        switch (tipoErrore) {
            case 1:
                messaggioErrore = getString(R.string.firstTherapy);
                break;
            case 2:
                messaggioErrore = getString(R.string.lastTherapy);
                break;
        }

        InfoDialog infoDialog = new InfoDialog(getContext(), messaggioErrore, getString(R.string.tastoRiprova));
        infoDialog.show();
        infoDialog.setOnConfirmButtonClickListener(null);
    }


}

