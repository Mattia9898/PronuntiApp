package views.fragment.userPaziente.classifica;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import it.uniba.dib.pronuntiapp.R;

import viewsModels.pazienteViewsModels.controller.ClassificaController;

import viewsModels.pazienteViewsModels.PazienteViewsModels;

import views.fragment.AbstractNavigazioneFragment;

public class ClassificaFragment extends AbstractNavigazioneFragment {

    private RecyclerView recyclerViewClassifica;

    private EntryClassificaAdapter entryClassificaAdapter;

    private PazienteViewsModels mPazienteViewModel;

    private ClassificaController mController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_classifica, container, false);

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

            entryClassificaAdapter = new EntryClassificaAdapter(classifica, mPazienteViewModel.getPazienteLiveData().getValue().getUsername());
            recyclerViewClassifica.setAdapter(entryClassificaAdapter);
        });
    }

}