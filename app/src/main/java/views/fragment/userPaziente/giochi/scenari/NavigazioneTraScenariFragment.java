package views.fragment.userPaziente.giochi.scenari;

package it.uniba.dib.sms2324.num15.PronuntiApp.views.fragment.user_paziente.giochi.scenari;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import java.time.format.DateTimeFormatter;
import java.util.List;

import it.uniba.dib.pronuntiapp.R;
import models.domain.terapie.Terapia;
import viewsModels.pazienteViewsModels.PazienteViewsModels;
import views.dialog.InfoDialog;
import views.fragment.AbstractNavigazioneFragment;

public class NavigazioneTraScenariFragment extends AbstractNavigazioneFragment {
    private TextView textViewDataScenario;
    private ImageButton buttonIndietroScenario;
    private ImageButton buttonAvantiScenario;
    private PazienteViewsModels mPazienteViewModel;
    private LinearLayout linearLayout;
    private int currentScenarioIndex = 0;
    private int maxSize;
    private Terapia terapia;
    private int terapiaIndex;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_nav_scenari, container, false);
        mPazienteViewModel = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);
        textViewDataScenario = view.findViewById(R.id.textViewDataScenario);
        buttonIndietroScenario = view.findViewById(R.id.buttonIndietroScenario);
        buttonAvantiScenario = view.findViewById(R.id.buttonAvantiScenario);
        linearLayout = view.findViewById(R.id.myLinearLayout);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPazienteViewModel.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {
            List<Integer> scenariIndexListmPazienteViewModel = mPazienteViewModel.getScenariPaziente();
            if(scenariIndexListmPazienteViewModel!= null) {
                terapiaIndex = mPazienteViewModel.getTerapiaPazienteIndex();
                maxSize = scenariIndexListmPazienteViewModel.size() - 1;
                currentScenarioIndex = maxSize;

                List<Terapia> terapie = paziente.getTerapie();
                terapia = terapie.get(terapiaIndex);
                textViewDataScenario.setText(terapia.getScenariGioco().get(scenariIndexListmPazienteViewModel.get(currentScenarioIndex)).getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                buttonIndietroScenario.setOnClickListener(v -> navigaScenarioPrecedente(scenariIndexListmPazienteViewModel));
                buttonAvantiScenario.setOnClickListener(v -> navigaScenarioSuccessivo(scenariIndexListmPazienteViewModel));
            }else{
                linearLayout.setVisibility(View.GONE);
            }
        });
    }

    private void navigaScenarioSuccessivo(List<Integer> listaIndici) {
        if(currentScenarioIndex+1<=maxSize) {

            Bundle bundle = new Bundle();
            ScenarioFragment scenarioFragment = new ScenarioFragment();
            currentScenarioIndex +=1;
            bundle.putInt("indiceScenarioCorrente", listaIndici.get(currentScenarioIndex));
            bundle.putInt("indiceTerapia",terapiaIndex);
            textViewDataScenario.setText(terapia.getScenariGioco().get(listaIndici.get(currentScenarioIndex)).getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            scenarioFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_scenari_singolo, scenarioFragment).commit();
        }else{
            showInfoDialog(true);
        }
    }

    private void navigaScenarioPrecedente(List<Integer> listaIndici) {
        if(currentScenarioIndex-1>=0) {
            textViewDataScenario.setText(terapia.getScenariGioco().get(currentScenarioIndex).getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            Bundle bundle = new Bundle();
            ScenarioFragment scenarioFragment = new ScenarioFragment();
            currentScenarioIndex-=1;
            bundle.putInt("indiceScenarioCorrente", listaIndici.get(currentScenarioIndex));
            bundle.putInt("indiceTerapia",terapiaIndex);
            textViewDataScenario.setText(terapia.getScenariGioco().get(listaIndici.get(currentScenarioIndex)).getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            Log.d("NavindiceScenarioCorrente","NavindiceScenarioCorrente "+listaIndici.get(currentScenarioIndex));
            scenarioFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_scenari_singolo, scenarioFragment).commit();
        }else{
            showInfoDialog(false);
        }
    }

    private void showInfoDialog(boolean error){
        if(error){
            InfoDialog infoDialog = new InfoDialog(requireActivity(),getString(R.string.navScenariNoticeForward),getString(R.string.ok));
            infoDialog.setOnConfermaButtonClickListener(() -> {});
            setDialogUI(infoDialog);
            infoDialog.show();
        }else{
            InfoDialog infoDialog = new InfoDialog(requireActivity(),getString(R.string.navScenariNoticeBackward),getString(R.string.ok));
            infoDialog.setOnConfermaButtonClickListener(() -> {});
            setDialogUI(infoDialog);
            infoDialog.show();
        }
    }

    private void setDialogUI(InfoDialog dialog){
        dialog.createCustome().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
