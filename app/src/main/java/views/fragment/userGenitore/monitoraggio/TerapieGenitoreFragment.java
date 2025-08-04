package views.fragment.userGenitore.monitoraggio;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.FrameLayout;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.fragment.app.FragmentContainerView;

import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;

import java.util.ArrayList;

import java.util.Comparator;

import java.util.List;

import java.util.stream.Collectors;

import it.uniba.dib.pronuntiapp.R;

import models.domain.terapie.Terapia;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import views.fragment.AbstractNavigazioneFragment;

public class TerapieGenitoreFragment extends AbstractNavigazioneFragment {

    private FragmentContainerView fragmentContainerViewMonitoraggio;

    private GenitoreViewsModels mGenitoreViewModel;

    private FragmentContainerView fragmentContainerViewNavTerapie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_terapie, container, false);

        fragmentContainerViewNavTerapie = view.findViewById(R.id.fragmentContainerViewNavTerapie);


        fragmentContainerViewMonitoraggio = view.findViewById(R.id.fragmentContainerViewMonitoraggio);
        mGenitoreViewModel = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);

        FragmentContainerView.MarginLayoutParams params = (FragmentContainerView.MarginLayoutParams) fragmentContainerViewMonitoraggio.getLayoutParams();
        params.setMargins(0, 0, 0, getContext().getResources().getDimensionPixelSize(R.dimen.nav_bar_height));

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewNavTerapie, new NavigazioneTerapieGenitoreFragment()).commit();



        mGenitoreViewModel.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {

            if(mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie() != null) {
                mGenitoreViewModel.getPazienteLiveData().getValue().getTerapie().sort(Comparator.comparing(Terapia::getDataInizio));
            }

            int risultatoIndice = mGenitoreViewModel.getIndiceUltimaTerapia();

            if(risultatoIndice != -1) {
                Bundle bundle = new Bundle();

                bundle.putInt("indiceTerapiaScelta",risultatoIndice);

                MonitoraggioGenitoreFragment monitoraggioGenitoreFragment = new MonitoraggioGenitoreFragment();
                monitoraggioGenitoreFragment.setArguments(bundle);

                getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewMonitoraggio, monitoraggioGenitoreFragment).commit();

            }

        });


    }
}

