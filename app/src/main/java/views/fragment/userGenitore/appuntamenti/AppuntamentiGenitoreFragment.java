package views.fragment.userGenitore.appuntamenti;

import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Appuntamento;

import viewsModels.genitoreViewsModels.controller.AppuntamentiGenitoreController;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

import views.fragment.AbstractNavigazioneFragment;

public class AppuntamentiGenitoreFragment extends AbstractNavigazioneFragment {

    private RecyclerView recyclerViewAppuntamentiGenitore;

    private AppuntamentiGenitoreAdapter appuntamentoAdapter;

    private GenitoreViewsModels mGenitoreViewModel;

    private AppuntamentiGenitoreController mController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appuntamenti_genitore, container, false);

        this.mGenitoreViewModel = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);
        this.mController = mGenitoreViewModel.getAppuntamentiControllerGenitore();

        setToolBar(view,getString(R.string.i_tuoi_appuntamenti));

        recyclerViewAppuntamentiGenitore = view.findViewById(R.id.recyclerViewAppuntamentiGenitore);
        recyclerViewAppuntamentiGenitore.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        List<Appuntamento> appuntamenti = mGenitoreViewModel.getAppuntamentiLiveData().getValue();

        appuntamentoAdapter = new AppuntamentiGenitoreAdapter(appuntamenti);
        recyclerViewAppuntamentiGenitore.setAdapter(appuntamentoAdapter);
    }

}
