package views.fragment.userPaziente.giochi.scenari;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import androidx.lifecycle.ViewModelProvider;

import viewsModels.pazienteViewsModels.PazienteViewsModels;
import views.fragment.AbstractNavigationBetweenFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ScenarioGenericoFragment extends AbstractNavigationBetweenFragment {

    private PazienteViewsModels pazienteViewsModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scenario_generico,container,false);

        this.pazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pazienteViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), Void -> {
            List<Integer> listSceneryGames = pazienteViewsModels.getScenariPaziente();
            if(listSceneryGames != null) {
                int indexTherapy = pazienteViewsModels.getTerapiaPazienteIndex();
                Bundle bundle = getArguments();
                Log.d("Bundle", "onViewCreated fine scenario: " + bundle);

                // se è presente checkEndScenery --> è stato completato uno scenario
                // quindi mostrare la schermata di fine esercizio
                if(bundle != null) {
                    if(bundle.containsKey("checkEndScenery") && bundle.getBoolean("checkEndScenery")) {
                        bundle.putBoolean("checkEndScenery", true);
                    }
                }
                else {
                    bundle = new Bundle();
                }

                ScenarioFragment scenarioFragment = new ScenarioFragment();
                bundle.putInt("actualIndexScenery", listSceneryGames.get(listSceneryGames.size() - 1));
                bundle.putInt("indexTherapy", indexTherapy);

                if(bundle.containsKey("indexSceneryGame")){
                    bundle.putInt("actualIndexScenery",bundle.getInt("indexSceneryGame"));
                }

                scenarioFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().
                        replace(R.id.fragment_scenari_singolo, scenarioFragment).commit();

            }else{
                NessunGiocoDisponibileFragment nessunGiocoDisponibileFragment = new NessunGiocoDisponibileFragment();
                getParentFragmentManager().beginTransaction().
                        replace(R.id.fragment_scenari_singolo, nessunGiocoDisponibileFragment).commit();
            }
        });
    }


}

