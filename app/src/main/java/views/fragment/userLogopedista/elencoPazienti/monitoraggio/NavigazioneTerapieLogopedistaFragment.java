package views.fragment.userLogopedista.elencoPazienti.monitoraggio;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageButton;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import it.uniba.dib.pronuntiapp.R;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import views.dialog.InfoDialog;

import views.fragment.AbstractNavigazioneFragment;

public class NavigazioneTerapieLogopedistaFragment extends AbstractNavigazioneFragment {

    private ImageButton imageButtonProssimaTerapia;

    private ImageButton imageButtonTerapiaPrecedente;

    private LogopedistaViewsModels mLogoPedistaViewModel;

    private int indiceTerapia;

    private String idPaziente;

    private int indicePaziente;

    private LinearLayout linearLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_navigazione_terapie, container, false);

        imageButtonProssimaTerapia = view.findViewById(R.id.buttonAvantiTerapia);
        imageButtonTerapiaPrecedente = view.findViewById(R.id.buttonIndietroTerapia);

        savedInstanceState = getArguments();
        indiceTerapia = savedInstanceState.getInt("indiceTerapia");
        idPaziente = savedInstanceState.getString("idPaziente");
        indicePaziente = savedInstanceState.getInt("indicePaziente");

        linearLayout = view.findViewById(R.id.myLinearLayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        int indiceultimaTerapia = indiceTerapia;

        Bundle bundle = new Bundle();

        if(indiceTerapia!=-1) {

            imageButtonProssimaTerapia.setOnClickListener(v -> {
                if (indiceTerapia != indiceultimaTerapia) {
                    indiceTerapia++;
                    Log.d("NavTerapieGenitore", "" + indiceTerapia);
                    bundle.putInt("indiceTerapia", indiceTerapia);
                    bundle.putString("idPaziente", idPaziente);
                    bundle.putInt("indicePaziente", indicePaziente);
                    MonitoraggioLogopedistaFragment nuovoFragmentMonitoraggio = new MonitoraggioLogopedistaFragment();
                    nuovoFragmentMonitoraggio.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewMonitoraggio, nuovoFragmentMonitoraggio).commit();
                } else {
                    creaDialogErroreCampi(1);
                }
            });

            imageButtonTerapiaPrecedente.setOnClickListener(v -> {
                if (indiceTerapia != 0) {
                    indiceTerapia--;
                    Log.d("NavTerapieGenitore", "" + indiceTerapia);
                    bundle.putInt("indiceTerapia", indiceTerapia);
                    bundle.putString("idPaziente", idPaziente);
                    bundle.putInt("indicePaziente", indicePaziente);
                    MonitoraggioLogopedistaFragment nuovoFragmentMonitoraggio = new MonitoraggioLogopedistaFragment();
                    nuovoFragmentMonitoraggio.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewMonitoraggio, nuovoFragmentMonitoraggio).commit();
                } else {
                    creaDialogErroreCampi(2);
                }
            });

        }else{
            linearLayout.setVisibility(View.GONE);
        }
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
        infoDialog.setOnConfermaButtonClickListener(null);
    }


}

