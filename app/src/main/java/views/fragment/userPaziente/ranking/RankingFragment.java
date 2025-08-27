package views.fragment.userPaziente.ranking;


import it.uniba.dib.pronuntiapp.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import views.fragment.AbstractNavigationBetweenFragment;

import viewsModels.pazienteViewsModels.PazienteViewsModels;


public class RankingFragment extends AbstractNavigationBetweenFragment {

    private RecyclerView recyclerViewClassifica;

    private RankingAdapter rankingAdapter;

    private PazienteViewsModels pazienteViewsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        this.pazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);

        setToolBar(view, getString(R.string.ranking));

        recyclerViewClassifica = view.findViewById(R.id.recyclerViewClassifica);
        recyclerViewClassifica.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        pazienteViewsModels.getClassificaLiveData().observe(getViewLifecycleOwner(), ranking -> {

            rankingAdapter = new RankingAdapter(ranking,
                                                pazienteViewsModels.getPazienteLiveData().getValue().getUsername());
            recyclerViewClassifica.setAdapter(rankingAdapter);
        });
    }

}