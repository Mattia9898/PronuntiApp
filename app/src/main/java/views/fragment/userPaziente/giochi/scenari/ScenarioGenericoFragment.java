package views.fragment.userPaziente.giochi.scenari;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;
import viewsModels.pazienteViewsModels.PazienteViewsModels;
import views.fragment.AbstractNavigazioneFragment;

public class ScenarioGenericoFragment extends AbstractNavigazioneFragment {

    private PazienteViewsModels mPazienteViewsModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scenario_generico,container,false);
        this.mPazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPazienteViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), Void -> {
            List<Integer> scenariGiocoValidi = mPazienteViewsModels.getScenariPaziente();
            if(scenariGiocoValidi!=null) {
                int terapiaIndex = mPazienteViewsModels.getTerapiaPazienteIndex();
                Bundle bundle = getArguments();
                Log.d("Bundle", "onViewCreated SCENARI fine scenario: " + bundle);

                //se è presente checkFineScenario, vuol dire che è stato completato uno scenario
                //quindi si deve mostrare la schermata di fine esercizio
                if(bundle != null) {
                    if(bundle.containsKey("checkFineScenario") && bundle.getBoolean("checkFineScenario")) {
                        bundle.putBoolean("checkFineScenario", true);
                    }
                }
                else {
                    bundle = new Bundle();
                }
                ScenarioFragment scenarioFragment = new ScenarioFragment();
                bundle.putInt("indiceScenarioCorrente", scenariGiocoValidi.get(scenariGiocoValidi.size() - 1));
                bundle.putInt("indiceTerapia",terapiaIndex);
                if(bundle.containsKey("indiceScenarioGioco")){
                    bundle.putInt("indiceScenarioCorrente",bundle.getInt("indiceScenarioGioco"));
                }
                scenarioFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_scenari_singolo, scenarioFragment).commit();
            }else{
                NoScenariFragment assenzaScenariFragment = new NoScenariFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_scenari_singolo, assenzaScenariFragment).commit();
            }
        });
    }


}

