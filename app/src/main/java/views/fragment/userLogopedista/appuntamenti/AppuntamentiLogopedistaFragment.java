package views.fragment.userLogopedista.appuntamenti;

import android.annotation.SuppressLint;

import android.app.DatePickerDialog;

import android.os.Bundle;

import android.text.Editable;

import android.text.TextWatcher;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.MotionEvent;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import android.widget.GridLayout;

import android.widget.ImageButton;

import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.cardview.widget.CardView;

import androidx.core.widget.NestedScrollView;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;

import java.time.LocalTime;

import java.util.ArrayList;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import models.domain.profili.Appuntamento;

import models.domain.profili.Logopedista;

import models.domain.profili.Paziente;

import viewsModels.logopedistaViewsModels.controller.ModificaAppuntamentiController;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import views.dialog.InfoDialog;

import views.fragment.AbstractNavigazioneFragment;

import views.fragment.userLogopedista.elencoPazienti.PazienteAdapter;

import views.fragment.DataCustomizzata;

public class AppuntamentiLogopedistaFragment extends AbstractNavigazioneFragment {

    private RecyclerView recyclerViewAppuntamenti;

    private PazienteAdapter adapterPazientiAppuntamentoLogopedista;

    private AppuntamentiLogopedistaAdapter adapterAppuntamenti;

    private Button addAppuntamentoButton;

    private CardView cardViewAppuntamento;

    private RecyclerView recyclerViewPazienteAppuntamentoLogopedista;

    private Button confermaAppuntamentoButton;

    private NestedScrollView nestedScrollView;

    private TextInputEditText editTextAppuntamentoPaziente;

    private TextInputEditText editTextLuogo;

    private TextInputEditText editTextDataAppuntemento;

    private GridLayout gridLayoutOrarioAppuntamento;

    private DatePickerDialog datePickerDialog;

    private TextInputEditText searchViewAppuntamentiLogopedista;

    private LinearLayout linearLayoutPazienteAppuntamentoLogopedista;

    private String orarioAppuntamento;

    private ImageButton closeCardUpButton;

    private View viewOverlaySelezionePaziente;

    private String idPazienteSelezionato;

    private List<AppuntamentiCustom> appuntamentiVisualizzazione;

    private LogopedistaViewsModels mLogopedistaViewModel;

    private ModificaAppuntamentiController mController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appuntamenti_logopedista, container, false);
        setToolBar(view, getString(R.string.i_tuoi_appuntamenti));

        this.mLogopedistaViewModel = new ViewModelProvider(requireActivity()).get(LogopedistaViewsModels.class);
        this.mController = mLogopedistaViewModel.getModificaAppuntamentiController();

        closeCardUpButton = view.findViewById(R.id.imageButtonArrowUpAppuntamentoLogopedista);
        nestedScrollView = view.findViewById(R.id.nestedScrollViewAppuntamentiLogopedista);
        cardViewAppuntamento = view.findViewById(R.id.cardViewNuovoAppuntamentoLogopedista);
        cardViewAppuntamento.setVisibility(View.GONE);
        addAppuntamentoButton = view.findViewById(R.id.buttonNuovoAppuntamentoLogopedista);
        confermaAppuntamentoButton = view.findViewById(R.id.buttonConfermaAppuntamentoLogopedista);
        searchViewAppuntamentiLogopedista = view.findViewById(R.id.searchBarAppuntamentiLogopedista);
        recyclerViewAppuntamenti = view.findViewById(R.id.recyclerViewAppuntamentiLogopedista);
        recyclerViewAppuntamenti.setLayoutManager(new LinearLayoutManager(requireContext()));


        editTextAppuntamentoPaziente = view.findViewById(R.id.textInputEditTextPazienteAppuntamentoLogopedista);
        editTextLuogo = view.findViewById(R.id.textInputEditTextLuogoAppuntamentoLogopedista);
        editTextDataAppuntemento = view.findViewById(R.id.textInputEditTextDataAppuntamentoLogopedista);
        gridLayoutOrarioAppuntamento = view.findViewById(R.id.gridLayoutAppuntamentoLogopedista);
        recyclerViewPazienteAppuntamentoLogopedista = view.findViewById(R.id.recyclerViewPazienteAppuntamentoLogopedista);
        recyclerViewPazienteAppuntamentoLogopedista.setLayoutManager(new LinearLayoutManager(requireContext()));
        linearLayoutPazienteAppuntamentoLogopedista = view.findViewById(R.id.llPazienteAppuntamentoLogopedista);

        linearLayoutPazienteAppuntamentoLogopedista.setVisibility(View.GONE);

        viewOverlaySelezionePaziente = view.findViewById(R.id.viewOverlayAppuntamentiLogopedistaSelezionePaziente);
        viewOverlaySelezionePaziente.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        closeCardUpButton.setOnClickListener(v -> {
            cardViewAppuntamento.setVisibility(View.GONE);
            addAppuntamentoButton.setVisibility(View.VISIBLE);
        });

        addAppuntamentoButton.setOnClickListener(v -> {
            viewOverlaySelezionePaziente.setVisibility(View.GONE);
            cardViewAppuntamento.setVisibility(View.VISIBLE);
            addAppuntamentoButton.setVisibility(View.GONE);
            nestedScrollView.smoothScrollTo(0, 0);
        });

        viewOverlaySelezionePaziente.setOnClickListener(v -> {
            linearLayoutPazienteAppuntamentoLogopedista.setVisibility(View.GONE);
            viewOverlaySelezionePaziente.setVisibility(View.GONE);
        });

        setUpAdapters();

        confermaAppuntamentoButton.setOnClickListener(v -> {
            viewOverlaySelezionePaziente.setVisibility(View.GONE);
            cardViewAppuntamento.setVisibility(View.GONE);
            addAppuntamentoButton.setVisibility(View.VISIBLE);
            if (checkInputAppuntamento()) {
                eseguiAggiuntaPrenotazione(mLogopedistaViewModel.getLogopedistaLiveData().getValue().getIdProfilo());
            }
        });

        editTextAppuntamentoPaziente.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    linearLayoutPazienteAppuntamentoLogopedista.setVisibility(View.VISIBLE);
                    viewOverlaySelezionePaziente.setVisibility(View.VISIBLE);
                } else {
                    viewOverlaySelezionePaziente.setVisibility(View.GONE);
                    linearLayoutPazienteAppuntamentoLogopedista.setVisibility(View.GONE);
                }
                adapterPazientiAppuntamentoLogopedista.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        for (int i = 0; i < gridLayoutOrarioAppuntamento.getChildCount(); i++) {
            View child = gridLayoutOrarioAppuntamento.getChildAt(i);

            if (child instanceof TextView) {
                final TextView textView = (TextView) child;
                textView.setOnClickListener(v -> handleTextViewSelection(textView));
            }
        }

        recyclerViewPazienteAppuntamentoLogopedista.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

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
                    int position = rv.getChildAdapterPosition(childView);
                    linearLayoutPazienteAppuntamentoLogopedista.setVisibility(View.GONE);
                    RecyclerView.Adapter adapter = rv.getAdapter();
                    Paziente paziente = ((PazienteAdapter) adapter).getItem(position);
                    idPazienteSelezionato = paziente.getIdProfilo();
                    editTextAppuntamentoPaziente.setText(paziente.getNome() + " " + paziente.getCognome());
                    linearLayoutPazienteAppuntamentoLogopedista.setVisibility(View.GONE);
                    viewOverlaySelezionePaziente.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

        editTextDataAppuntemento.setOnClickListener(v -> DataCustomizzata.showDatePickerDialog(getContext(), editTextDataAppuntemento));

        searchViewAppuntamentiLogopedista.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("PazientiFragment", "onQueryTextSubmit: " + s);
                adapterAppuntamenti.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void setUpAdapters() {

        appuntamentiVisualizzazione = new ArrayList<>();
        Logopedista logopedista = mLogopedistaViewModel.getLogopedistaLiveData().getValue();
        List<Paziente> listaPazienti = logopedista.getListaPazienti() != null ? logopedista.getListaPazienti() : new ArrayList<>();

        for (Appuntamento appuntamento : mLogopedistaViewModel.getAppuntamentiLiveData().getValue()) {
            for (Paziente paziente : listaPazienti) {
                if (appuntamento.getReferenceIdPaziente().equals(paziente.getIdProfilo())) {
                    AppuntamentiCustom appuntamentiCustom = new AppuntamentiCustom(appuntamento.getIdAppuntamento(),paziente.getNome(), paziente.getCognome(), appuntamento.getLuogo(), appuntamento.getData(), appuntamento.getOra());
                    appuntamentiVisualizzazione.add(appuntamentiCustom);
                }
            }
        }

        adapterPazientiAppuntamentoLogopedista = new PazienteAdapter(listaPazienti, null);
        recyclerViewPazienteAppuntamentoLogopedista.setAdapter(adapterPazientiAppuntamentoLogopedista);

        adapterAppuntamenti = new AppuntamentiLogopedistaAdapter(appuntamentiVisualizzazione, mLogopedistaViewModel);
        recyclerViewAppuntamenti.setAdapter(adapterAppuntamenti);

    }

    private boolean checkInputAppuntamento() {
        if (idPazienteSelezionato == null || idPazienteSelezionato.isEmpty() || editTextAppuntamentoPaziente.getText().toString().isEmpty() || !cercaPazienteInLista(editTextAppuntamentoPaziente.getText().toString(), mLogopedistaViewModel.getLogopedistaLiveData().getValue().getListaPazienti()) || orarioAppuntamento.isEmpty() || editTextDataAppuntemento.getText().toString().isEmpty()) {

            showErrorInputDialog();
            cardViewAppuntamento.setVisibility(View.VISIBLE);
            addAppuntamentoButton.setVisibility(View.GONE);

            return false;
        } else {
            return true;
        }
    }

    private boolean cercaPazienteInLista(String nomeCognome, List<Paziente> pazienti) {

        String nome = nomeCognome.split(" ")[0];
        String cognome = nomeCognome.split(" ")[1];

        for (Paziente p : pazienti) {
            if (p.getNome().equals(nome) && p.getCognome().equals(cognome)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void eseguiAggiuntaPrenotazione(String idLogopedista) {

        String luogoAppuntamento = editTextLuogo.getText().toString();
        LocalDate dataAppuntamento = LocalDate.parse(editTextDataAppuntemento.getText().toString());
        LocalTime orarioAppuntamentoEffettivo = LocalTime.parse(orarioAppuntamento);
        String idPaziente = this.idPazienteSelezionato;

        mController.creazioneAppuntamento(idLogopedista, idPaziente, dataAppuntamento, orarioAppuntamentoEffettivo, luogoAppuntamento)
                .thenAccept(appuntamento -> {
                    mLogopedistaViewModel.getAppuntamentiLiveData().getValue().add(appuntamento);

                    String nomePaziente = editTextAppuntamentoPaziente.getText().toString().split(" ")[0];
                    String cognomePaziente = editTextAppuntamentoPaziente.getText().toString().split(" ")[1];
                    adapterAppuntamenti.addAppuntamento(new AppuntamentiCustom(appuntamento.getIdAppuntamento(), nomePaziente, cognomePaziente, luogoAppuntamento, dataAppuntamento, orarioAppuntamentoEffettivo));

                    editTextAppuntamentoPaziente.setText("");
                    editTextLuogo.setText("");
                    editTextDataAppuntemento.setText("");
                });
    }

    private void handleTextViewSelection(TextView selectedTextView) {
        selectedTextView.setBackground(getContext().getDrawable(R.drawable.rettangolo_grigio_bordo_blu));
        for (int i = 0; i < gridLayoutOrarioAppuntamento.getChildCount(); i++) {
            View child = gridLayoutOrarioAppuntamento.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                if (textView != selectedTextView) {
                    textView.setBackground(getContext().getDrawable(R.drawable.rettangolo_bordo_grigio));
                }
            }
        }
        orarioAppuntamento = selectedTextView.getText().toString();
    }

    private void showErrorInputDialog(){
        InfoDialog inputErratoDialog = new InfoDialog(getContext(),getString(R.string.inputErratoAggiungiAppuntamento) , getString(R.string.infoOk));
        inputErratoDialog.setOnConfermaButtonClickListener(null);
        inputErratoDialog.show();
    }

}
