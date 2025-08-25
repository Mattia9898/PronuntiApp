package views.fragment.userPaziente.personaggi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.uniba.dib.pronuntiapp.R;

import viewsModels.pazienteViewsModels.PazienteViewsModels;
import viewsModels.pazienteViewsModels.controller.PersonaggiController;

import models.domain.profili.personaggio.Personaggio;

import views.fragment.AbstractNavigationFragment;

public class PersonaggiFragment extends AbstractNavigationFragment {

    private RecyclerView recyclerViewPersonaggiSbloccati;
    private RecyclerView recyclerViewPersonaggiAcquistabili;
    private List<Personaggio> personaggiSbloccati;
    private List<Personaggio> personaggiOttenibili;
    private List<String> idsPersonaggiOttenibili;
    private List<String> idsPersonaggiSbloccati;
    private NestedScrollView nestedScrollView;
    private PazienteViewsModels mPazienteViewsModels;
    private PersonaggiController mController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= inflater.inflate(R.layout.fragment_personaggi, container, false);

        this.mPazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        this.mController = mPazienteViewsModels.getPersonaggiController();

        setToolBarNoTitle(view);

        nestedScrollView = view.findViewById(R.id.nestedScrollViewPersonaggi);
        recyclerViewPersonaggiSbloccati = view.findViewById(R.id.recyclerViewPersonaggiSbloccati);
        recyclerViewPersonaggiAcquistabili = view.findViewById(R.id.recyclerViewPersonaggiAcquistabili);
        recyclerViewPersonaggiAcquistabili.setHasFixedSize(true);

        idsPersonaggiOttenibili = new ArrayList<>();
        idsPersonaggiSbloccati = new ArrayList<>();
        personaggiSbloccati = new ArrayList<>();
        personaggiOttenibili = new ArrayList<>();

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewPersonaggiSbloccati.setItemAnimator(itemAnimator);
        recyclerViewPersonaggiAcquistabili.setItemAnimator(itemAnimator);

        GridLayoutManager layoutManagerSbloccati = new GridLayoutManager(getContext(), 3);
        GridLayoutManager layoutManagerAcquistabili = new GridLayoutManager(getContext(), 3);
        recyclerViewPersonaggiSbloccati.setLayoutManager(layoutManagerSbloccati);
        recyclerViewPersonaggiAcquistabili.setLayoutManager(layoutManagerAcquistabili);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapters();
    }

    private void setAdapters() {
        Map<String, Integer> personaggiPaziente = mPazienteViewsModels.getPazienteLiveData().getValue().getPersonaggiSbloccati();
        List<Personaggio> personaggi = mPazienteViewsModels.getListaPersonaggiLiveData().getValue();

        setIdLists(personaggiPaziente);
        personaggiSbloccati = mController.getSortedListPersonaggi(personaggi, idsPersonaggiSbloccati);
        personaggiOttenibili = mController.getSortedListPersonaggi(personaggi, idsPersonaggiOttenibili);

        PersonaggiSbloccatiAdapter personaggiSbloccatiAdapter = new PersonaggiSbloccatiAdapter(getContext(), personaggiSbloccati, mController);
        PersonaggiOttenibiliAdapter personaggiOttenibiliAdapter = new PersonaggiOttenibiliAdapter(getContext(), personaggiOttenibili, personaggiSbloccatiAdapter, nestedScrollView, mController);

        recyclerViewPersonaggiAcquistabili.setAdapter(personaggiOttenibiliAdapter);
        recyclerViewPersonaggiSbloccati.setAdapter(personaggiSbloccatiAdapter);
    }

    private void setIdLists(Map<String, Integer> map) {
        setFirstId(map);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = Integer.parseInt(String.valueOf(entry.getValue()));
            if (value == 1) {
                idsPersonaggiSbloccati.add(key);
            } else if (value == 0) {
                idsPersonaggiOttenibili.add(key);
            }
        }
    }

    private void setFirstId(Map<String, Integer> map){
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = Integer.parseInt(String.valueOf(entry.getValue()));
            if (value == 2) {
                idsPersonaggiSbloccati.add(key);
            }
        }
    }

}