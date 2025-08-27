package views.fragment.userPaziente.giochi.scenari;


import it.uniba.dib.pronuntiapp.R;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import models.domain.terapie.Terapia;
import viewsModels.pazienteViewsModels.PazienteViewsModels;
import views.dialog.InfoDialog;
import views.fragment.AbstractNavigationBetweenFragment;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class NavigationScenariFragment extends AbstractNavigationBetweenFragment {

    private TextView dateScenery;

    private ImageButton buttonBackScenery;

    private ImageButton buttonNextScenery;

    private PazienteViewsModels pazienteViewsModels;

    private LinearLayout mainLinearLayout;

    private int actualIndexScenery = 0;

    private int indexTherapy;

    private int maxSize;

    private Terapia terapia;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_navigation_scenari, container, false);

        pazienteViewsModels = new ViewModelProvider(requireActivity()).get(PazienteViewsModels.class);

        dateScenery = view.findViewById(R.id.dateScenery);
        buttonBackScenery = view.findViewById(R.id.buttonBackScenery);
        buttonNextScenery = view.findViewById(R.id.buttonNextScenery);
        mainLinearLayout = view.findViewById(R.id.mainLinearLayout);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pazienteViewsModels.getPazienteLiveData().observe(getViewLifecycleOwner(), paziente -> {
            List<Integer> listIndexScenery = pazienteViewsModels.getScenariPaziente();
            if(listIndexScenery!= null) {

                indexTherapy = pazienteViewsModels.getTerapiaPazienteIndex();
                maxSize = listIndexScenery.size() - 1;
                actualIndexScenery = maxSize;

                List<Terapia> listTherapies = paziente.getTerapie();

                terapia = listTherapies.get(indexTherapy);
                dateScenery.setText(terapia.getListScenariGioco().
                        get(listIndexScenery.get(actualIndexScenery)).getDataInizioScenarioGioco().
                        format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                buttonBackScenery.setOnClickListener(v -> goToBackScenery(listIndexScenery));
                buttonNextScenery.setOnClickListener(v -> goToNextScenery(listIndexScenery));
            }else{
                mainLinearLayout.setVisibility(View.GONE);
            }
        });
    }

    private void showInfoDialog(boolean dialogError){
        if(dialogError){
            InfoDialog infoDialog = new InfoDialog
                    (requireActivity(), getString(R.string.navScenariNoticeForward),getString(R.string.ok));

            infoDialog.setOnConfirmButtonClickListener(() -> {});
            setUIDialog(infoDialog);
            infoDialog.show();
        }else{
            InfoDialog infoDialog = new InfoDialog
                    (requireActivity(), getString(R.string.navScenariNoticeBackward),getString(R.string.ok));

            infoDialog.setOnConfirmButtonClickListener(() -> {});
            setUIDialog(infoDialog);
            infoDialog.show();
        }
    }

    private void setUIDialog(InfoDialog infoDialog){
        infoDialog.createCustome().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    private void goToBackScenery(List<Integer> listIndexScenery) {
        if(actualIndexScenery-1 >= 0) {
            dateScenery.setText(terapia.getListScenariGioco().
                    get(actualIndexScenery).getDataInizioScenarioGioco().
                    format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            Bundle bundle = new Bundle();
            ScenarioFragment scenarioFragment = new ScenarioFragment();

            actualIndexScenery-=1;

            bundle.putInt("actualIndexScenery", listIndexScenery.get(actualIndexScenery));
            bundle.putInt("indexTherapy", indexTherapy);

            dateScenery.setText(terapia.getListScenariGioco().
                    get(listIndexScenery.get(actualIndexScenery)).getDataInizioScenarioGioco().
                    format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            Log.d("navigationActualIndexScenery","navigationActualIndexScenery " + listIndexScenery.get(actualIndexScenery));

            scenarioFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().
                    replace(R.id.fragment_scenari_singolo, scenarioFragment).commit();
        }else{
            showInfoDialog(false);
        }
    }

    private void goToNextScenery(List<Integer> listIndexScenery) {
        if(actualIndexScenery+1 <= maxSize) {

            Bundle bundle = new Bundle();
            ScenarioFragment scenarioFragment = new ScenarioFragment();

            actualIndexScenery += 1;

            bundle.putInt("actualIndexScenery", listIndexScenery.get(actualIndexScenery));
            bundle.putInt("indexTherapy", indexTherapy);

            dateScenery.setText(terapia.getListScenariGioco().
                    get(listIndexScenery.get(actualIndexScenery)).getDataInizioScenarioGioco().
                    format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            scenarioFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().
                    replace(R.id.fragment_scenari_singolo, scenarioFragment).commit();
        }else{
            showInfoDialog(true);
        }
    }


}
