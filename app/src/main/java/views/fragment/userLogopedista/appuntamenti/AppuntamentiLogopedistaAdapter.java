package views.fragment.userLogopedista.appuntamenti;

import android.annotation.SuppressLint;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import android.widget.Filter;

import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.time.LocalTime;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import java.util.Collection;

import java.util.Collections;

import java.util.Comparator;

import java.util.List;

import it.uniba.dib.pronuntiapp.R;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import viewsModels.logopedistaViewsModels.controller.ModificaAppuntamentiController;

public class AppuntamentiLogopedistaAdapter extends RecyclerView.Adapter<AppuntamentiLogopedistaAdapter.AppuntamentiLogopedistaViewHolder> {

    private List<AppuntamentiCustom> appuntamentiFull;

    private List<AppuntamentiCustom> appuntamentiCustom;

    private LogopedistaViewsModels mLogopedistaViewModel;


    public AppuntamentiLogopedistaAdapter(List<AppuntamentiCustom> appuntamentiFull, LogopedistaViewsModels logopedistaViewModel) {
        this.appuntamentiFull = appuntamentiFull;
        appuntamentiCustom = new ArrayList<>(appuntamentiFull);
        this.mLogopedistaViewModel = logopedistaViewModel;
    }

    @Override
    public AppuntamentiLogopedistaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appuntamento_in_logopedista, parent, false);
        return new AppuntamentiLogopedistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppuntamentiLogopedistaViewHolder holder, int position) {
        appuntamentiCustom.sort((v1, v2) -> {
            int compareDate = v1.getDataAppuntamento().compareTo(v2.getDataAppuntamento());
            if(compareDate == 0){
                return v1.getOraAppuntamento().compareTo(v2.getOraAppuntamento());
            }
            return compareDate;
        });

        AppuntamentiCustom appuntamento = appuntamentiCustom.get(position);

        holder.textViewNomePaziente.setText(appuntamento.getNomePaziente());
        holder.textViewCognomePaziente.setText(appuntamento.getCognomePaziente());

        // Modifica in base al tipo di dato previsto per data appuntamento
        holder.textViewDataAppuntamento.setText(appuntamento.getDataAppuntamento().format(DateTimeFormatter.ofPattern("dd MMMM")));

        // Modifica in base al tipo di dato previsto per orario appuntamento
        holder.textViewOraAppuntamento.setText(appuntamento.getOraAppuntamento().format(DateTimeFormatter.ofPattern("HH:mm")));

        holder.textViewLuogoAppuntamento.setText(appuntamento.getLuogoAppuntamento());

        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        LocalDateTime appuntamentoDateTime = LocalDateTime.of(appuntamento.getDataAppuntamento(), appuntamento.getOraAppuntamento());

        if (appuntamentoDateTime.isBefore(now) || appuntamentoDateTime.isEqual(now)){
            holder.cardViewAppuntamentoLogopedista.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.hintTextColorDisabled));
        } else {
            holder.cardViewAppuntamentoLogopedista.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.colorPrimary));
        }

        holder.textViewNomePaziente.setOnClickListener(v -> holder.hideInfoAggiuntive());
        holder.textViewCognomePaziente.setOnClickListener(v -> holder.hideInfoAggiuntive());
        holder.textViewDataAppuntamento.setOnClickListener(v -> holder.hideInfoAggiuntive());
        holder.textViewOraAppuntamento.setOnClickListener(v -> holder.hideInfoAggiuntive());
        holder.llPazienteInAppuntamentiLogopedistaPrincipale.setOnClickListener(v -> holder.hideInfoAggiuntive());

        holder.buttonRimuoviAppuntamento.setOnClickListener(v -> {
            String idAppuntamentoToDelete = appuntamento.getIdAppuntamentoCustom();

            ModificaAppuntamentiController.eliminazioneAppuntamento(idAppuntamentoToDelete);
            mLogopedistaViewModel.rimuoviAppuntamentoFromListaAppuntamentiLiveData(idAppuntamentoToDelete);
            appuntamentiCustom.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return appuntamentiCustom.size();
    }

    public AppuntamentiCustom getItem(int position) {
        return appuntamentiCustom.get(position);
    }

    public void addAppuntamento(AppuntamentiCustom appuntamento) {
        appuntamentiCustom.add(appuntamento);
        appuntamentiFull.add(appuntamento);
        notifyDataSetChanged();
    }

    public static class AppuntamentiLogopedistaViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNomePaziente;

        private TextView textViewCognomePaziente;

        private TextView textViewDataAppuntamento;

        private TextView textViewOraAppuntamento;

        private TextView textViewLuogoAppuntamento;

        private Button buttonRimuoviAppuntamento;

        private LinearLayout llPazienteInAppuntamentiLogopedistaInfoAggiuntive;

        private LinearLayout llPazienteInAppuntamentiLogopedistaPrincipale;

        private CardView cardViewAppuntamentoLogopedista;

        public AppuntamentiLogopedistaViewHolder(View itemView) {

            super(itemView);
            llPazienteInAppuntamentiLogopedistaPrincipale = itemView.findViewById(R.id.llPazienteInAppuntamentiLogopedistaPrincipale);
            llPazienteInAppuntamentiLogopedistaInfoAggiuntive = itemView.findViewById(R.id.llPazienteInAppuntamentiLogopedistaInfoAggiuntive);
            textViewNomePaziente = itemView.findViewById(R.id.textViewNomePaziente);
            textViewCognomePaziente = itemView.findViewById(R.id.textViewCognomePaziente);
            textViewDataAppuntamento = itemView.findViewById(R.id.textViewDataNascitaPaziente);
            textViewOraAppuntamento = itemView.findViewById(R.id.textViewOrarioAppuntamentoLogopedista);
            textViewLuogoAppuntamento = itemView.findViewById(R.id.textViewLuogoAppuntamentoLogopedista);
            buttonRimuoviAppuntamento = itemView.findViewById(R.id.buttonRimuoviAppuntamentoLogopedista);
            cardViewAppuntamentoLogopedista = itemView.findViewById(R.id.cardViewPazienteInAppuntamentiLogopedista);
        }

        private void hideInfoAggiuntive() {
            if (llPazienteInAppuntamentiLogopedistaInfoAggiuntive.getVisibility() == View.VISIBLE) {
                llPazienteInAppuntamentiLogopedistaInfoAggiuntive.setVisibility(View.GONE);
            } else {
                llPazienteInAppuntamentiLogopedistaInfoAggiuntive.setVisibility(View.VISIBLE);
            }
        }
    }


    public Filter getFilter() {
        return new AppuntamentiLogopedistaAdapter.AppuntamentiFilter();
    }


    private class AppuntamentiFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<AppuntamentiCustom> filteredAppuntamenti = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredAppuntamenti.addAll(appuntamentiFull);
            } else {
                String query = charSequence.toString().toLowerCase();
                for (AppuntamentiCustom appuntamento : appuntamentiFull) {
                    String nome = appuntamento.getNomePaziente().toLowerCase();
                    String cognome = appuntamento.getCognomePaziente().toLowerCase();
                    if (nome.contains(query) || cognome.contains(query)) {
                        filteredAppuntamenti.add(appuntamento);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredAppuntamenti;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            appuntamentiCustom.clear();
            appuntamentiCustom.addAll((List<AppuntamentiCustom>) filterResults.values);
            notifyDataSetChanged();
        }
    }

}