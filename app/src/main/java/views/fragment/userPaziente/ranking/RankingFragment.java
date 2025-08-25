package views.fragment.userPaziente.ranking;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import it.uniba.dib.pronuntiapp.R;

import viewsModels.pazienteViewsModels.controller.RankingController;

import viewsModels.pazienteViewsModels.PazienteViewsModels;

import views.fragment.AbstractNavigationFragment;

public class RankingFragment extends AbstractNavigationFragment {

    private RecyclerView recyclerViewClassifica;

    private RankingAdapter rankingAdapter;

    private PazienteViewsModels mPazienteViewModel;

    private RankingController mController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        this.mPazienteViewModel = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        this.mController = mPazienteViewModel.getClassificaController();

        setToolBar(view, getString(R.string.classifica));

        recyclerViewClassifica = view.findViewById(R.id.recyclerViewClassifica);
        recyclerViewClassifica.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mPazienteViewModel.getClassificaLiveData().observe(getViewLifecycleOwner(), classifica -> {

            rankingAdapter = new RankingAdapter(classifica, mPazienteViewModel.getPazienteLiveData().getValue().getUsername());
            recyclerViewClassifica.setAdapter(rankingAdapter);
        });
    }

}