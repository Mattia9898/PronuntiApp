package views.fragment.userGenitore.appuntamenti;


import it.uniba.dib.pronuntiapp.R;

import java.util.List;

import models.domain.profili.Appuntamento;

import views.fragment.AbstractNavigationBetweenFragment;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;
import viewsModels.genitoreViewsModels.controller.AppuntamentiGenitoreController;

import android.view.ViewGroup;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AppuntamentiGenitoreFragment extends AbstractNavigationBetweenFragment {


    private AppuntamentiGenitoreAdapter appuntamentiGenitoreAdapter;

    private AppuntamentiGenitoreController appuntamentiGenitoreController;

    private GenitoreViewsModels genitoreViewsModels;

    private RecyclerView appuntamentiGenitore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appuntamenti_genitore, container, false);

        this.genitoreViewsModels = new ViewModelProvider(requireActivity()).get(GenitoreViewsModels.class);
        this.appuntamentiGenitoreController = genitoreViewsModels.getAppuntamentiControllerGenitore();

        setToolBar(view,getString(R.string.i_tuoi_appuntamenti));

        appuntamentiGenitore = view.findViewById(R.id.recyclerViewAppuntamentiGenitore);
        appuntamentiGenitore.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        List<Appuntamento> listAppuntamento = genitoreViewsModels.getAppuntamentiLiveData().getValue();

        appuntamentiGenitoreAdapter = new AppuntamentiGenitoreAdapter(listAppuntamento);
        appuntamentiGenitore.setAdapter(appuntamentiGenitoreAdapter);
    }

}
