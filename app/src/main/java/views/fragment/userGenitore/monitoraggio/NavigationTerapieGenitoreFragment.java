package views.fragment.userGenitore.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.Nullable;

import androidx.annotation.NonNull;

import android.os.Bundle;

import views.fragment.AbstractNavigationBetweenFragment;
import views.dialog.InfoDialog;

import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.util.Log;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import androidx.lifecycle.ViewModelProvider;


public class NavigationTerapieGenitoreFragment extends AbstractNavigationBetweenFragment {

    private int therapy;

    private ImageButton buttonNextTherapy;

    private ImageButton buttonPreviousTherapy;

    private GenitoreViewsModels genitoreViewsModels;


    public void showDialogError(int typeError) {

        String errorMessage = "";

        switch (typeError) {
            case 1:
                errorMessage = getString(R.string.firstTherapy);
                break;
            case 2:
                errorMessage = getString(R.string.lastTherapy);
                break;
        }

        InfoDialog infoDialog = new InfoDialog(getContext(), errorMessage, getString(R.string.tastoRiprova));
        infoDialog.show();
        infoDialog.setOnConfirmButtonClickListener(null);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_navigation_terapie, container, false);

        buttonPreviousTherapy = view.findViewById(R.id.buttonPreviousTherapy);
        buttonNextTherapy = view.findViewById(R.id.buttonNextTherapy);

        genitoreViewsModels = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        genitoreViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {
            if(genitoreViewsModels.getPazienteLiveData().getValue().getTerapie()!=null) {
                this.therapy = genitoreViewsModels.getIndexLastTherapy();
            }else {
                this.therapy = -1;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = new Bundle();

        buttonNextTherapy.setOnClickListener(v -> {
            if (therapy != genitoreViewsModels.getIndexLastTherapy() && therapy != -1) {

                therapy++;
                Log.d("NavigationTherapyGenitore","" + therapy);
                bundle.putInt("indexTherapySelected", therapy);

                MonitoraggioGenitoreFragment newMonitoraggioGenitoreFragment = new MonitoraggioGenitoreFragment();
                newMonitoraggioGenitoreFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.monitoring, newMonitoraggioGenitoreFragment).commit();
            }else{
                showDialogError(1);
            }
        });

        buttonPreviousTherapy.setOnClickListener(v -> {
            if (therapy != 0 && therapy != -1) {

                therapy--;
                Log.d("NavTerapieGenitore",""+therapy);
                bundle.putInt("indiceTerapiaScelta",therapy);

                MonitoraggioGenitoreFragment newMonitoraggioGenitoreFragment = new MonitoraggioGenitoreFragment();
                newMonitoraggioGenitoreFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.monitoring, newMonitoraggioGenitoreFragment).commit();
            }else{
                showDialogError(2);
            }
        });

    }


}

