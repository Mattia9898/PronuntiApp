package views.fragment.userLogopedista.elencoPazienti.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import views.fragment.AbstractNavigazioneFragment;
import views.dialog.InfoDialog;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ImageButton;


public class NavigationTerapieLogopedistaFragment extends AbstractNavigazioneFragment {

    private LinearLayout linearLayout;

    private ImageButton buttonPreviousTherapy;

    private ImageButton buttonNextTherapy;

    private String idPatient;

    private int indexPatient;

    private int indexTherapy;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_navigation_terapie, container, false);

        buttonNextTherapy = view.findViewById(R.id.buttonNextTherapy);
        buttonPreviousTherapy = view.findViewById(R.id.buttonPreviousTherapy);

        savedInstanceState = getArguments();

        indexTherapy = savedInstanceState.getInt("indexTherapy");
        idPatient = savedInstanceState.getString("idPatient");
        indexPatient = savedInstanceState.getInt("indexPatient");

        linearLayout = view.findViewById(R.id.myLinearLayout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        int indexLastTherapy = indexTherapy;
        Bundle bundle = new Bundle();

        if (indexTherapy != -1) {

            buttonNextTherapy.setOnClickListener(v -> {
                if (indexTherapy != indexLastTherapy) {

                    indexTherapy++;
                    Log.d("NavigationTherapyGenitore", "" + indexTherapy);

                    bundle.putInt("indexTherapy", indexTherapy);
                    bundle.putString("idPatient", idPatient);
                    bundle.putInt("indexPatient", indexPatient);

                    MonitoraggioLogopedistaFragment monitoraggioLogopedistaFragment = new MonitoraggioLogopedistaFragment();
                    monitoraggioLogopedistaFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().
                            replace(R.id.monitoring, monitoraggioLogopedistaFragment).commit();
                } else {
                    showDialogError(1);
                }
            });

            buttonPreviousTherapy.setOnClickListener(v -> {
                if (indexTherapy != 0) {

                    indexTherapy--;
                    Log.d("NavigationTherapyGenitore", "" + indexTherapy);

                    bundle.putInt("indexTherapy", indexTherapy);
                    bundle.putString("idPatient", idPatient);
                    bundle.putInt("indexPatient", indexPatient);

                    MonitoraggioLogopedistaFragment monitoraggioLogopedistaFragment = new MonitoraggioLogopedistaFragment();
                    monitoraggioLogopedistaFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().
                            replace(R.id.monitoring, monitoraggioLogopedistaFragment).commit();
                } else {
                    showDialogError(2);
                }
            });

        }else{
            linearLayout.setVisibility(View.GONE);
        }
    }


    public void showDialogError(int typeError) {

        String messageError = "";

        switch (typeError) {
            case 1:
                messageError = getString(R.string.firstTherapy);
                break;
            case 2:
                messageError = getString(R.string.lastTherapy);
                break;
        }

        InfoDialog infoDialog = new InfoDialog(getContext(), messageError, getString(R.string.tastoRiprova));
        infoDialog.show();
        infoDialog.setOnConfirmButtonClickListener(null);
    }


}

