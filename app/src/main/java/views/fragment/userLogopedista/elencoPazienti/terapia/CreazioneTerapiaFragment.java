package views.fragment.userLogopedista.elencoPazienti.terapia;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Logopedista;

import models.domain.scenariGioco.ScenarioGioco;

import models.domain.terapie.Terapia;

import views.dialog.InfoDialog;

import views.fragment.AbstractNavigazioneFragment;

import views.fragment.DataCustomizzata;

import androidx.lifecycle.ViewModelProvider;

import models.domain.profili.Paziente;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

public class CreazioneTerapiaFragment extends AbstractNavigazioneFragment implements SalvataggioScenario{

    private TextInputEditText dataInizio;

    private TextInputEditText dataFine;

    private Button buttonAddScenario;

    private Button buttonSalvataggioTerapia;

    private Terapia terapia;

    private boolean isFirstScenario = true;

    private LogopedistaViewsModels mLogopedistaViewsModels;

    private Bundle bundle;

    private String idPaziente;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_creazione_terapia, container, false);
        mLogopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);

        dataFine = view.findViewById(R.id.textInputEditTextDataFineTerapia);
        dataInizio = view.findViewById(R.id.textInputEditTextDataInizioTerapia);

        buttonAddScenario = view.findViewById(R.id.buttonAddScenario);
        buttonSalvataggioTerapia = view.findViewById(R.id.buttonSalvaTerapia);
        bundle = getArguments();
        idPaziente = bundle.getString("idPaziente");
        buttonSalvataggioTerapia.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mLogopedistaViewsModels.getLogopedistaLiveData().observe(getViewLifecycleOwner(), Void -> {
            Log.d("idPaziente",idPaziente);
            Paziente paziente = mLogopedistaViewsModels.getPazienteById(idPaziente);
            String nomePaziente = paziente.getNome();
            String cognomePaziente = paziente.getCognome();
            setToolBar(view,getString(R.string.creaTerapia) + " " + nomePaziente+" "+cognomePaziente);

            dataInizio.setOnClickListener(v -> DataCustomizzata.showDatePickerDialog(getContext(), dataInizio));
            dataFine.setOnClickListener(v -> DataCustomizzata.showDatePickerDialog(getContext(), dataFine));

            buttonAddScenario.setOnClickListener(v -> addScenario());

            buttonSalvataggioTerapia.setOnClickListener(v-> saveTerapia(idPaziente,nomePaziente,cognomePaziente));

        });


    }
    private void showErrorDialog(int messaggio){
        InfoDialog infoDialog = new InfoDialog(getContext(), getString(messaggio), getString(R.string.tastoRiprova));
        infoDialog.setOnConfermaButtonClickListener(null);
        infoDialog.show();
    }
    private void addScenario(){
        if(dataInizio.getText().toString().isEmpty() || dataFine.getText().toString().isEmpty()){
            showErrorDialog(R.string.compilaPrimaTutto);
        }else if(LocalDate.parse(dataInizio.getText().toString()).isAfter(LocalDate.parse(dataFine.getText().toString()))){
            showErrorDialog(R.string.insertDate);
        }else if(verificaDate(mLogopedistaViewsModels)){
            showErrorDialog(R.string.checkDate);
        }
        else {
            if(isFirstScenario){
                Log.d("Terapia","isFristScenario");
                terapia = new Terapia(LocalDate.parse(dataInizio.getText().toString()), LocalDate.parse(dataFine.getText().toString()));
                isFirstScenario = false;
            }
            Log.d("Terapia",terapia.toString());
            buttonAddScenario.setVisibility(View.GONE);

            //passare data minima e massima per datePicker
            bundle = new Bundle();
            bundle.putString("dataInizio",dataInizio.getText().toString());
            bundle.putString("dataFine",dataFine.getText().toString());
            CreazioneScenarioFragment creazioneScenarioFragment = new CreazioneScenarioFragment(this);
            creazioneScenarioFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewNuovoScenario, creazioneScenarioFragment).commit();
            buttonSalvataggioTerapia.setVisibility(View.GONE);
        }
    }

    private boolean verificaDate(LogopedistaViewsModels logopedistaViewModel){
        Paziente paziente = logopedistaViewModel.getPazienteById(idPaziente);
        List<Terapia> terapiePaziente = paziente.getTerapie();
        LocalDate dataInizioNuovaTerapia = LocalDate.parse(dataInizio.getText().toString());
        LocalDate dataFineNuovaTerapia = LocalDate.parse(dataFine.getText().toString());
        if(terapiePaziente !=null) {
            for (Terapia terapiaPaziente : terapiePaziente) {
                LocalDate dataInizioTerapiaEsistente = terapiaPaziente.getDataInizioTerapia();
                LocalDate dataFineTerapiaEsistente = terapiaPaziente.getDataFineTerapia();
                if ((dataInizioNuovaTerapia.isEqual(dataInizioTerapiaEsistente) || dataInizioNuovaTerapia.isBefore(dataInizioTerapiaEsistente)) || (dataFineNuovaTerapia.isEqual(dataFineTerapiaEsistente) || dataFineNuovaTerapia.isBefore(dataFineTerapiaEsistente))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void saveTerapia(String idPaziente,String nome,String cognome){
        mLogopedistaViewsModels.addTerapiaInPaziente(terapia,idPaziente);
        mLogopedistaViewsModels.aggiornaLogopedistaRemoto();
        Bundle bundle1 = new Bundle();
        bundle1.putString("idPaziente",idPaziente);
        bundle1.putString("nomePaziente",nome);
        bundle1.putString("cognomePaziente",cognome);
        navigateTo(R.id.action_creazioneTerapiaFragment_to_schedaPazienteFragment,bundle1);
    }

    @Override
    public void saveScenario(ScenarioGioco scenarioGioco) {
        Log.d("Terapia","terapia in saveScenario: " + terapia.toString());
        terapia.addListScenarioGioco(scenarioGioco);
        buttonAddScenario.setVisibility(View.VISIBLE);
        buttonSalvataggioTerapia.setVisibility(View.VISIBLE);

    }

}
