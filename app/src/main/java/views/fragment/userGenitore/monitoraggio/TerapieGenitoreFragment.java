package views.fragment.userGenitore.monitoraggio;


import it.uniba.dib.pronuntiapp.R;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import views.fragment.AbstractNavigazioneFragment;
import viewsModels.genitoreViewsModels.GenitoreViewsModels;
import models.domain.terapie.Terapia;

import java.util.Comparator;

import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;


public class TerapieGenitoreFragment extends AbstractNavigazioneFragment {

    private FragmentContainerView navigationTherapies;

    private FragmentContainerView monitoring;

    private GenitoreViewsModels genitoreViewsModels;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_terapie, container, false);

        navigationTherapies = view.findViewById(R.id.navigationTherapies);
        monitoring = view.findViewById(R.id.monitoring);

        genitoreViewsModels = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        FragmentContainerView.MarginLayoutParams marginLayoutParams =
                (FragmentContainerView.MarginLayoutParams) monitoring.getLayoutParams();

        marginLayoutParams.setMargins(0, 0, 0, getContext().getResources().getDimensionPixelSize(R.dimen.nav_bar_height));

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().beginTransaction().
                replace(R.id.navigationTherapies, new NavigationTerapieGenitoreFragment()).commit();

        genitoreViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {

            if(genitoreViewsModels.getPazienteLiveData().getValue().getTerapie() != null) {
                genitoreViewsModels.getPazienteLiveData().getValue().
                        getTerapie().sort(Comparator.comparing(Terapia::getDataInizioTerapia));
            }

            int result = genitoreViewsModels.getIndiceUltimaTerapia();

            if(result != -1) {

                Bundle bundle = new Bundle();
                bundle.putInt("terapiaScelta", result);

                MonitoraggioGenitoreFragment monitoraggioGenitoreFragment = new MonitoraggioGenitoreFragment();
                monitoraggioGenitoreFragment.setArguments(bundle);

                getChildFragmentManager().beginTransaction().replace(R.id.monitoring, monitoraggioGenitoreFragment).commit();

            }

        });


    }
}

