package views.fragment.userLogopedista.elencoPazienti;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import models.domain.profili.Paziente;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;
import viewsModels.pazienteViewsModels.PazienteViewsModels;

import views.fragment.AbstractNavigationBetweenFragment;
import views.fragment.adapter.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;


public class PazienteFragment extends AbstractNavigationBetweenFragment implements Navigation {

    private RecyclerView listPatients;

    private PazienteAdapter pazienteAdapter;

    private Button buttonAddPatient;

    private SearchView searchView;

    private PazienteViewsModels pazienteViewsModels;

    private LogopedistaViewsModels logopedistaViewsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pazienti, container, false);
        logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);
        pazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);

        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().setTitle("I tuoi pazienti");
        }
        if(searchView.isIconified())
            buttonAddPatient.setText("Paziente +");
        else buttonAddPatient.setText("+");
    }

    @Override
    public void navigationId(int id, Bundle bundle) {
        navigationTo(id, bundle);
    }

    private void loadData() {

        logopedistaViewsModels.getLogopedistaLiveData().observe(getViewLifecycleOwner(), logopedista -> {

            List<Paziente> listPatient = new ArrayList<>();

            if(logopedista.getListaPazienti() != null) {
                listPatient = logopedista.getListaPazienti();
            }

            Log.d("PazientiFragment.loadData()", "pazienti: " + ((listPatient == null) ? "null" : listPatient.toString()));

            pazienteAdapter = new PazienteAdapter(listPatient, this);
            listPatients.setAdapter(pazienteAdapter);

            searchView.setOnCloseListener(() -> {
                buttonAddPatient.setText("Paziente +");
                return false;
            });

            searchView.setOnSearchClickListener(v ->
                    buttonAddPatient.setText("+"));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("PazientiFragment", "onQueryTextSubmit: " + query);
                    pazienteAdapter.getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    pazienteAdapter.getFilter().filter(newText);
                    return true;
                }
            });
        });
    }

    private void initViews(View view) {

        buttonAddPatient = view.findViewById(R.id.buttonAddPatient);
        buttonAddPatient.setOnClickListener(v -> navigationTo(R.id.action_pazientiFragment_to_registrazionePazienteGenitoreFragment));

        searchView = view.findViewById(R.id.searchView);
        listPatients = view.findViewById(R.id.listPatients);
        listPatients.setLayoutManager(new LinearLayoutManager(requireContext()));
    }


}

