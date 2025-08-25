package views.fragment.userLogopedista.appuntamenti;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.annotation.SuppressLint;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.core.widget.NestedScrollView;

import models.domain.profili.Logopedista;
import models.domain.profili.Paziente;
import models.domain.profili.Appuntamento;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;
import viewsModels.logopedistaViewsModels.controller.ModificaAppuntamentiController;

import views.fragment.userLogopedista.elencoPazienti.PazienteAdapter;
import views.fragment.AbstractNavigationFragment;
import views.fragment.DataCustomizzata;
import views.dialog.InfoDialog;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.time.LocalTime;
import java.util.List;
import java.time.LocalDate;

import android.text.TextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ImageButton;


public class AppuntamentiLogopedistaFragment extends AbstractNavigationFragment {


    private RecyclerView recyclerViewAppuntamento;

    private Button buttonAddAppuntamento;

    private Button buttonConfirmAppuntamento;

    private AppuntamentiLogopedistaAdapter appuntamentiLogopedistaAdapter;

    private CardView cardViewAppuntamento;

    private PazienteAdapter pazienteAdapter;

    private RecyclerView recyclerViewAppuntamentoPaziente;

    private NestedScrollView nestedScrollViewAppuntamento;

    private GridLayout gridLayoutTimeAppuntamento;

    private TextInputEditText patientAppuntamento;

    private LinearLayout linearLayoutPatientAppuntamento;

    private String timeAppuntamento;

    private TextInputEditText dateAppuntamento;

    private TextInputEditText placeAppuntamento;

    private TextInputEditText searchAppuntamento;

    private View viewSelectionPatient;

    private LogopedistaViewsModels logopedistaViewsModels;

    private ImageButton arrowCloseCardView;

    private List<AppuntamentiCustom> visualizationAppuntamentiCustom;

    private String idPatientSelected;

    private ModificaAppuntamentiController modificaAppuntamentiController;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appuntamenti_logopedista, container, false);
        setToolBar(view, getString(R.string.i_tuoi_appuntamenti));

        this.logopedistaViewsModels = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);
        this.modificaAppuntamentiController = logopedistaViewsModels.getModificaAppuntamentiController();

        arrowCloseCardView = view.findViewById(R.id.arrowCloseCardView);
        nestedScrollViewAppuntamento = view.findViewById(R.id.nestedScrollViewAppuntamento);

        cardViewAppuntamento = view.findViewById(R.id.cardViewAppuntamento);
        cardViewAppuntamento.setVisibility(View.GONE);

        buttonAddAppuntamento = view.findViewById(R.id.buttonAddAppuntamento);
        buttonConfirmAppuntamento = view.findViewById(R.id.buttonConfirmAppuntamento);

        searchAppuntamento = view.findViewById(R.id.searchAppuntamento);

        recyclerViewAppuntamento = view.findViewById(R.id.recyclerViewAppuntamento);
        recyclerViewAppuntamento.setLayoutManager(new LinearLayoutManager(requireContext()));

        patientAppuntamento = view.findViewById(R.id.patientAppuntamento);
        placeAppuntamento = view.findViewById(R.id.placeAppuntamento);
        dateAppuntamento = view.findViewById(R.id.dateAppuntamento);
        gridLayoutTimeAppuntamento = view.findViewById(R.id.gridLayoutTimeAppuntamento);

        recyclerViewAppuntamentoPaziente = view.findViewById(R.id.recyclerViewAppuntamentoPaziente);
        recyclerViewAppuntamentoPaziente.setLayoutManager(new LinearLayoutManager(requireContext()));

        linearLayoutPatientAppuntamento = view.findViewById(R.id.linearLayoutPatientAppuntamento);
        linearLayoutPatientAppuntamento.setVisibility(View.GONE);

        viewSelectionPatient = view.findViewById(R.id.viewSelectionPatient);
        viewSelectionPatient.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        arrowCloseCardView.setOnClickListener(v -> {
            cardViewAppuntamento.setVisibility(View.GONE);
            buttonAddAppuntamento.setVisibility(View.VISIBLE);
        });

        buttonAddAppuntamento.setOnClickListener(v -> {
            viewSelectionPatient.setVisibility(View.GONE);
            cardViewAppuntamento.setVisibility(View.VISIBLE);
            buttonAddAppuntamento.setVisibility(View.GONE);
            nestedScrollViewAppuntamento.smoothScrollTo(0, 0);
        });

        viewSelectionPatient.setOnClickListener(v -> {
            linearLayoutPatientAppuntamento.setVisibility(View.GONE);
            viewSelectionPatient.setVisibility(View.GONE);
        });

        setupAdapter();

        buttonConfirmAppuntamento.setOnClickListener(v -> {
            viewSelectionPatient.setVisibility(View.GONE);
            cardViewAppuntamento.setVisibility(View.GONE);
            buttonAddAppuntamento.setVisibility(View.VISIBLE);
            if (checkInputAppuntamento()) {
                addBooking(logopedistaViewsModels.getLogopedistaLiveData().getValue().getIdProfilo());
            }
        });

        patientAppuntamento.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    linearLayoutPatientAppuntamento.setVisibility(View.VISIBLE);
                    viewSelectionPatient.setVisibility(View.VISIBLE);
                } else {
                    viewSelectionPatient.setVisibility(View.GONE);
                    linearLayoutPatientAppuntamento.setVisibility(View.GONE);
                }
                pazienteAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        for (int i = 0; i < gridLayoutTimeAppuntamento.getChildCount(); i++) {
            View viewChild = gridLayoutTimeAppuntamento.getChildAt(i);

            if (viewChild instanceof TextView) {
                final TextView textView = (TextView) viewChild;
                textView.setOnClickListener(v -> handleTextViewSelection(textView));
            }
        }

        recyclerViewAppuntamentoPaziente.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return e.getAction() == MotionEvent.ACTION_DOWN;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d("PazienteTouchListener", "onTouchEvent: ACTION_MOVE " + e);
                }
                else if (e.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("PazienteTouchListener", "onTouchEvent: ACTION_UP " + e);
                    View childView = rv.findChildViewUnder(e.getX(), e.getY());
                    int positionView = rv.getChildAdapterPosition(childView);

                    linearLayoutPatientAppuntamento.setVisibility(View.GONE);

                    RecyclerView.Adapter adapter = rv.getAdapter();
                    Paziente paziente = ((PazienteAdapter) adapter).getItem(positionView);

                    idPatientSelected = paziente.getIdProfilo();
                    patientAppuntamento.setText(paziente.getNome() + " " + paziente.getCognome());
                    linearLayoutPatientAppuntamento.setVisibility(View.GONE);
                    viewSelectionPatient.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

        dateAppuntamento.setOnClickListener(v -> DataCustomizzata.showDatePickerDialog(getContext(), dateAppuntamento));

        searchAppuntamento.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("PazientiFragment", "onQueryTextSubmit: " + s);
                appuntamentiLogopedistaAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void setupAdapter() {

        visualizationAppuntamentiCustom = new ArrayList<>();
        Logopedista logopedista = logopedistaViewsModels.getLogopedistaLiveData().getValue();
        List<Paziente> listPaziente= logopedista.getListaPazienti() !=
                null ? logopedista.getListaPazienti() : new ArrayList<>();

        for (Appuntamento appuntamento : logopedistaViewsModels.getAppuntamentiLiveData().getValue()) {
            for (Paziente paziente : listPaziente) {
                if (appuntamento.getReferenceIdPaziente().equals(paziente.getIdProfilo())) {
                    AppuntamentiCustom appuntamentiCustom =
                            new AppuntamentiCustom(appuntamento.getIdAppuntamento(), paziente.getNome(),
                                    paziente.getCognome(), appuntamento.getLuogo(), appuntamento.getData(),
                                    appuntamento.getOra());

                    visualizationAppuntamentiCustom.add(appuntamentiCustom);
                }
            }
        }

        pazienteAdapter = new PazienteAdapter(listPaziente, null);
        recyclerViewAppuntamentoPaziente.setAdapter(pazienteAdapter);

        appuntamentiLogopedistaAdapter = new AppuntamentiLogopedistaAdapter(visualizationAppuntamentiCustom, logopedistaViewsModels);
        recyclerViewAppuntamento.setAdapter(appuntamentiLogopedistaAdapter);

    }

    private void handleTextViewSelection(TextView selectedTextView) {
        selectedTextView.setBackground(getContext().getDrawable(R.drawable.rettangolo_grigio_bordo_blu));
        for (int i = 0; i < gridLayoutTimeAppuntamento.getChildCount(); i++) {
            View childView = gridLayoutTimeAppuntamento.getChildAt(i);
            if (childView instanceof TextView) {
                TextView textView = (TextView) childView;
                if (textView != selectedTextView) {
                    textView.setBackground(getContext().getDrawable(R.drawable.rettangolo_bordo_grigio));
                }
            }
        }
        timeAppuntamento = selectedTextView.getText().toString();
    }

    private boolean checkInputAppuntamento() {
        if (idPatientSelected == null || idPatientSelected.isEmpty() ||
                patientAppuntamento.getText().toString().isEmpty() ||
                !searchPatient(patientAppuntamento.getText().toString(),
                        logopedistaViewsModels.getLogopedistaLiveData().getValue().getListaPazienti()) ||
                timeAppuntamento.isEmpty() ||
                dateAppuntamento.getText().toString().isEmpty()) {

            showErrorInputDialog();
            cardViewAppuntamento.setVisibility(View.VISIBLE);
            buttonAddAppuntamento.setVisibility(View.GONE);

            return false;
        } else {
            return true;
        }
    }

    private void showErrorInputDialog(){
        InfoDialog infoDialog = new InfoDialog(getContext(),getString(R.string.inputErratoAggiungiAppuntamento) , getString(R.string.infoOk));
        infoDialog.setOnConfirmButtonClickListener(null);
        infoDialog.show();
    }

    private boolean searchPatient(String nameComplete, List<Paziente> listPaziente) {

        String name = nameComplete.split(" ")[0];
        String surname = nameComplete.split(" ")[1];

        for (Paziente paziente : listPaziente) {
            if (paziente.getNome().equals(name)
                    && paziente.getCognome().equals(surname)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addBooking(String idLogopedist) {

        String place = placeAppuntamento.getText().toString();
        LocalDate date = LocalDate.parse(dateAppuntamento.getText().toString());
        LocalTime effectiveTimeAppuntamento = LocalTime.parse(timeAppuntamento);
        String idPatient = this.idPatientSelected;

        modificaAppuntamentiController.creazioneAppuntamento(idLogopedist, idPatient, date,
                effectiveTimeAppuntamento, place).thenAccept(appuntamento -> {

                    logopedistaViewsModels.getAppuntamentiLiveData().getValue().add(appuntamento);

                    String namePatient = patientAppuntamento.getText().toString().split(" ")[0];
                    String surnamePatient = patientAppuntamento.getText().toString().split(" ")[1];

                    appuntamentiLogopedistaAdapter.addAppuntamento(new AppuntamentiCustom(appuntamento.getIdAppuntamento(),
                            namePatient, surnamePatient, place, date, effectiveTimeAppuntamento));

                    patientAppuntamento.setText("");
                    placeAppuntamento.setText("");
                    dateAppuntamento.setText("");
                });
    }


}
