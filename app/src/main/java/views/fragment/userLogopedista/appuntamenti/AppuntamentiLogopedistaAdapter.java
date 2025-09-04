package views.fragment.userLogopedista.appuntamenti;


import it.uniba.dib.pronuntiapp.R;

import viewsModels.logopedistaViewsModels.controller.EditAppuntamentiController;
import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

import java.util.List;
import java.util.ArrayList;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Filter;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;


public class AppuntamentiLogopedistaAdapter extends RecyclerView.Adapter<AppuntamentiLogopedistaAdapter.AppuntamentiLogopedistaViewHolder> {

    private LogopedistaViewsModels logopedistaViewsModels;

    private List<AppuntamentiCustom> appuntamentiCustom;

    private List<AppuntamentiCustom> appuntamentiFullList;


    public AppuntamentiLogopedistaAdapter(List<AppuntamentiCustom> appuntamentiFullList,
                                          LogopedistaViewsModels logopedistaViewModel) {

        this.appuntamentiFullList = appuntamentiFullList;
        appuntamentiCustom = new ArrayList<>(appuntamentiFullList);
        this.logopedistaViewsModels = logopedistaViewModel;
    }

    @Override
    public AppuntamentiLogopedistaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appuntamento_in_logopedista, parent, false);
        return new AppuntamentiLogopedistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppuntamentiLogopedistaViewHolder holder, int position) {

        appuntamentiCustom.sort((v1, v2) -> {
            int comparationDate = v1.getDateAppuntamento().compareTo(v2.getDateAppuntamento());
            if(comparationDate == 0){
                return v1.getTimeAppuntamento().compareTo(v2.getTimeAppuntamento());
            }
            return comparationDate;
        });

        AppuntamentiCustom appuntamenti = appuntamentiCustom.get(position);

        holder.namePatient.setText(appuntamenti.getNamePatient());
        holder.surnamePatient.setText(appuntamenti.getSurnamePatient());

        // modifica in base al tipo di dato previsto per dateAppuntamento
        holder.dateAppuntamento.setText(appuntamenti.getDateAppuntamento().format(DateTimeFormatter.ofPattern("dd MMMM")));

        // modifica in base al tipo di dato previsto per timeAppuntamento
        holder.timeAppuntamento.setText(appuntamenti.getTimeAppuntamento().format(DateTimeFormatter.ofPattern("HH:mm")));

        holder.placeAppuntamento.setText(appuntamenti.getPlaceAppuntamento());

        LocalDateTime actualDate = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        LocalDateTime appuntamentoDate = LocalDateTime.of(appuntamenti.getDateAppuntamento(), appuntamenti.getTimeAppuntamento());

        if (appuntamentoDate.isBefore(actualDate) || appuntamentoDate.isEqual(actualDate)){
            holder.cardViewInfoAppuntamento.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.hintTextColorDisabled));
        } else {
            holder.cardViewInfoAppuntamento.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.colorPrimary));
        }

        holder.namePatient.setOnClickListener(v -> holder.hideOtherInfo());
        holder.surnamePatient.setOnClickListener(v -> holder.hideOtherInfo());
        holder.dateAppuntamento.setOnClickListener(v -> holder.hideOtherInfo());
        holder.timeAppuntamento.setOnClickListener(v -> holder.hideOtherInfo());
        holder.mainInfoAppuntamento.setOnClickListener(v -> holder.hideOtherInfo());

        holder.buttonRemoveAppuntamento.setOnClickListener(v -> {
            String deleteIdAppuntamento = appuntamenti.getIdAppuntamentoCustom();
            EditAppuntamentiController.deleteAppuntamento(deleteIdAppuntamento);
            logopedistaViewsModels.removeAppuntamentoFromListAppuntamentiLiveData(deleteIdAppuntamento);
            appuntamentiCustom.remove(position);
            notifyDataSetChanged();
        });
    }


    public void addAppuntamento(AppuntamentiCustom newAppuntamento) {
        appuntamentiCustom.add(newAppuntamento);
        appuntamentiFullList.add(newAppuntamento);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return appuntamentiCustom.size();
    }


    private class AppuntamentiFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<AppuntamentiCustom> listAppuntamentiCustom = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                listAppuntamentiCustom.addAll(appuntamentiFullList);
            } else {
                String query = charSequence.toString().toLowerCase();
                for (AppuntamentiCustom appuntamenti : appuntamentiFullList) {
                    String name = appuntamenti.getNamePatient().toLowerCase();
                    String surname = appuntamenti.getSurnamePatient().toLowerCase();
                    if (name.contains(query) || surname.contains(query)) {
                        listAppuntamentiCustom.add(appuntamenti);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = listAppuntamentiCustom;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            appuntamentiCustom.clear();
            appuntamentiCustom.addAll((List<AppuntamentiCustom>) filterResults.values);
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        return new AppuntamentiLogopedistaAdapter.AppuntamentiFilter();
    }


    public static class AppuntamentiLogopedistaViewHolder extends RecyclerView.ViewHolder {

        private TextView namePatient;

        private TextView surnamePatient;

        private TextView dateAppuntamento;

        private TextView timeAppuntamento;

        private TextView placeAppuntamento;

        private Button buttonRemoveAppuntamento;

        private LinearLayout otherInfoAppuntamento;

        private LinearLayout mainInfoAppuntamento;

        private CardView cardViewInfoAppuntamento;

        public AppuntamentiLogopedistaViewHolder(View itemView) {

            super(itemView);
            mainInfoAppuntamento = itemView.findViewById(R.id.mainInfoAppuntamento);
            otherInfoAppuntamento = itemView.findViewById(R.id.otherInfoAppuntamento);
            namePatient = itemView.findViewById(R.id.textViewNomePaziente);
            surnamePatient = itemView.findViewById(R.id.textViewCognomePaziente);
            dateAppuntamento = itemView.findViewById(R.id.textViewDataNascitaPaziente);
            timeAppuntamento = itemView.findViewById(R.id.textViewOrarioAppuntamentoLogopedista);
            placeAppuntamento = itemView.findViewById(R.id.textViewLuogoAppuntamentoLogopedista);
            buttonRemoveAppuntamento = itemView.findViewById(R.id.buttonRimuoviAppuntamentoLogopedista);
            cardViewInfoAppuntamento = itemView.findViewById(R.id.cardViewInfoAppuntamento);
        }

        private void hideOtherInfo() {
            if (otherInfoAppuntamento.getVisibility() == View.VISIBLE) {
                otherInfoAppuntamento.setVisibility(View.GONE);
            } else {
                otherInfoAppuntamento.setVisibility(View.VISIBLE);
            }
        }
    }


}