package views.fragment.userLogopedista.elencoPazienti;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Paziente;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import viewsModels.pazienteViewsModels.PazienteViewsModels;

import views.fragment.AbstractNavigazioneFragment;

import views.fragment.adapter.Navigation;

public class PazienteFragment extends AbstractNavigazioneFragment implements Navigation {

    private RecyclerView recyclerViewListaPazienti;

    private PazienteAdapter adapterPazienti;

    private Button addPazientiButton;

    private SearchView searchViewListaPazienti;

    private PazienteViewsModels mPazienteViewModel;

    private LogopedistaViewsModels mLogopedistaViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pazienti, container, false);

        mLogopedistaViewModel = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);
        mPazienteViewModel = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);

        initViews(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void initViews(View view) {
        addPazientiButton = view.findViewById(R.id.addPaziente);
        addPazientiButton.setOnClickListener(v -> navigateTo(R.id.action_pazientiFragment_to_registrazionePazienteGenitoreFragment));

        searchViewListaPazienti = view.findViewById(R.id.searchViewListaPazienti);
        recyclerViewListaPazienti = view.findViewById(R.id.pazientiRecyclerView);
        recyclerViewListaPazienti.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void loadData() {

        mLogopedistaViewModel.getLogopedistaLiveData().observe(getViewLifecycleOwner(), logopedista -> {

            List<Paziente> pazienti = new ArrayList<>();
            if(logopedista.getListaPazienti() != null) {
                pazienti = logopedista.getListaPazienti();
            }
            Log.d("PazientiFragment.loadData()", "pazienti: " + ((pazienti == null) ? "null" : pazienti.toString()));

            adapterPazienti = new PazienteAdapter(pazienti, this);
            recyclerViewListaPazienti.setAdapter(adapterPazienti);

            searchViewListaPazienti.setOnCloseListener(() -> {
                addPazientiButton.setText("Paziente +");
                return false;
            });
            searchViewListaPazienti.setOnSearchClickListener(v -> addPazientiButton.setText("+"));

            searchViewListaPazienti.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("PazientiFragment", "onQueryTextSubmit: " + query);
                    adapterPazienti.getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapterPazienti.getFilter().filter(newText);
                    return true;
                }
            });
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().setTitle("I tuoi pazienti");
        }
        if(searchViewListaPazienti.isIconified())
            addPazientiButton.setText("Paziente +");
        else addPazientiButton.setText("+");
    }

    @Override
    public void navigationId(int id, Bundle bundle) {
        navigateTo(id, bundle);
    }
}

